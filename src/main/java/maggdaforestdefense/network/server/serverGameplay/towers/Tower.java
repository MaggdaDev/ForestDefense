/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.GameAnimation;
import maggdaforestdefense.util.UpgradeHandler;

/**
 *
 * @author DavidPrivat
 */
public abstract class Tower extends GameObject {

    protected int xIndex, yIndex, prize;

    protected ServerGame serverGame;

    protected Vector<Upgrade> upgrades;

    protected UpgradeSet upgradeSet;

    protected double healthPoints, regenerationPerSecond, maxHealth;

    protected double lifeSteal = 0;

    protected int range;

    protected MapCell mapCell;

    protected boolean isAlive = true;

    protected CanAttackSet canAttackSet;
    
    protected double growingTime;
    
    protected boolean isEssenceFed = false;

    // Upgrade events
    protected Vector<UpgradeHandler> onShoot, onKill, onUpdate, onTowerChanges, onNewRound;
    
    protected boolean isMature = false;
    
    protected GameAnimation growingAnimation;
    

    public Tower(ServerGame game, double xPos, double yPos, GameObjectType type, int prize, UpgradeSet upgrades, double health, double regen, int range, CanAttackSet attackSet, double growTime) {
        super(game.getNextId(), type);
        upgradeSet = upgrades;
        xIndex = (int) (xPos / MapCell.CELL_SIZE);
        yIndex = (int) (yPos / MapCell.CELL_SIZE);

        serverGame = game;
        mapCell = serverGame.getMap().getCells()[xIndex][yIndex];
        mapCell.setTower(this);

        this.range = range;
        this.prize = prize;
        this.healthPoints = health;
        this.maxHealth = healthPoints;
        this.regenerationPerSecond = regen;
        this.upgrades = new Vector<>();
        this.onShoot = new Vector<UpgradeHandler>();
        this.onKill = new Vector<UpgradeHandler>();
        this.onUpdate = new Vector<UpgradeHandler>();
        this.onTowerChanges = new Vector<UpgradeHandler>();
        this.onNewRound = new Vector<UpgradeHandler>();
        this.canAttackSet = attackSet;
        this.growingTime = growTime;
        
        // Animation
        GameImage lastImage;
        switch(gameObjectType) {
            case T_SPRUCE:
                lastImage = GameImage.TOWER_SPRUCE_1;
                break;
            case T_MAPLE:
                lastImage = GameImage.TOWER_MAPLE_1;
                break;
            default:
                throw new UnsupportedOperationException();
        }
        growingAnimation = new GameAnimation(growTime, new GameImage[]{GameImage.TOWERGROWING_ANIMATION_1, GameImage.TOWERGROWING_ANIMATION_2, GameImage.TOWERGROWING_ANIMATION_3, GameImage.TOWERGROWING_ANIMATION_4, GameImage.TOWERGROWING_ANIMATION_5, GameImage.TOWERGROWING_ANIMATION_6, GameImage.TOWERGROWING_ANIMATION_7, GameImage.TOWERGROWING_ANIMATION_8, lastImage});

    }
    
    protected GameImage updateGrowing(double timeElapsed) {
        return growingAnimation.update(timeElapsed);
    }

