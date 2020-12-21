/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Blattlaus;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class Lorbeer extends Tower{
    public static final int DEFAULT_PRIZE = 1;
    public final static double DEFAULT_HEALTH = 20, DEFAULT_REGEN = 0;
    public final static double DEFAULT_RANGE = 2;
    public final static double DEFAULT_GROWING_TIME = 2;
    public final static RangeType RANGE_TYPE = RangeType.SQUARED;
    public final static double DEFAULT_DAMAGE = 10;
    public final static int DEFAULT_MAX_LORBEERS = 20;
    public final static int DEFAULT_GOLD_PER_LORBEER = 10;
    
    private Damage damageObject;
    private Damage.NormalDamage usualDamage;
    
    private double attackCooldown = 5.0d, attackTimer = attackCooldown;
    private boolean canAttack = true;
    
    private int lorbeerAmount = 0, maxLorbeerAmount = DEFAULT_MAX_LORBEERS;
    private int goldPerLorbeer = DEFAULT_GOLD_PER_LORBEER;
    
    //UPGRADES
    private double goldPerLorbeerUpgradeMult = 1;
    
    private Damage.NormalDamage executiveDamage;
    
    private boolean isExecutive = false;
    public final static double EXECUTIVE_PERCENTMISSINGHEALTH = 0.2d;
    
    private boolean isErnteRausch = false;
    public final static int RAUSCH_KILL_AMOUNT = 4;
    private int ernteRauschKillCount = 0;
    
    private boolean isMassenproduktion = false;
    public final static double MASSENPRODUKTION_MULT = 1.1;
    
    private boolean isWiederverwertung = false;
    public final static double WIEDERVWERTUNG_ADD = 5;
    
    
    public Lorbeer(ServerGame game, double xPos, double yPos) {
        super(game, xPos, yPos,GameObjectType.T_LORBEER, DEFAULT_PRIZE, UpgradeSet.LORBEER_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(true, true, true), DEFAULT_GROWING_TIME, RANGE_TYPE);
        damageObject = new Damage(this);
        usualDamage = new Damage.NormalDamage(DEFAULT_DAMAGE);
        executiveDamage = new Damage.NormalDamage(0.0d);
        
        damageObject.addAllDamage(new Damage.DamageSubclass[]{usualDamage, executiveDamage});
        //damageObject.addAllDamageMultiplier(new Damage.DamageMultiplier[]{});
        
        onKill.add((o)->
                {
                    if(lorbeerAmount < maxLorbeerAmount) {
                    lorbeerAmount++;
                    }
                });
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
        
        switch(upgrade) {
            case LORBEER_1_1:   // WEITREICHEND
                range += 1;
                break;
            case LORBEER_1_2:   // ERTRAGREICH
                goldPerLorbeerUpgradeMult = (1.5 * goldPerLorbeer);
                goldPerLorbeer = (int)(DEFAULT_GOLD_PER_LORBEER * goldPerLorbeerUpgradeMult);
                break;
            case LORBEER_1_3:   // EFFIZIENTNE ERNTE
                attackCooldown /= 1.5;
                break;
            case LORBEER_1_4:   // SPEICHER
                maxLorbeerAmount *= 1.5;
                break;
                
            case LORBEER_2_1:   // EXECUTIVE
                isExecutive = true;
                break;
            case LORBEER_2_2:   // ERNTERAUSCH
                isErnteRausch = true;
                break;
            case LORBEER_2_3:   // MASSENPRODUKTION
                isMassenproduktion = true;
                break;
            case LORBEER_2_4:   // WIEDERVERWERTUNG
                isWiederverwertung = true;
                break;
                
                
                
        }
    }


    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        if (!checkAlive()) {
            return null;
        }

        if (!isMature) {
            GameImage currImage = updateGrowing(timeElapsed);
            isMature = growingAnimation.isFinished();
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
                new CommandArgument("id", id),
                new CommandArgument("hp", healthPoints),
                new CommandArgument("image", currImage.ordinal()),
                new CommandArgument("timeLeft", growingAnimation.getTimeLeft()), 
                new CommandArgument("range", range)
            });
        } 

        // Regen
        updateRegen(timeElapsed);
        
        if(attackTimer <= attackCooldown) {
            attackTimer += timeElapsed;

        } else {
            canAttack = true;
        }

           
        // Health
        // Upgrades
        if(isMassenproduktion) {
            goldPerLorbeer = (int)(DEFAULT_GOLD_PER_LORBEER * goldPerLorbeerUpgradeMult * (1 + lorbeerAmount/DEFAULT_MAX_LORBEERS));
        }
        
        performUpgradesOnUpdate();

        return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, 
                new CommandArgument[]{new CommandArgument("id", id), 
                new CommandArgument("hp", healthPoints), 
                new CommandArgument("effects", effectSet.toString()),
                new CommandArgument("range", range),
                new CommandArgument("attackCooldown", attackCooldown - attackTimer),
                new CommandArgument("lorbeeren", lorbeerAmount + "-" + maxLorbeerAmount),
                new CommandArgument("coinsPerLorbeer", goldPerLorbeer)});
        
    }
    
    @Override
    public void performActiveSkill(ActiveSkill skill) {
        switch(skill) {
            case LORBEER_ATTACK:
                attack();
                break;
            case LORBEER_SELL:
                sellLorbeers();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    private void sellLorbeers() {
        if(lorbeerAmount > 0) {
            serverGame.addGold(goldPerLorbeer * lorbeerAmount);
            lorbeerAmount = 0;
            
        }
    }
    
    private void attack() {
        if(canAttack) {
            canAttack = false;
            attackTimer = 0;
           
            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TC, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("skill", ActiveSkill.LORBEER_ATTACK.ordinal())}));
            
            ernteRauschKillCount = 0;
            serverGame.getMobs().forEach((String id, Mob mob)->{
                if(isInRange(mob, range)) {
                   
                    executiveDamage.setDamageVal(0.0d);
                    
                    if(isExecutive) {
                        executiveDamage.setDamageVal(EXECUTIVE_PERCENTMISSINGHEALTH * (mob.getMaxHP() - mob.getHP()));
                    }
                    mob.damage(damageObject);
                    if(mob.getHP() <= 0) {
                        ernteRauschKillCount++;
                    } else if(isWiederverwertung) {
                        mob.getEffectSet().addEffect(new EffectSet.Effect(EffectSet.EffectType.GOLDED, EffectSet.Effect.UNLIMITED));
                    }
                }
            });
            if(ernteRauschKillCount >= RAUSCH_KILL_AMOUNT) {
                attackTimer += attackCooldown/2;
            }
            
        }
        
    }
    
}
