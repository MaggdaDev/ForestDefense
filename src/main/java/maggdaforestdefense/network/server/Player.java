/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.auth.AuthUser;
import maggdaforestdefense.network.NetworkCommand;
import org.java_websocket.WebSocket;

/**
 *
 * @author David
 */
public class Player {


    private ServerSocketHandler commandHandler;
    private boolean readyForNextRound = false;

    private int serverId;
    private AuthUser authUser;

    public Player(ServerSocketHandler handler, int id) {
        this.serverId = id;
        commandHandler = handler;
        handler.setOwner(this);
        new Thread(commandHandler).start();
    }

    public void sendCommand(NetworkCommand command) {
        commandHandler.sendCommand(command);
    }
    
    public void sendCommandUDP(NetworkCommand command) {
        commandHandler.sendCommandUPD(command);
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
        return serverId;
    }
    public boolean isAnonymous() {
        return !authUser.isSignedIn();
    }
    public String getUserName() {
        return authUser.getUsername();
    }
    public String getUserId() {
        return authUser.getId();
    }
    public Player setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
        return this;
    }
}
