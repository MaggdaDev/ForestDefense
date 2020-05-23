/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.server.Player;

import java.util.List;
import maggdaforestdefense.network.server.serverGameplay.mobs.Bug;
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author David
 */
public class ServerLoop{
    private boolean running = true;
    private List<Player> players;

    private long startTimeNano = 0;
    private double runTime = 0;
    
    private ServerGame serverGame;
    public ServerLoop(List<Player> playerList, ServerGame game) {
        players = playerList;
        serverGame = game;
    }
    

    public void run() {
        startTimeNano = System.nanoTime();
        
        //Test
        double secondsBetweenspawns = 1;
        int spawnAmount = 0;
        while(running) {
            runTime = GameMaths.nanoToSeconds(System.nanoTime() - startTimeNano);
            
            
            //TEST
            if(secondsBetweenspawns*spawnAmount < runTime) {
                spawnAmount++;
                serverGame.addGameObject(new Bug(serverGame));
            }
            
            
            //TEST END
            players.forEach((Player player)->{
                player.testUpdateCircle();
            });
            try {
                Thread.sleep(1);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
