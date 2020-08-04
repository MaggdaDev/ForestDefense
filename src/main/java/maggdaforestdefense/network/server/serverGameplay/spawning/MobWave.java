/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.spawning;

import java.util.Vector;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;

/**
 *
 * @author David
 */
public class MobWave {

    private int mobAmount;
    private Vector<Spawnable> spawnableVector;
    private Spawnable currentSpawnable;
    private int currentIndex = 0;

    public MobWave(Vector<Spawnable> spawnables) {
        spawnableVector = spawnables;
        mobAmount = spawnableVector.size();

        currentSpawnable = spawnableVector.get(currentIndex);
    }

    public Spawnable update(double timeElapsed) {
        Spawnable retSpawn = null;
        if (!isFinished()) {
            if (currentSpawnable.readyToSpawn(timeElapsed)) {
                retSpawn = currentSpawnable;
                currentIndex++;
                if (!isFinished()) {
                    currentSpawnable = spawnableVector.get(currentIndex);
                }
            }
        }
        return retSpawn;
    }

    public boolean isFinished() {
        return currentIndex >= mobAmount;
    }

    public int getMobAmount() {
        return mobAmount;
    }
}
