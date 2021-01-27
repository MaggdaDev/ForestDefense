/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.util.TimeUpdatable;

/**
 *
 * @author David
 */
public class GameLoop extends AnimationTimer {

    private boolean gameRunning = true;
    
    private boolean initialized = false;
    private double oldTimeSec, nowSec;
    private double timeElapsed = 0;
    
    private static ConcurrentLinkedQueue<TimeUpdatable> timeUpdatables;

    public GameLoop() {
       timeUpdatables = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void handle(long now) {
        
        nowSec = now / (1000d * 1000d * 1000d);
        
        if(!initialized) {
            initialized = true;
            oldTimeSec = now;
        }
        timeElapsed = nowSec - oldTimeSec;
        oldTimeSec = nowSec;
        
        if (gameRunning) {
            NetworkManager.getInstance().update();
        } 
        
        timeUpdatables.forEach((TimeUpdatable u)->{
            u.update(timeElapsed);
        });
        
        
    }
    
    public static void addTimeUpdatable(TimeUpdatable u) {
        timeUpdatables.add(u);
    }
    
    public static void removeTimeUpdatable(TimeUpdatable u) {
        if(timeUpdatables.contains(u)) {
            timeUpdatables.remove(u);
        }
    }
    
    public void setGameRunning(boolean b) {
        gameRunning = b;
    }

}
