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
    private Projectile owner;

    protected Damage(double d, DamageType type, Projectile owner) {
        damage = d;
        this.type = type;
        this.owner = owner;
    }

    public static class DirectDamage extends Damage {

        public DirectDamage(double d, Projectile owner) {
            super(d, DamageType.DIRECT, owner);
        }

    }

    public double getDamage() {
        return damage;
    }

    public DamageType getType() {
        return type;
    }

    public Projectile getOwnerProjectile() {
        return owner;
    }

    public static enum DamageType {
        DIRECT;
    }
    
    public void setDamage(double d) {
        damage = d;
    }
}
