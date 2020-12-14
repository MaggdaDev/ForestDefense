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
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;
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
    protected double damage, damageTime, damageTimer = 0, armor;
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

    protected MapDistanceSet mapDistanceSet;

    protected MovementType movementType;

    protected EffectSet effectSet;

    public Mob(ServerGame game, GameObjectType objectType, double health, double speed, HitBox hitBox, int towerVision, double damage, double damageTime, MapDistanceSet distanceSet, double armor, MovementType movement) {
        super(game.getNextId(), objectType);
        serverGame = game;
        this.hitBox = hitBox;
        this.mapDistanceSet = distanceSet;
        this.speed = speed;
        this.towerVisionRange = towerVision;
        this.damage = damage;
        this.damageTime = damageTime;
        this.armor = armor;
        this.movementType = movement;
        this.effectSet = new EffectSet();

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
    
    protected void updateEffects(double timeElapsed) {
        effectSet.update(timeElapsed);
    }

    protected void updateMovement(double timeElapsed) {
        if (targetTower != null && (!targetTower.isAlive())) {
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
                    PathFinder finder = new PathFinder(serverGame.getMap().getCells()[currentXIndex][currentYIndex].getPathCell(), serverGame.getMap().getCells()[tower.getXIndex()][tower.getYIndex()].getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType, mapDistanceSet);
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
            } else if (serverGame.getMap().getCells()[currentXIndex][currentYIndex] == serverGame.getMap().getBase()) {
                damageBase();
            } else {
                targetReached = false;
                pathToBase();
                searchForTowers();
            }
        }
    }

    protected void damageBase() {
        serverGame.damageBase(this);
    }

    protected void damageTarget() {
        if (targetTower != null) {
            targetTower.damage(damageObject);
        }
    }

    protected void pathToBase() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[currentXIndex][currentYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType, mapDistanceSet);
        path = pathFinder.findPath();
    }

    protected void initializePathFinder() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[startXIndex][startYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType, mapDistanceSet);
        path = pathFinder.findPath();
    }

    public double getDistanceToBase() {
        return path.getRestWay();
    }

    public boolean updateAlive() {
        if (!checkAlive()) {
            die(true);
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

    public void die(boolean getGold) {
        if (!dead) {
            dead = true;
            serverGame.killMob(this, getGold);
        }
    }

    public double damage(Damage damage) {

        if (!damageTaken.contains(damage)) {
            damageTaken.add(damage);
            return directDamage(damage);
        }
        return 0;

    }

    public double directDamage(Damage damage) {
        double oldHealthPoints = healthPoints;
        if (checkAlive()) {
            double damageVal = damage.getTotalDamage(armor);
            if (effectSet.isActive(EffectSet.EffectType.SENSITIVE)) {        // EFFECT: SENSITIVE
                damageVal *= EffectSet.Effect.EFFECT_SENSITIVE_MULT;
            }
            healthPoints -= damageVal;
            if (!checkAlive()) {
                if(damage.getOwnerProjectile() != null) {
                damage.getOwnerProjectile().notifyKill(this);
                } else if(damage.getOwnerTower() != null) {
                    damage.getOwnerTower().notifyKill(this);
                }
            }
            return oldHealthPoints - healthPoints;
        }
        return 0;
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

    public void destroyArmor(double arm) {
        armor -= arm;
        if (armor < 0) {
            armor = 0;
        }
    }

    public double getArmor() {
        return armor;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public double getSpeed() {
        return speed;
    }

    public EffectSet getEffectSet() {
        return effectSet;
    }

    public static enum MovementType {
        DIG,
        WALK,
        FLY;
    }

}
