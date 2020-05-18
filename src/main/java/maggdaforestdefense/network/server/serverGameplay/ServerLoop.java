/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.server.Player;

import java.util.List;

/**
 *
 * @author David
 */
public class ServerLoop{
    private boolean running = true;
    private List<Player> players;
    public ServerLoop(List<Player> playerList) {
        players = playerList;
    }
    

    public void run() {
        while(running) {
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
