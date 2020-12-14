/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.Arrays;
import java.util.Vector;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class Damage {

    private Projectile ownerProjectile;
    private Mob ownerMob;
    private Tower ownerTower;
    private Vector<DamageSubclass> damageList;
    private Vector<DamageMultiplier> damageMultiplierList;

    public Damage(Projectile ownerProjectile) {
        this.ownerProjectile = ownerProjectile;
        damageList = new Vector<DamageSubclass>();
        damageMultiplierList = new Vector<DamageMultiplier>();
    }
    
    public Damage(Tower ownerTower) {
        this.ownerTower = ownerTower;
        damageList = new Vector<DamageSubclass>();
        damageMultiplierList = new Vector<DamageMultiplier>();
    }
    
    public Damage(Mob owner) {
        this.ownerMob = owner;
        damageList = new Vector<DamageSubclass>();
        damageMultiplierList = new Vector<DamageMultiplier>();
    }

    public void addDamage(DamageSubclass d) {
        damageList.add(d);
    }

    public void addAllDamage(DamageSubclass[] d) {
        damageList.addAll(Arrays.asList(d));
    }
    
    public void addDamageMultiplier(DamageMultiplier d) {
        damageMultiplierList.add(d);
    }

    public void addAllDamageMultiplier(DamageMultiplier[] d) {
        damageMultiplierList.addAll(Arrays.asList(d));
    }

    public double getTotalDamage(double armor) {
        double damageCounter = 0;
        for (DamageSubclass currDamage : damageList) {
            damageCounter += currDamage.getDamageValue();
        }
        for(DamageMultiplier currMult : damageMultiplierList) {
            damageCounter *= currMult.getMultiplier();
        }
        
        damageCounter -= armor;
        if(damageCounter < 0) {
            damageCounter = 0;
        }
        return damageCounter;
    }

    public Projectile getOwnerProjectile() {
        return ownerProjectile;
    }
    
    public Mob getOwnerMob() {
        return ownerMob;
    }
    
    public Tower getOwnerTower() {
        return ownerTower;
    }

    //Multipliers
    public interface DamageMultiplier {

        public double getMultiplier();
    }
    
    public static class NormalMultiplier implements DamageMultiplier {
        private double multiplier;
        public NormalMultiplier(double fact) {
            multiplier = fact;
        }
        
        public void setMultiplier(double m) {
            multiplier = m;
        }
        @Override
        public double getMultiplier() {
            //Logger.logServer("Normal mult: " + multiplier);
            return multiplier;
        }
        
      
        
    }

    public static class CriticalDamage implements DamageMultiplier {

        private double critChance, critMultiplier;

        public CriticalDamage(double critChance, double critFact) {
            this.critChance = critChance;
            this.critMultiplier = critFact;
        }

        @Override
        public double getMultiplier() {
            if (Math.random() <= critChance) {
                //Logger.logServer("Crit: x" + critMultiplier);
                return critMultiplier;
            } else {
                //Logger.logServer("No crit ");
                return 1;
            }
        }
        
        public void setCritChance(double chance) {
            critChance = chance;
        }
        
        public void setCritMult(double m) {
            critMultiplier = m;
        }
    }

    // SUBCLASSES
    public interface DamageSubclass {

        public double getDamageValue();

    }

    public static class NormalDamage implements DamageSubclass {

        private double damageVal;

        public NormalDamage(double damage) {
            damageVal = damage;
        }

        @Override
        public double getDamageValue() {
            //Logger.logServer("Normal damage: " + damageVal);
            return damageVal;
        }

        public void setDamageVal(double d) {
            damageVal = d;
        }
    }

}
