/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.spawning;

/**
 *
 * @author David
 */
public class SpawnDelay {

    private double duration, runTime = 0;

    public SpawnDelay(double delay) {
        duration = delay;
    }

    public boolean update(double timeElapsed) {       // returns true when delay ending
        runTime += timeElapsed;
        if(runTime >= duration) {
            return true;
        }
        return false;
    }

}
