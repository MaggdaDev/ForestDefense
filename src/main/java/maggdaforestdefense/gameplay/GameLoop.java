/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.AnimationTimer;
import maggdaforestdefense.network.client.NetworkManager;

/**
 *
 * @author David
 */
public class GameLoop extends AnimationTimer {

    private boolean gameRunning = true;

    public GameLoop() {

    }

    @Override
    public void handle(long now) {
        if (gameRunning) {
            NetworkManager.getInstance().update();
        } 
    }
    
    public void setGameRunning(boolean b) {
        gameRunning = b;
    }

}
