/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.HashMap;
import maggdaforestdefense.network.server.Player;

import java.util.List;
import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.mobs.Bug;
import maggdaforestdefense.network.server.serverGameplay.spawning.MobWave;
import maggdaforestdefense.network.server.serverGameplay.spawning.Spawnable;
import maggdaforestdefense.network.server.serverGameplay.spawning.WaveGenerator;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.storage.MobWavesLoader;
import maggdaforestdefense.util.FPSLimiter;
import maggdaforestdefense.util.GameMaths;
import maggdaforestdefense.util.Waiter;

/**
 *
 * @author David
 */
public class ServerLoop {

    private boolean running = true;
    private List<Player> players;

    private long startTimeNano = 0;
    private double runTime = 0, oldRunTime = 0;

    private ServerGame serverGame;

    private int livingMobs = 0, mobsToSpawn = 0;
    private boolean nextWave = false;
    private WaveGenerator waveGenerator;
    private int currentWaveIndex = 0;
    private MobWave currentWave;

    private boolean isInWave = false;
    
    
    private FPSLimiter fpsLimiter;
    
    private ServerSoundsPicker musicPicker;
    
    public ServerLoop(List<Player> playerList, ServerGame game) {
        players = playerList;
        serverGame = game;

        waveGenerator = new WaveGenerator();
        fpsLimiter = new FPSLimiter();
        musicPicker = new ServerSoundsPicker(game);
        Logger.logServer("ServerLoop generated.");
    }

    public void run() {
        Logger.logServer("ServerLoop started.");
        startTimeNano = System.nanoTime();
        oldRunTime = 0;
        
        try {
            Thread.sleep(500);
        } catch(Exception e) {
            
        }

        while (running) {
            
            currentWave = waveGenerator.generateWave(currentWaveIndex);
            mobsToSpawn = currentWave.getMobAmount();

            setAllPlayersNotReady();
            serverGame.sendCommandToAllPlayers(NetworkCommand.WAIR_FOR_READY_NEXT_WAVE);
            
            serverGame.checkPlayers();
            
            serverGame.updateRessources();

            Waiter.waitUntil(() -> {      // wait until
                return allPlayersReadyForNextRound();
            });
            
            serverGame.handleTreesDieing();

            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.NEXT_WAVE, new CommandArgument[]{new CommandArgument("wave", currentWaveIndex + 1)}));
            
            if((currentWaveIndex + 1) % 5 == 0) {
                serverGame.increaseMaxEssence(1);
            }
            
            serverGame.handleEssenceNewRound();
            
            runTime = GameMaths.nanoToSeconds(System.nanoTime() - startTimeNano);
            oldRunTime = runTime;
            
            isInWave = true;
            musicPicker.handleNewRound(currentWaveIndex, currentWave);
            while (running && !(livingMobs == 0 && mobsToSpawn == 0)) {                         // ONE WAVE!
                fpsLimiter.startOfIteration();
                runTime = GameMaths.nanoToSeconds(System.nanoTime() - startTimeNano);
                double timeElapsed = runTime - oldRunTime;
                oldRunTime = runTime;

                serverGame.updateGameObjects(timeElapsed);

                serverGame.updateRessources();

                // MOB SPAWNING
                Spawnable toSpawn = currentWave.update(timeElapsed);
                if (toSpawn != null) {
                    mobsToSpawn--;
                    livingMobs++;
                    serverGame.spawnMob(toSpawn);
                }

                // MOB SPAWNING END
                
               
                
                fpsLimiter.endOfIteration();
                fpsLimiter.doSleep();
            }
            isInWave = false;

            serverGame.handleEssenceAfterRound();
            serverGame.notifyTowersNewRound();

            currentWaveIndex++;

        }
    }

    public boolean allPlayersReadyForNextRound() {
        boolean allReady = true;

        for (Player player : players) {
            if (!player.isReadyForNextRound()) {
                allReady = false;
            }
        }

        return allReady;
    }
    
    private void setAllPlayersNotReady() {
        for (Player player : players) {
            player.setReadyForNextRound(false);
        }
    }

    public void notifyMobDeath() {
        livingMobs--;
    }

    public void endGame() {
        running = false;
    }
    
    public boolean isInWave() {
        return isInWave;
    }
}
