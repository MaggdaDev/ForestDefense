/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.spawning.MobWave;
import maggdaforestdefense.sound.SoundEngine;

/**
 *
 * @author DavidPrivat
 */
public class ServerSoundsPicker {
    private boolean alreadyLaufkaefer = false, alreadyHirschkaefer = false;
    private ServerGame game;
    public ServerSoundsPicker(ServerGame game) {
        this.game = game;
    }
    
    public void handleNewRound(int currentWaveIndex, MobWave currentWave) {
        if(isBossWave(currentWave)) {
            game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SUGGEST_MUSIC, new CommandArgument[]{new CommandArgument("id", SoundEngine.Sound.RUNDEN_2.ordinal()), new CommandArgument("later", 0)}));
        } else {
            game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SUGGEST_MUSIC, new CommandArgument[]{new CommandArgument("id", SoundEngine.Sound.RUNDEN_1_LOOP.ordinal()), new CommandArgument("later", 1)}));
        }
    }
    
    private boolean isBossWave(MobWave currentWave) {
        if(currentWave.containsMobType(GameObjectType.M_WANDERLAUFER) && !alreadyLaufkaefer) {
            alreadyLaufkaefer = true;
            return true;
            
        } else if(currentWave.containsMobType(GameObjectType.M_HIRSCHKAEFER) && !alreadyHirschkaefer) {
            alreadyHirschkaefer = true;
            return true;
        }
        
        return false;
    }
    
    
    
}
