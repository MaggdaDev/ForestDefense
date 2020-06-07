/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public abstract class Tower extends GameObject{
    protected int xIndex, yIndex;
    
    public Tower(ServerGame game, double xPos, double yPos, GameObjectType type) {
        super(game.getNextId(), type);
        xIndex = (int)(xPos/MapCell.CELL_SIZE);
        yIndex = (int)(yPos/MapCell.CELL_SIZE);
    }
    
    public int getXIndex() {
        return xIndex;
    }
    
    public int getYIndex() {
        return yIndex;
    }
}
