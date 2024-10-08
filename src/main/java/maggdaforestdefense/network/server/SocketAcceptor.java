/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.storage.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SocketAcceptor extends WebSocketServer {

    public SocketAcceptor(InetSocketAddress address) {
        super(address);
        Logger.debugServer("SocketAcceptor created (adress: " + address.toString() + ")");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Logger.debugServer("New connection to " + conn.getRemoteSocketAddress());
        try {
        Server.getInstance().addNewSocket(conn);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Logger.debugServer("Closed connection " + conn.getRemoteSocketAddress() + "|" + conn.getAttachment() + " with exit code " + code + " additional info: " + reason);
        Server.getInstance().handleDisconnect(conn, code, reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        //Logger.debugServer("received message from "	+ conn.getRemoteSocketAddress() + "|" + conn.getAttachment() + ": " + message);
        Server.getInstance().handleMessage(conn, message);
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        Logger.debugServer("The below message was received as a ByteBuffer:");
        onMessage(conn, StandardCharsets.ISO_8859_1.decode(message).toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Logger.errServer("An Error occured in connection " + conn.getRemoteSocketAddress() + "|" + conn.getAttachment(), ex);
        Server.getInstance().handleException(conn, ex);
    }

    @Override
    public void onStart() {
        Logger.logServer("Started SocketServer.");
    }


}