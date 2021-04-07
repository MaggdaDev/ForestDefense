/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import maggdaforestdefense.network.server.ServerSocketHandler;

/**
 *
 * @author David
 */
public class NetworkManager extends WebSocketClient {

    

    private ClientCommandHandler commandHandler;
    private static NetworkManager instance;
    private UdpReceiver udpReceiver;

    private CountDownLatch waitLatch = new CountDownLatch(1);
    
    
    

    public NetworkManager(URI serverURI) {
        super(serverURI);
        
        udpReceiver = new UdpReceiver();
        udpReceiver.start();
    }

    public void update() {
        commandHandler.handleInput();
    }
    
    public void resetCommandHandler() {
        commandHandler.reset();
    }

   public void startConnection() {
       Logger.debugClient("Connecting...");
       commandHandler = new ClientCommandHandler();
       this.connect();
       try {
           waitLatch.await();
       } catch (InterruptedException e) {
           Logger.debugClient("Connection failed: " + e.getMessage());
       }
   }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Logger.debugClient("Connected!");
        sendCommand(new NetworkCommand(NetworkCommand.CommandType.REQUIRE_CONNECTION, new CommandArgument[]{new CommandArgument("auth", new Gson().toJson(ConfigurationManager.getConfig().getAuth()))}));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Logger.errClient("Socket closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        
        //Logger.debugClient("Received: " + message);
        commandHandler.onMessage(message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        Logger.debugClient("The below message was received as a ByteBuffer:");
        onMessage(StandardCharsets.ISO_8859_1.decode(message).toString());
    }

    @Override
    public void onError(Exception ex) {
        Logger.errClient("An error occurred in the server connection", ex);
    }

    /**
     * Used by ClientCommandHandler to tell that it has completed the connection handshake.
     */
    public void onReady(boolean isAuthenticated) {
        waitLatch.countDown();
        if(isAuthenticated) {
            Logger.debugClient("Ready!");
        } else {
            Logger.logClient("Could not authenticate with the server.");
        }
    }

    public void sendCommand(NetworkCommand command) {
        Logger.logClient("Command sent: " + command.toString());
        this.send(command.toString());
        //Logger.debugClient("Sent: " + command.toString());
    }
    
    public void setInGame(boolean b) {
        commandHandler.setInGame(b);
    }
    
    public ClientCommandHandler getCommandHandler() {
        return commandHandler;
    }
    

    public static NetworkManager getInstance() {
        if (instance == null) {
            String WS_STRING = maggdaforestdefense.MaggdaForestDefense.isDev() ? "ws://localhost:" + Server.WS_PORT + "/" + Server.WS_FILENAME : Server.WS_URL + Server.WS_FILENAME;
            //String WS_STRING = "ws://localhost:" + Server.WS_PORT + "/" + Server.WS_FILENAME;
            Logger.debugClient("WebSocket URI is " + WS_STRING);
            try {
                instance = new NetworkManager(new URI(WS_STRING));
            } catch (URISyntaxException e) {
                Logger.errClient("Couldn't instantiate a NetworkManager: " + WS_STRING, e);
            }
        }
        return instance;
    }
}
