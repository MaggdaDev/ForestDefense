/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class FPSLimiter {

    public static final int MAX_FPS = 60;
    public static final double TIME_BETWEEN_FRAMES = 1000.0d / MAX_FPS;
    public static final boolean FPS_TEST_MODE = true;
    private int counter = 0;
    private double timeElapsedTotal;

    private double oldTime, newTime, sleep, behind = 0;

    public FPSLimiter() {
        oldTime = 0;
        newTime = 0;
    }

    public void startOfIteration() {
        if (System.currentTimeMillis() - oldTime < 1000) {
            counter++;
            timeElapsedTotal += System.currentTimeMillis() - oldTime;
        }
        printFPS();
        oldTime = System.currentTimeMillis();
    }

    public void endOfIteration() {
        newTime = System.currentTimeMillis();
    }

    public void doSleep() {
        if(newTime - oldTime > 1000) {
            oldTime = newTime;
        }
        try {
            sleep = TIME_BETWEEN_FRAMES + behind - (newTime - oldTime);
            behind = 0;
            if (sleep > 0) {
                Thread.sleep((long) sleep);
            } else {
                behind = sleep;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printFPS() {
        if (FPS_TEST_MODE) {
            if (counter % ((int)(MAX_FPS*5)) == 0) {
                Logger.logServer("FPS:   " + (counter / (timeElapsedTotal / 1000)));
            }
        }
    }
}
