/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.Logger;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import maggdaforestdefense.network.NetworkCommand;

/**
 *
 * @author David
 */
public class Server {

    public final static int PORT = 27757;
    public final static int WS_PORT = 27756;
    public final static String WS_FILENAME = "api/gamesocket/";
    public final static String WS_HOST = "forestdefense.minortom.net";
    public final static String WS_HOST2 = "0.0.0.0";
    //public final static String WS_URL = "wss://" + WS_HOST + "/";
    public final static String WS_URL = "ws://" + WS_HOST + ":" + WS_PORT + "/";

    private ObservableMap<Integer, Player> playerList;
    private SocketAcceptor acceptor;

    private GameHandler gameHandler;
    private static Server instance;

    public Server() throws IOException {
        instance = this;
        playerList = FXCollections.observableHashMap();

        acceptor = new SocketAcceptor(new InetSocketAddress(WS_HOST2, Server.WS_PORT));

        gameHandler = new GameHandler();

        acceptor.start();

    }

    public void addNewSocket(WebSocket conn) {
        ServerSocketHandler addSocketHandler;
        addSocketHandler = new ServerSocketHandler(conn);
        Player player = new Player(addSocketHandler, playerList.size());
        playerList.put(player.getID(), player);
        conn.setAttachment(player.getID());
    }

    public void handleMessage(WebSocket conn, String message) {
        if(conn.getAttachment()!=null) {
            if(playerList.get(conn.getAttachment())!=null) {
                playerList.get(conn.getAttachment()).handleMessage(conn, message);
            } else {
                Logger.logServer("Received a message from an unknown player! " + message);
            }
        } else {
            Logger.logServer("Received a message from an unknown client! " + message);
        }
    }

    public void handleException(WebSocket conn, Exception e) {
        if(conn.getAttachment()!=null) {
            if(playerList.get(conn.getAttachment())!=null) {
                playerList.get(conn.getAttachment()).handleException(conn, e);
            } else {
                Logger.logServer("Received an exception from an unknown player! " + e.getMessage());
            }
        } else {
            Logger.logServer("Received an exception from an unknown client! " + e.getMessage());
        }
    }

    public void handleDisconnect(WebSocket conn, int code, String reason) {
        if(conn.getAttachment()!=null) {
            if(playerList.get(conn.getAttachment())!=null) {
                playerList.get(conn.getAttachment()).handleDisconnect(conn, code, reason);
                playerList.remove(conn.getAttachment());
            } else {
                Logger.logServer("Received a disconnect from an unknown player! ");
            }
        } else {
            Logger.logServer("Received a disconnect from an unknown client! ");
        }
    }

    public void addGame(ServerGame game) {
        gameHandler.addGame(game);
    }
    
    public static Server getInstance() {
        return instance;
    }

    public NetworkCommand getGamesAsCommand() {
        return gameHandler.getGamesAsCommand();
    }
}
