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
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public abstract class Mob extends GameObject {

    protected double xPos, yPos, healthPoints, speed, maxHealth;
    protected double damage, damageTime, damageTimer = 0;
    protected int startXIndex, startYIndex, currentXIndex, currentYIndex, currentXMidIndex, currentYMidIndex;
    protected ServerGame serverGame;

    protected PathFinder pathFinder;
    protected Path path;
    protected MapCell targetCell, centerCell;

    protected HitBox hitBox;

    protected LinkedList<Damage> damageTaken;

    protected boolean dead = false;

    protected int towerVisionRange;

    protected boolean targetReached = false;

    protected Tower targetTower;

    protected Damage damageObject;
    protected Damage.NormalDamage basicDamage;

    public Mob(ServerGame game, GameObjectType objectType, double health, double speed, HitBox hitBox, int towerVision, double damage, double damageTime) {
        super(game.getNextId(), objectType);
        serverGame = game;
        this.hitBox = hitBox;
        this.speed = speed;
        this.towerVisionRange = towerVision;
        this.damage = damage;
        this.damageTime = damageTime;

        healthPoints = health;
        maxHealth = healthPoints;

        damageTaken = new LinkedList<>();

        targetCell = serverGame.getMap().getBase();

        // Damage
        damageObject = new Damage(this);
        basicDamage = new Damage.NormalDamage(damage);
        damageObject.addDamage(basicDamage);

    }

    protected void findStartPos() {
        int width = serverGame.getMap().getCells().length;
        int height = serverGame.getMap().getCells()[0].length;
        int random = (int) (Math.random() * 4);
        switch (random) {
            case 0:
                startXIndex = 0;
                startYIndex = (int) (Math.random() * height);
                break;
            case 1:
                startYIndex = 0;
                startXIndex = (int) (Math.random() * width);
                break;
            case 2:
                startXIndex = width - 1;
                startYIndex = (int) (Math.random() * height);
                break;
            case 3:
                startYIndex = height - 1;
                startXIndex = (int) (Math.random() * width);
                break;
        }
        xPos = startXIndex * MapCell.CELL_SIZE;
        yPos = startYIndex * MapCell.CELL_SIZE;
        hitBox.updatePos(xPos, yPos);
        initializePathFinder();
    }

    protected void updateMovement(double timeElapsed) {
        if(targetTower != null && (!targetTower.isAlive())) {
            targetTower = null;
        }
        if (!targetReached) {
            targetReached = path.walk(timeElapsed * speed);

            xPos = path.getCurrentX();
            yPos = path.getCurrentY();
            hitBox.updatePos(xPos, yPos);
        }
    }

    protected void updateIndexPosition() {
        currentXIndex = (int) (xPos / MapCell.CELL_SIZE);
        currentYIndex = (int) (yPos / MapCell.CELL_SIZE);
        if (((int) (0.5d + xPos / MapCell.CELL_SIZE) != currentXMidIndex) || ((int) (0.5d + yPos / MapCell.CELL_SIZE) != currentYMidIndex)) {
            currentXMidIndex = (int) (0.5d + xPos / MapCell.CELL_SIZE);
            currentYMidIndex = (int) (0.5d + yPos / MapCell.CELL_SIZE);
            searchForTowers();
        }

    }

    protected Tower searchForTowers() {

        serverGame.getGameObjects().forEach((String key, GameObject gameObject) -> {
            if (gameObject instanceof Tower) {
                Tower tower = ((Tower) gameObject);
                if (Math.abs(tower.getXIndex() - currentXIndex) < towerVisionRange && Math.abs(tower.getYIndex() - currentYIndex) < towerVisionRange) {
                    PathFinder finder = new PathFinder(serverGame.getMap().getCells()[currentXIndex][currentYIndex].getPathCell(), serverGame.getMap().getCells()[tower.getXIndex()][tower.getYIndex()].getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType);
                    Path newPath = finder.findPath();
                    if (newPath.getRestWay() < path.getRestWay()) {
                        path = newPath;
                        targetTower = tower;
                    }
                }
            }
        });

        return null;
    }

    protected void updateDamageTarget(double timeElapsed) {
        if (targetReached) {
            
            if (targetTower != null) {
                damageTimer += timeElapsed;
                if (damageTimer > damageTime) {
                    damageTimer = 0;
                    damageTarget();
                }
            } else if(serverGame.getMap().getCells()[currentXIndex][currentYIndex] == serverGame.getMap().getBase()) {
                
            }else {
                targetReached = false;
                pathToBase();
                searchForTowers();
            }
        }
    }

    protected void damageTarget() {
        if (targetTower != null) {
           targetTower.damage(damageObject);
        }
    }
    
    protected void pathToBase() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[currentXIndex][currentYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType);
        path = pathFinder.findPath();
    }

    protected void initializePathFinder() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[startXIndex][startYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType);
        path = pathFinder.findPath();
    }

    public double getDistanceToBase() {
        return path.getRestWay();
    }

    public boolean updateAlive() {
        if (!checkAlive()) {
            die();
            return false;
        } else {
            return true;
        }
    }

    public boolean checkAlive() {
        if (healthPoints <= 0) {
            return false;
        } else {
            return true;
        }
    }

    public void die() {
        if (!dead) {
            dead = true;
            serverGame.killMob(this);
        }
    }

    public void damage(Damage damage) {

        if (!damageTaken.contains(damage)) {
            damageTaken.add(damage);
            directDamage(damage);
        }

    }

    public void directDamage(Damage damage) {
        if (checkAlive()) {
            healthPoints -= damage.getTotalDamage();
            if (!checkAlive()) {
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
        return (int) (calculateStrength() / 50);
    }

    public double getHP() {
        return healthPoints;
    }

}
