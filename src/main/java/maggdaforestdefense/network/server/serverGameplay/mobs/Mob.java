/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import java.util.LinkedList;
import java.util.List;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.Path;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathFinder;

/**
 *
 * @author DavidPrivat
 */
public abstract class Mob extends GameObject {

    protected double xPos, yPos, healthPoints, speed, maxHealth;
    protected int startXIndex, startYIndex;
    protected ServerGame serverGame;
    
    protected PathFinder pathFinder;
    protected Path path;
    
    protected HitBox hitBox;
    
    protected LinkedList<Damage> damageTaken;
    
    protected boolean dead = false;
    
    
    public Mob(ServerGame game, GameObjectType objectType, double health, double speed, HitBox hitBox) {
        super(game.getNextId(), objectType);
        serverGame = game;
        this.hitBox = hitBox;
        this.speed = speed;

        healthPoints = health;
        maxHealth = healthPoints;
        
        damageTaken = new LinkedList<>();
    }


    protected void findStartPos() {
        int width = serverGame.getMap().getCells().length;
        int height = serverGame.getMap().getCells()[0].length;
        int random = (int)(Math.random()*4);
        switch(random) {
            case 0:
                startXIndex = 0;
                startYIndex = (int)(Math.random() * height);
                break;
            case 1:
                startYIndex = 0;
                startXIndex =(int) (Math.random() * width);
                break;
            case 2:
                startXIndex = width-1;
                startYIndex = (int) (Math.random() * height);
                break;
            case 3:
                startYIndex = height-1;
                startXIndex = (int) (Math.random() * width);
                break;
        }
        xPos = startXIndex * MapCell.CELL_SIZE;
        yPos = startYIndex * MapCell.CELL_SIZE;
        hitBox.updatePos(xPos, yPos);
        initializePathFinder();
    }
    
    protected void initializePathFinder() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[startXIndex][startYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType);
        path = pathFinder.findPath();
    }
    
    public double getDistanceToBase() {
        return path.getRestWay();
    }
    
    public boolean updateAlive() {
        if(!checkAlive()) {
            die();
            return false;
        } else {
            return true;
        }
        }
    
    public boolean checkAlive() {
        if(healthPoints <= 0) {
            return false;
        } else {
            return true;
        }
    }
    

    
    public void die() {
        if(!dead) {
            dead = true;
        serverGame.killMob(this);
        }
    }
    
    
    public void damage(Damage damage) {

        
        if(!damageTaken.contains(damage)) {
            damageTaken.add(damage);
        switch(damage.getType()) {
            case DIRECT:
                directDamage((Damage.DirectDamage)damage);
                break;            
            default:
                throw new UnsupportedOperationException();
                
                
        }
        }
        
   
    
    }
    
    public void directDamage(Damage.DirectDamage damage) {
        if(checkAlive()) {
        healthPoints -= damage.getDamage();
        if(!checkAlive()) {
            damage.getOwnerProjectile().notifyKill(this);
        }
        }
    }
    // Get/Set
    
    public double getXPos() {
        return xPos;
    }
    
    public double getYPos() {
        return yPos;
    }
    
    public HitBox getHitBox() {
        return hitBox;
    }
    
    public double calculateStrength() {
        return maxHealth * speed;
    }
    
    public double getCoinValue() {
        return (int)(calculateStrength() / 50);
    }
    
    public double getHP() {
        return healthPoints;
    }
    
    
    

    
    
}
