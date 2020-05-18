/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.Player;

import java.util.Vector;

/**
 *
 * @author David
 */
public class ServerGame extends Thread{

    private ServerLoop serverLoop;
    private Vector<Player> players;
    private Map map;

    public ServerGame(Player firstPlayer) {
        players = new Vector<>();
        players.add(firstPlayer);

        serverLoop = new ServerLoop(players);
        
        map = Map.generateMap();
    }
    
    @Override
    public void run() {     //Start game!
        // Map!
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SHOW_MAP, new CommandArgument[]{new CommandArgument("map", map.toString())}));
        
        
        serverLoop.run();
    }
    
    public void sendCommandToAllPlayers(NetworkCommand command) {
        players.forEach((Player player)->{
            player.sendCommand(command);
        });
    }
}
