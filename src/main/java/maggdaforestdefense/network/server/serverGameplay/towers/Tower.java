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
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

/**
 *
 * @author DavidPrivat
 */
public abstract class Tower extends GameObject{
    protected int xIndex, yIndex;
    
    protected ServerGame serverGame;
    
  
    
    public Tower(ServerGame game, double xPos, double yPos, GameObjectType type) {
        super(game.getNextId(), type);
        xIndex = (int)(xPos/MapCell.CELL_SIZE);
        yIndex = (int)(yPos/MapCell.CELL_SIZE);
        serverGame = game;
    }
    
    
    protected Mob findTarget(int range) {

        for(Mob mob: serverGame.getSortedMobs()){
            if(isInRange(mob, range)) {
                return mob;
            }
        }
        
        return null;
    }
    
    protected boolean isInRange(Mob mob, int range) {
        double deltaX = Math.abs(getCenterX()-mob.getXPos());
        double deltaY = Math.abs(getCenterY()-mob.getYPos());
        double pixelRange = (range+0.5)*MapCell.CELL_SIZE;
        if(deltaX <= pixelRange && deltaY <= pixelRange) {
            return true;
        }
        return false;
    }
    
    
    //Get/Set
    
    public int getXIndex() {
        return xIndex;
    }
    
    public int getYIndex() {
        return yIndex;
    }
    
    public double getCenterX() {
        return (xIndex+0.5d) * MapCell.CELL_SIZE;
    }
    
    public double getCenterY() {
        return (yIndex+0.5d) * MapCell.CELL_SIZE;
    } 
}
