/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author David
 */
public class GameAnimation {

    private double animationDuration;
    private double runTime = 0;
    private GameImage[] gameImages;


    public GameAnimation(double duration, GameImage[] images) {
        gameImages = images;
        animationDuration = duration;
    }

    public GameImage update(double timeElapsed) {

        runTime += timeElapsed;
        if (runTime > animationDuration) {
            runTime = animationDuration;
        }
        int currentIndex = (int)((runTime/animationDuration) * (gameImages.length-1));
        return gameImages[currentIndex];
    }
    
    public boolean isFinished() {
        return animationDuration == runTime;
    }

    public double getTimeLeft() {
        return animationDuration - runTime;
    }
}
