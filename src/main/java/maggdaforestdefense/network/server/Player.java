/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;

/**
 *
 * @author David
 */
public class Player {


    private ServerSocketHandler commandHandler;
    private boolean readyForNextRound = false;

    public Player(ServerSocketHandler handler) {
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

}
