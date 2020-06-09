/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;

/**
 *
 * @author DavidPrivat
 */
public abstract class Damage {
    private double damage;
    private final DamageType type;

    protected Damage(double d, DamageType type) {
        damage = d;
        this.type = type;
    } 
    public static class DirectDamage extends Damage{
        public DirectDamage(double d) {
            super(d, DamageType.DIRECT);
        }
    }
    
    public double getDamage() {
        return damage;
    }
    
    public DamageType getType() {
        return type;
    }

    
    public static enum DamageType{
        DIRECT;
    }
}
