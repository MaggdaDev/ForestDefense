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
import java.util.concurrent.ConcurrentSkipListSet;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

/**
 *
 * @author DavidPrivat
 */
public abstract class Projectile extends GameObject{
    protected HitBox hitBox;
    public Projectile(int id, GameObjectType type, HitBox hitBox) {
        super(id, type);
        this.hitBox = hitBox;
    }
    
    
    
    //get/set
    public HitBox getHitBox() {
        return hitBox;
    }
    
    public void collision(HashMap<String, Mob> mobs) {

        
        mobs.forEach((String s, Mob mob)->{
            if(HitBox.intersects(hitBox, mob.getHitBox())) {
                dealDamage(mob);
            }
        });
    }
    
    public abstract void dealDamage(Mob target);
}
