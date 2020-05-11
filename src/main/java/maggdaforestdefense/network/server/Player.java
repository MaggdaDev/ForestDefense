/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.storage.Logger;

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

    public void testUpdateCircle() {
        testCounter += 0.01;
        double x = Math.pow(Math.sin(testCounter), 3.0d)*200+500;
        double y = Math.pow(Math.sin(testCounter), 5.0d)*400+500;
        commandHandler.sendCommand(new NetworkCommand(NetworkCommand.CommandType.UPDATE_TEST, new CommandArgument[]{new CommandArgument("x", String.valueOf(x)), new CommandArgument("y", String.valueOf(y))}));
    }

}
