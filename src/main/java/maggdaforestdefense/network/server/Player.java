/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class Player {

    private boolean loopRunning = true;
    private ServerSocketHandler commandHandler;

    public Player(ServerSocketHandler handler) {
        commandHandler = handler;
        new Thread(commandHandler).start();
    }
    
    public void run() {
   
    }
}
