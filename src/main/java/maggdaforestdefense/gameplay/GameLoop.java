/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.AnimationTimer;
import javafx.scene.shape.Circle;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class GameLoop extends AnimationTimer{

    public GameLoop() {

    }

    @Override
    public void handle(long now) {
        NetworkManager.getInstance().update();
        
    }
    
    public void updateCircle(double x, double y) {
        
    }
    
}
