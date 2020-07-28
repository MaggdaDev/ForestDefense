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
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
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

    protected double healthPoints;

    protected MapCell mapCell;
    
    protected boolean isAlive = true;

    // Upgrade events
    protected Vector<UpgradeHandler> onShoot, onKill, onUpdate;

    public Tower(ServerGame game, double xPos, double yPos, GameObjectType type, int prize, UpgradeSet upgrades, double health) {
        super(game.getNextId(), type);
        upgradeSet = upgrades;
        xIndex = (int) (xPos / MapCell.CELL_SIZE);
        yIndex = (int) (yPos / MapCell.CELL_SIZE);

        serverGame = game;
        mapCell = serverGame.getMap().getCells()[xIndex][yIndex];
        mapCell.setTower(this);

        this.prize = prize;
        this.healthPoints = health;
        this.upgrades = new Vector<>();
        this.onShoot = new Vector<UpgradeHandler>();
        this.onKill = new Vector<UpgradeHandler>();
        this.onUpdate = new Vector<UpgradeHandler>();

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
                return mob;
            }
        }

        return null;
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
        healthPoints -= damageObject.getTotalDamage();
    }
    
    public boolean checkAlive() {
        if(healthPoints < 0) {
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

    // UpradePerforms
    public void performUpgradesOnShoot() {                  // MUST BE IN EVERY SUB CLASS SHOOT METHOD
        for (int i = 0; i < onShoot.size(); i++) {
            UpgradeHandler u = onShoot.get(i);
            u.handleUpgrade();
        }
    }

    public void performUpgradesOnKill() {
        for (int i = 0; i < onKill.size(); i++) {
            UpgradeHandler u = onKill.get(i);
            u.handleUpgrade();
        }
    }

    public void performUpgradesOnUpdate() {                  // MUST BE IN EVERY SUB CLASS UPDATE METHOD
        for (int i = 0; i < onUpdate.size(); i++) {
            UpgradeHandler u = onUpdate.get(i);
            u.handleUpgrade();
        }
    }

    // UPgrade performs end
    abstract public void addUpgrade(Upgrade upgrade);

    public void notifyKill() {
        performUpgradesOnKill();
    }

}
