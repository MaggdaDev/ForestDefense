/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.spawning;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;

/**
 *
 * @author David
 */
public class Spawnable {
    private GameObjectType gameObjectType;
    private SpawnDelay spawnDelay;
    public Spawnable(GameObjectType type, double delay) {
        gameObjectType = type;
        spawnDelay = new SpawnDelay(delay);
    }
    
    public boolean readyToSpawn(double timeElapsed) {
        return spawnDelay.update(timeElapsed);
    }

    public GameObjectType getType() {
        return gameObjectType;
    }
}
