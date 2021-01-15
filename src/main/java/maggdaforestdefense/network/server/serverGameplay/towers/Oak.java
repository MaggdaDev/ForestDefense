/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

/**
 *
 * @author DavidPrivat
 */
public class Oak extends Tower{
    public final static int DEFAULT_PRIZE = 50;
    public final static double DEFAULT_HEALTH = 100, DEFAULT_REGEN = 0.1, DEFAULT_RANGE=0, GROWING_TIME=5;
    public final static RangeType RANGE_TYPE = RangeType.CIRCLE;
    
    
    
    //Upgrade var
    private boolean isEicheln, isWurzeln, isRaueRinde;
    
    // Upgrade const
    public final double HARTE_RINDE_ADD = 50;
    public final double ATTRACT_RANGE = 1.5;

    
    public Oak(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_OAK, DEFAULT_PRIZE, UpgradeSet.OAK_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(false,false,false), GROWING_TIME, RANGE_TYPE);
       
    }

    
    @Override
    public void updateSpecific(double timeElapsed) {
        
    }
    
    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
        
        switch(upgrade) {
            case OAK_1_1:
                maxHealth += HARTE_RINDE_ADD;
                healthPoints += HARTE_RINDE_ADD;
                break;
            case OAK_1_2:
                isEicheln = true;
                break;
            case OAK_1_3:
                isWurzeln = true;
                break;
            case OAK_1_4:
                isRaueRinde = true;
                onDamageTaken.add((m)->{
                    Mob mob = (Mob)m;
                    mob.getEffectSet().addEffect(new EffectSet.Effect(EffectSet.EffectType.SENSITIVE, 3));
                });
                break;
                
                
                
        }
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean shouldPrioritize(double dist, Mob.MovementType movement) {
        if(isEicheln && movement == Mob.MovementType.FLY && dist < ATTRACT_RANGE * mapCell.CELL_SIZE) {
            return true;
        } 
        if(isWurzeln && movement == Mob.MovementType.DIG && dist < ATTRACT_RANGE * mapCell.CELL_SIZE) {
            return true;
        }
        return false;
    }

}
