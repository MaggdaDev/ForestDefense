/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;

/**
 *
 * @author DavidPrivat
 */
public class Oak extends Tower{
    public final static int DEFAULT_PRIZE = 50;
    public final static double DEFAULT_HEALTH = 100, DEFAULT_REGEN = 0.1, DEFAULT_RANGE=0, GROWING_TIME=5;
    public final static RangeType RANGE_TYPE = RangeType.CIRCLE;
    
    public Oak(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_OAK, DEFAULT_PRIZE, UpgradeSet.OAK_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(false,false,false), GROWING_TIME, RANGE_TYPE);
    }

    
    @Override
    public void updateSpecific(double timeElapsed) {
        
    }
    
    @Override
    public void addUpgrade(Upgrade upgrade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
