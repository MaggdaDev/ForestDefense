/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

/**
 *
 * @author DavidPrivat
 */
public abstract class Tower extends GameObject{
    public Tower(ServerGame game) {
        super(game.getNextId());
    }
}
