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
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.storage.MobWavesLoader;
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
    private Vector<MobWave> mobWaves;
    private int currentWaveIndex = 0;
    private MobWave currentWave;

    public ServerLoop(List<Player> playerList, ServerGame game) {
        players = playerList;
        serverGame = game;

        mobWaves = MobWavesLoader.loadMobWaves();
    }

    public void run() {
        startTimeNano = System.nanoTime();
        oldRunTime = 0;

        while (running) {
            currentWave = mobWaves.get(currentWaveIndex);
            mobsToSpawn = currentWave.getMobAmount();

            setAllPlayersNotReady();
            serverGame.sendCommandToAllPlayers(NetworkCommand.WAIR_FOR_READY_NEXT_WAVE);

            Waiter.waitUntil(() -> {      // wait unti
                return allPlayersReadyForNextRound();
            });
            
            serverGame.handleTreesDieing();

            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.NEXT_WAVE, new CommandArgument[]{new CommandArgument("wave", currentWaveIndex + 1)}));
            
            serverGame.handleEssenceNewRound();
            
            runTime = GameMaths.nanoToSeconds(System.nanoTime() - startTimeNano);
            oldRunTime = runTime;

            while (running && !(livingMobs == 0 && mobsToSpawn == 0)) {                         // ONE WAVE!
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
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            serverGame.handleEssenceAfterRound();

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
}
