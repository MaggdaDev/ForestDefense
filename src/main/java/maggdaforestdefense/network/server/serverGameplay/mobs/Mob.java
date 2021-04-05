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
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.Path;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathFinder;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer;
import maggdaforestdefense.network.server.serverGameplay.towers.Oak;
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
    
    protected boolean sentDeathToClient = false;

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
                double dist = Math.sqrt(Math.pow(tower.getXIndex() - currentXIndex, 2.0d) + Math.pow(tower.getYIndex() - currentYIndex, 2.0d));
                if (tower.shouldPrioritize(dist * MapCell.CELL_SIZE, movementType) || Math.abs(tower.getXIndex() - currentXIndex) < towerVisionRange && Math.abs(tower.getYIndex() - currentYIndex) < towerVisionRange) {
                    PathFinder finder = new PathFinder(serverGame.getMap().getCells()[currentXIndex][currentYIndex].getPathCell(), serverGame.getMap().getCells()[tower.getXIndex()][tower.getYIndex()].getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType, mapDistanceSet);
                    Path newPath = finder.findPath();
                    if(movementType == MovementType.FLY && tower instanceof Oak && ((Oak)tower).getUpgrades().contains(Upgrade.OAK_1_2)) {
                        newPath.setPriority(10);
                    }
                    if (newPath.getPriority() > path.getPriority() || newPath.getRestWay() < path.getRestWay()) {
                        path = newPath;
                        targetTower = tower;
                    }
                }
            }
        });

        return null;
    }
    
    public boolean wouldDie(Damage damage) {
        return directDamage(damage) >= healthPoints;
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
                damageTimer += timeElapsed;
                if (damageTimer > damageTime) {
                    damageTimer = 0;
                    damageBase();
                }
                
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
        if (sentDeathToClient) {
            die(true);
        }
        return checkAlive();
    }

    public boolean checkAlive() {
        if (healthPoints <= 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public Tower getTargetTower() {
        return targetTower;
    }
    
    public double getDamage() {
        return damage;
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
            double damageVal = directDamage(damage);
            return applyDamage(damageVal, damage);
        }
        return 0;

    }
    
    private double applyDamage(double damageVal, Damage damage) {
        if (checkAlive()) {
        double oldHealthPoints = healthPoints;
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

    public double directDamage(Damage damage) {
            double damageVal = damage.getTotalDamage(armor);
            if (effectSet.isActive(EffectSet.EffectType.SENSITIVE)) {        // EFFECT: SENSITIVE
                damageVal *= EffectSet.Effect.EFFECT_SENSITIVE_MULT;
            }
            return damageVal;
            
            
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
    
    public int calculateCoinValue() {
        int goldAdd = 0;
        if(effectSet.isActive(EffectSet.EffectType.GOLDED)) {
            goldAdd = (int)Lorbeer.WIEDERVWERTUNG_ADD;
        }
        return getCoinValue(gameObjectType) + goldAdd;
    }

    public static int getCoinValue(GameObjectType type) {
        switch(type) {
            case M_BLATTLAUS:
                return 20;
            case M_BORKENKAEFER:
                return 100;
            case M_HIRSCHKAEFER:
                return 500;
            case M_SCHWIMMKAEFER:
                return 20;
            case M_WANDERLAUFER:
                return 100;
            case M_WASSERLAEUFER:
                return 40;
            case M_MARIENKAEFER:
                return 80;
            default:
                throw new UnsupportedOperationException();
        }
    }

    public double getHP() {
        return healthPoints;
    }
    
    public double getMaxHP() {
        return maxHealth;
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
