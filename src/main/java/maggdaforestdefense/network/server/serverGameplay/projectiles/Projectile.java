/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.util.UpgradeHandler;

/**
 *
 * @author DavidPrivat
 */
public abstract class Projectile extends GameObject {

    protected HitBox hitBox;
    protected Tower owner;

    protected Vector<UpgradeHandler> onCollision;
    protected Vector<UpgradeHandler> onKill;

    protected Vector<Mob> mobsDamaged;

    public Projectile(int id, GameObjectType type, HitBox hitBox, Tower ownerTower) {
        super(id, type);
        this.hitBox = hitBox;
        this.owner = ownerTower;

        mobsDamaged = new Vector<Mob>();
        onCollision = new Vector<UpgradeHandler>();
        onKill = new Vector<UpgradeHandler>();
    }

    //get/set
    public HitBox getHitBox() {
        return hitBox;
    }

    public void collision(HashMap<String, Mob> mobs) {

        mobs.forEach((String s, Mob mob) -> {
            if (HitBox.intersects(hitBox, mob.getHitBox())) {
                dealDamage(mob);
            }
        });
    }

    public abstract void dealDamage(Mob target);

    public Tower getOwnerTower() {
        return owner;
    }

    // Perform upgrades
    protected void performUpgradesOnCollision() {
        for (int i = 0; i < onCollision.size(); i++) {
            UpgradeHandler u = (UpgradeHandler) onCollision.get(i);
            u.handleUpgrade();
        }
    }

    protected void performUpgradesOnKill() {
        for (int i = 0; i < onKill.size(); i++) {
            UpgradeHandler u = (UpgradeHandler) onKill.get(i);
            u.handleUpgrade();
        }
    }
    
    public void notifyKill(Mob target) {
        owner.notifyKill();
        performUpgradesOnKill();
    }

}
