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

    private boolean loopRunning = true;
    private ServerSocketHandler commandHandler;
    
    private double testCounter = 0;

    public Player(ServerSocketHandler handler) {
        commandHandler = handler;
        handler.setOwner(this);
        new Thread(commandHandler).start();
    }

    public void sendCommand(NetworkCommand command) {
        commandHandler.sendCommand(command);
    }

}
