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
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class Lorbeer extends Tower{
    public static final int DEFAULT_PRIZE = 1;
    public final static double DEFAULT_HEALTH = 20, DEFAULT_REGEN = 0;
    public final static double DEFAULT_RANGE = 3;
    public final static double DEFAULT_GROWING_TIME = 10;
    public final static RangeType RANGE_TYPE = RangeType.SQUARED;
    
    
    public Lorbeer(ServerGame game, double xPos, double yPos) {
        super(game, xPos, yPos,GameObjectType.T_LORBEER, DEFAULT_PRIZE, UpgradeSet.LORBEER_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(true, true, true), DEFAULT_GROWING_TIME, RANGE_TYPE);
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
        
        switch(upgrade) {
            
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

           
            // Health
            // Upgrades
            performUpgradesOnUpdate();

            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, 
                    new CommandArgument[]{new CommandArgument("id", id), 
                    new CommandArgument("hp", healthPoints), 
                    new CommandArgument("effects", effectSet.toString()),
                    new CommandArgument("range", range)});
        
    }
    
}
