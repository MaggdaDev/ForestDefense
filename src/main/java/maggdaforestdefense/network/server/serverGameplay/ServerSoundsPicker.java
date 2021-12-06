/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.spawning.MobWave;
/**
 *
 * @author DavidPrivat
 */
public class ServerSoundsPicker {
    private boolean alreadyLaufkaefer = false, alreadyHirschkaefer = false, alreadyBorkenKaefer = false, alreadyMarienKaefer = false;
    private ServerGame game;
    public ServerSoundsPicker(ServerGame game) {
        this.game = game;
    }
    
    public void handleNewRound(int currentWaveIndex, MobWave currentWave) {
        if(isCriticalWave(currentWave)) {
            game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SUGGEST_MUSIC, new CommandArgument[]{new CommandArgument("id", Sound.RUNDEN_2.ordinal()), new CommandArgument("later", 0)}));
        } else if(isBossWave(currentWaveIndex)){
            game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SUGGEST_MUSIC, new CommandArgument[]{new CommandArgument("id", Sound.RUNDEN_3_INTRO.ordinal()), new CommandArgument("later", 0)}));
        } else {
            game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SUGGEST_MUSIC, new CommandArgument[]{new CommandArgument("id", Sound.RUNDEN_1_LOOP.ordinal()), new CommandArgument("later", 1)}));
        }
    }
    
    private boolean isCriticalWave(MobWave currentWave) {
        if(currentWave.containsMobType(GameObjectType.M_WANDERLAUFER) && !alreadyLaufkaefer) {
            alreadyLaufkaefer = true;
            return true;
            
        } else if(currentWave.containsMobType(GameObjectType.M_HIRSCHKAEFER) && !alreadyHirschkaefer) {
            alreadyHirschkaefer = true;
            return true;
        }
        if(currentWave.containsMobType(GameObjectType.M_MARIENKAEFER) && !alreadyMarienKaefer) {
            alreadyMarienKaefer = true;
            return true;
            
        } else if(currentWave.containsMobType(GameObjectType.M_BORKENKAEFER) && !alreadyBorkenKaefer) {
            alreadyBorkenKaefer = true;
            return true;
        }
        
        return false;
    }
    
    private boolean isBossWave(int waveIndex) {
        if(waveIndex == 19) {
            return true;
        }
        return false;
    }
    
     public static enum Sound {
        RUNDEN_1_INTRO,
        RUNDEN_1_LOOP,
        RUNDEN_2,
        RUNDEN_3_INTRO,
        RUNDEN_3_LOOP,
        MENU_INTRO,
        MENU_LOOP,
        GAMEOVER,
        SAMSA;
    }
    
    
    
}
