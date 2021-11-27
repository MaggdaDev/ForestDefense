/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentSkipListSet;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.mobs.Caterpillar;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.UpgradeHandler;

/**
 *
 * @author DavidPrivat
 */
public abstract class Projectile extends GameObject {

    protected HitBox hitBox;
    protected Tower owner;

    protected Vector<UpgradeHandler> beforeCollision, afterCollision;
    protected Vector<UpgradeHandler> onKill;

    protected Vector<Mob> mobsDamaged;

    protected Tower.CanAttackSet canAttackSet;

    public Projectile(int id, GameObjectType type, HitBox hitBox, Tower ownerTower, Tower.CanAttackSet attackSet) {
        super(id, type);
        this.hitBox = hitBox;
        this.owner = ownerTower;
        this.canAttackSet = attackSet;
        mobsDamaged = new Vector<Mob>();
        beforeCollision = new Vector<UpgradeHandler>();
        afterCollision = new Vector<UpgradeHandler>();
        onKill = new Vector<UpgradeHandler>();
    }

    //get/set
    public HitBox getHitBox() {
        return hitBox;
    }

    public void collision(ArrayList<Mob> mobs) {

        mobs.forEach((Mob mob) -> {
            
            if (HitBox.intersects(hitBox, mob.getHitBox())) {
                switch (mob.getMovementType()) {
                    case DIG:
                        if (canAttackSet.canAttackDigging()) {
                            dealDamage(mob);
                        }
                        break;
                    case FLY:
                        if (canAttackSet.canAttackFlying()) {
                            dealDamage(mob);
                        }
                        break;
                    case WALK:
                        if (canAttackSet.canAttackWalking()) {
                            dealDamage(mob);
                        }
                        break;
                }
            }
            if (mob.getGameObjectType().equals(GameObjectType.M_BOSS_CATERPILLAR)) {
                if (canAttackSet.canAttackWalking()) {
                    ((Caterpillar) mob).checkSegmentCollision(this);
                }
            }
        });
    }

    public abstract void dealDamage(Mob target);

    public Tower getOwnerTower() {
        return owner;
    }

    // Perform upgrades
    protected void performUpgradesBeforeCollision() {
        for (int i = 0; i < beforeCollision.size(); i++) {
            UpgradeHandler u = (UpgradeHandler) beforeCollision.get(i);
            u.handleUpgrade(null);
        }
    }

    protected void performUpgradesAfterCollision() {
        for (int i = 0; i < afterCollision.size(); i++) {
            UpgradeHandler u = (UpgradeHandler) afterCollision.get(i);
            u.handleUpgrade(null);
        }
    }

    protected void performUpgradesOnKill(Mob killed) {
        for (int i = 0; i < onKill.size(); i++) {
            UpgradeHandler u = (UpgradeHandler) onKill.get(i);
            u.handleUpgrade(killed);
        }
    }

    public void notifyKill(Mob target) {
        owner.notifyKill(target);
        performUpgradesOnKill(target);
    }

    protected void notifyOwnerDamage(double damage) {
        owner.notifyDamage(damage);
    }

}
