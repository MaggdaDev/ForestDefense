/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import org.java_websocket.WebSocket;

/**
 *
 * @author David
 */
public class Player {


    private ServerSocketHandler commandHandler;
    private boolean readyForNextRound = false;

    private int id;

    public Player(ServerSocketHandler handler, int id) {
        this.id = id;
        commandHandler = handler;
        handler.setOwner(this);
        new Thread(commandHandler).start();
    }

    public void sendCommand(NetworkCommand command) {
        commandHandler.sendCommand(command);
    }
    
    public void setReadyForNextRound(boolean b) {
        readyForNextRound = b;
    }
    
    public boolean isReadyForNextRound() {
        return readyForNextRound;
    }

    public void handleMessage(WebSocket conn, String message) {
        commandHandler.handleMessage(conn, message);
    }

    public void handleException(WebSocket conn, Exception e) {
        commandHandler.handleException(conn, e, true);
    }

    public void handleDisconnect(WebSocket conn, int code, String reason) {
        commandHandler.stop();
    }

    public int getID() {
        return id;
    }
}
