/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower.CanAttackSet;
import maggdaforestdefense.util.GameMaths;
import maggdaforestdefense.util.UpgradeHandler;

/**
 *
 * @author DavidPrivat
 */
public class SpruceShot extends ConstantFlightProjectile {

    private final static double DEFAULT_SPEED = 1000;
    private final static int DEFAULT_RANGE = Spruce.DEFAULT_RANGE;
    private final static double HITBOX_RADIUS = 10;
    private final static double DAMAGE = 10;
    private final static int DEFAULT_PIERCE = 2;

    private Damage damageObject;

    // Upgrade consts
    private final static double percetageOfHealthRiesenschreck = 0.1;
    private final static double extraDamagePerPixel = 0.01;
    private final static double critChance = 0.2, critMultiplier = 5;
    private final static double armorPierceDamageMult = 1;

    // Upgrade vars
    private boolean isRiesenschreck = false;
    private boolean isArmorPierce = false;
    private Damage.NormalDamage basicDamage, riesenschreckDamage, dominierendeNadelnDamage, armorPierceDamage;
    private Damage.NormalMultiplier nadelStaerkungMult;
    private Damage.CriticalDamage criticalDamageMult;

    public SpruceShot(int id, ServerGame game, double x, double y, Mob target, Tower ownerTower, Tower.CanAttackSet attackSet) {
        super(id, GameObjectType.P_SPRUCE_SHOT, DEFAULT_RANGE, target, x, y, DEFAULT_SPEED, game, new HitBox.CircularHitBox(HITBOX_RADIUS, x, y), DEFAULT_PIERCE, ownerTower, attackSet);
        serverGame = game;
        xPos = x;
        yPos = y;

        calculateSpeed(target);
        setUpDamage();
        ownerTower.getUpgrades().forEach((Upgrade upgrade) -> {
            addUpgrade(upgrade);
        });

    }

    private SpruceShot(int id, ServerGame game, double x, double y, double xSpd, double ySpd, Tower ownerTower, double pierce, Vector<Mob> mobsDamaged, CanAttackSet attackSet) {
        super(id, GameObjectType.P_SPRUCE_SHOT, DEFAULT_RANGE, null, x, y, DEFAULT_SPEED, game, new HitBox.CircularHitBox(HITBOX_RADIUS, x, y), DEFAULT_PIERCE, ownerTower, attackSet);
        serverGame = game;
        xPos = x;
        yPos = y;
        this.xSpd = xSpd;
        this.ySpd = ySpd;
        this.pierce = pierce;

        this.mobsDamaged = mobsDamaged;

        damageObject = new Damage(this);
        setUpDamage();
    }

    private final void setUpDamage() {
        damageObject = new Damage(this);
        riesenschreckDamage = new Damage.NormalDamage(0);
        dominierendeNadelnDamage = new Damage.NormalDamage(0);
        armorPierceDamage = new Damage.NormalDamage(0);
        basicDamage = new Damage.NormalDamage(DAMAGE);
        damageObject.addAllDamage(new Damage.DamageSubclass[]{basicDamage, riesenschreckDamage, dominierendeNadelnDamage, armorPierceDamage});

        nadelStaerkungMult = new Damage.NormalMultiplier(1);
        criticalDamageMult = new Damage.CriticalDamage(0, 1);
        damageObject.addAllDamageMultiplier(new Damage.DamageMultiplier[]{nadelStaerkungMult, criticalDamageMult});
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.P_SPRUCE_SHOT.ordinal())),
            new CommandArgument("id", String.valueOf(id))};
    }

    @Override
    public NetworkCommand update(double timeElapsed) {

        boolean stillExists = updateFlight(timeElapsed);

        collision(serverGame.getMobs());
        if (stillExists) {
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
                new CommandArgument("y", String.valueOf(yPos)),
                new CommandArgument("id", String.valueOf(id))});
        } else {
            return null;
        }
    }

    @Override
    public void dealDamage(Mob target) {
        if (pierce > 0 && (!mobsDamaged.contains(target))) {
            performUpgradesBeforeCollision();
            pierce--;
            mobsDamaged.add(target);

            if (isRiesenschreck) {       // RIESENSCHRECK UPGRADE
                riesenschreckDamage.setDamageVal(percetageOfHealthRiesenschreck * target.getHP());
            }
            
            if(isArmorPierce) {         // ARMOR PIERCE UPGRADE
                armorPierceDamage.setDamageVal(target.getArmor() * armorPierceDamageMult);
            }

            notifyOwnerDamage(target.damage(damageObject));
            performUpgradesAfterCollision();
            
            
        }
        
    }

    private void addUpgrade(Upgrade upgrade) {
        switch (upgrade) {
            case SPRUCE_1_1:    // nadelteilung
                beforeCollision.add(new UpgradeHandler() {
                    @Override
                    public void handleUpgrade() {
                        performNeedleSplit();
                        beforeCollision.remove(this);

                    }
                });
                break;
            case SPRUCE_1_3:           // Ruestungsdurchdringende Nadeln
                isArmorPierce = true;
                break;
            case SPRUCE_1_5:    // Nadelstaerkung
                afterCollision.add(new UpgradeHandler() {
                    @Override
                    public void handleUpgrade() {
                        performNeedlesStronger();
                        afterCollision.remove(this);

                    }
                });
                break;
            case SPRUCE_2_3:    // kritische nadeln
                criticalDamageMult.setCritChance(critChance);
                criticalDamageMult.setCritMult(critMultiplier);
                break;
            case SPRUCE_2_5:    // Erbarmungslose fichte
                onKill.add(new UpgradeHandler() {
                    @Override
                    public void handleUpgrade() {
                        performErbarmungslos();

                    }
                });
                break;
            case SPRUCE_3_3:   //RIESENSCHRECK
                isRiesenschreck = true;
                break;
            case SPRUCE_3_5:    // Dominierende nadeln
                beforeCollision.add(() -> {
                    dominierendeNadelnDamage.setDamageVal(extraDamagePerPixel * distanceTravelled);
                });
                break;

        }
    }

    // Perform upgrades
    private void performNeedleSplit() {
        double totSpd = GameMaths.getAbs(xSpd, ySpd);
        double otherX1 = ySpd * -1;
        double otherY1 = xSpd;

        double otherX2 = ySpd;
        double otherY2 = xSpd * -1;

        double newVec1X = 3 * xSpd + otherX1;
        double newVec1Y = 3 * ySpd + otherY1;
        double tot1 = GameMaths.getAbs(newVec1X, newVec1Y);

        double newVec2X = 3 * xSpd + otherX2;
        double newVec2Y = 3 * ySpd + otherY2;
        double tot2 = GameMaths.getAbs(newVec2X, newVec2Y);

        newVec1X = newVec1X * totSpd / tot1;
        newVec1Y = newVec1Y * totSpd / tot1;

        newVec2X = newVec2X * totSpd / tot2;
        newVec2Y = newVec2Y * totSpd / tot2;

        serverGame.addProjectile(new SpruceShot(serverGame.getNextId(), serverGame, xPos, yPos, newVec1X, newVec1Y, owner, pierce + 1, mobsDamaged, canAttackSet));
        serverGame.addProjectile(new SpruceShot(serverGame.getNextId(), serverGame, xPos, yPos, newVec2X, newVec2Y, owner, pierce + 1, mobsDamaged, canAttackSet));

        pierce = 0;
    }

    private void performNeedlesStronger() {

        nadelStaerkungMult.setMultiplier(Spruce.NADEL_STAERKUNG_MULT);
    }

    private void performErbarmungslos() {
        pierce++;
    }

}