    protected Mob findTarget(int range) {

        LinkedList<Mob> mobs = new LinkedList(serverGame.getMobs().values());
        Collections.sort(mobs, new Comparator<Mob>() {
            @Override
            public int compare(Mob mob1, Mob mob2) {
                double diff = mob1.getDistanceToBase() - mob2.getDistanceToBase();
                if (diff == 0) {
                    return 0;
                } else if (diff < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        for (Mob mob : mobs) {
            if (isInRange(mob, range)) {
                if (mob.getMovementType() == Mob.MovementType.DIG && canAttackSet.canAttackDigging()) {
                    return mob;
                } else if (mob.getMovementType() == Mob.MovementType.WALK && canAttackSet.canAttackWalking()) {
                    return mob;
                } else if (mob.getMovementType() == Mob.MovementType.FLY && canAttackSet.canAttackFlying()) {
                    return mob;
                }
            }
        }

        return null;
    }

    protected void updateRegen(double timeElapsed) {
        if (timeElapsed * regenerationPerSecond + healthPoints > maxHealth) {
            healthPoints = maxHealth;
        } else {
            healthPoints += timeElapsed * regenerationPerSecond;
        }
    }

    protected boolean isInRange(Mob mob, int range) {
        double deltaX = Math.abs(getCenterX() - mob.getXPos());
        double deltaY = Math.abs(getCenterY() - mob.getYPos());
        double pixelRange = (range + 0.5) * MapCell.CELL_SIZE;
        if (deltaX <= pixelRange && deltaY <= pixelRange) {
            return true;
        }
        return false;
    }

    public void damage(Damage damageObject) {
        healthPoints -= damageObject.getTotalDamage(0);
    }

    public boolean checkAlive() {
        if (healthPoints < 0) {
            die();
            return false;
        } else {
            return true;
        }
    }

    public void die() {
        serverGame.killTower(this);
        isAlive = false;
    }
    
    public void handleAfterWave() {
        if(isMature) {
        isEssenceFed = false;
        serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.TOWER_NEED_ESSENCE, new CommandArgument[]{new CommandArgument("id", id)}));
        
        }
    }
    
    public void supplyEssence() {
        isEssenceFed = true;
    }
    
      public void checkEssenceFed() {
        if(isMature && (!isEssenceFed)) {
            serverGame.killTower(this);
        }
    }

    //Get/Set
    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public double getCenterX() {
        return (xIndex + 0.5d) * MapCell.CELL_SIZE;
    }

    public double getCenterY() {
        return (yIndex + 0.5d) * MapCell.CELL_SIZE;
    }

    public int getPrize() {
        return prize;
    }

    public UpgradeSet getUpgradeSet() {
        return upgradeSet;
    }

    public Vector<Upgrade> getUpgrades() {
        return upgrades;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public double getHealthPoints() {
        return healthPoints;
    }

    public double getMaxHealthPoints() {
        return maxHealth;
    }
    
    public double getGrowingTime() {
        return growingTime;
    }

    // UpradePerforms
    public void performUpgradesOnShoot() {                  // MUST BE IN EVERY SUB CLASS SHOOT METHOD
        for (int i = 0; i < onShoot.size(); i++) {
            UpgradeHandler u = onShoot.get(i);
            u.handleUpgrade(null);
        }
    }

    public void performUpgradesOnKill(Mob killed) {
        for (int i = 0; i < onKill.size(); i++) {
            UpgradeHandler u = onKill.get(i);
            u.handleUpgrade(killed);
        }
    }

    public void performUpgradesOnUpdate() {                  // MUST BE IN EVERY SUB CLASS UPDATE METHOD
        for (int i = 0; i < onUpdate.size(); i++) {
            UpgradeHandler u = onUpdate.get(i);
            u.handleUpgrade(null);
        }
    }

    public void performUpgradesOnTowerChanges() {
        for (int i = 0; i < onTowerChanges.size(); i++) {
            UpgradeHandler u = onTowerChanges.get(i);
            u.handleUpgrade(null);
        }
    }
    
     public void performUpgradesOnNewRound() {
        for (int i = 0; i < onNewRound.size(); i++) {
            UpgradeHandler u = onNewRound.get(i);
            u.handleUpgrade(null);
        }
    }

    // UPgrade performs end
    abstract public void addUpgrade(Upgrade upgrade);

    public void notifyNextRound() {
        performUpgradesOnNewRound();
    }
    
    public void notifyKill(Mob killed) {
        performUpgradesOnKill(killed);
    }

    public void notifyTowerChanges() {
        performUpgradesOnTowerChanges();
    }

    public int getRange() {
        return range;
    }

    public void notifyDamage(double damageVal) {
        Logger.logServer("DAMAGE: " + damageVal);
        double healthAdd = damageVal * lifeSteal;
        if (healthPoints + healthAdd > maxHealth) {
            healthPoints = maxHealth;
        } else {
            healthPoints += healthAdd;
        }
    }

  

    

    public static class CanAttackSet {

        private boolean canAttackDigging, canAttackWalking, canAttackFlying;

        public CanAttackSet(boolean attackDigging, boolean attackWalking, boolean attackFlying) {
            canAttackDigging = attackDigging;
            canAttackWalking = attackWalking;
            canAttackFlying = attackFlying;
        }

        public boolean canAttackDigging() {
            return canAttackDigging;
        }

        public boolean canAttackWalking() {
            return canAttackWalking;
        }

        public boolean canAttackFlying() {
            return canAttackFlying;
        }

        public void setCanAttackDigging(boolean b) {
            canAttackDigging = b;
        }

        public void setCanAttackWalking(boolean b) {
            canAttackWalking = b;
        }

        public void setCanAttackFlying(boolean b) {
            canAttackFlying = b;
        }
    }

}
