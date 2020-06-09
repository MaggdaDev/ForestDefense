/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public abstract class ConstantFlightProjectile extends Projectile{
    
    protected double startX, startY, xPos, yPos, xSpd, ySpd, totalSpeed, distanceTravelled = 0;
    protected int towerRange;
    protected Mob targetMob;
    protected ServerGame serverGame;
    public ConstantFlightProjectile(int id, GameObjectType type, int range, Mob target, double x, double y, double totSpd, ServerGame game, HitBox hitBox) {
        super(id, type, hitBox);
        targetMob = target;
        towerRange = range;
        totalSpeed = totSpd;
        xPos = x;
        yPos = y;
        startX = x;
        startY = y;
        serverGame = game;
        
    }
    
    protected void calculateSpeed(Mob target) {
        double deltaXMob = target.getXPos() - xPos;
        double deltaYMob = target.getYPos() - yPos;
        double deltaAbs = GameMaths.getAbs(deltaXMob, deltaYMob);
        xSpd = totalSpeed * deltaXMob / deltaAbs;
        ySpd = totalSpeed * deltaYMob / deltaAbs;

    }
    
    protected boolean updateFlight(double timeElapsed) {
        xPos += xSpd * timeElapsed;
        yPos += ySpd * timeElapsed;
        distanceTravelled += totalSpeed * timeElapsed;
        
        hitBox.updatePos(xPos, yPos);
        
        if(!checkAlive()) {
            serverGame.removeProjectile(this);
            return false;
        }
        
        return true;
    }
    
    protected boolean checkAlive() {
        double towerRangeRectHalfSite = (towerRange + 0.5) * MapCell.CELL_SIZE;
        if(Math.abs(startX - xPos) > towerRangeRectHalfSite || Math.abs(startY - yPos) > towerRangeRectHalfSite) {
            return false;
        }
        return true;
    }
}
