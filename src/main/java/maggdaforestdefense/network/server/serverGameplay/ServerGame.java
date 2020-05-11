/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import maggdaforestdefense.network.server.Player;

/**
 *
 * @author David
 */
public class ServerGame extends Thread{

    private ServerLoop serverLoop;
    private Vector<Player> players;

    public ServerGame(Player firstPlayer) {
        players = new Vector<>();
        players.add(firstPlayer);

        serverLoop = new ServerLoop(players);
    }
    
    @Override
    public void run() {
        serverLoop.run();
    }
}
