/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.HashMap;
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
    private HashMap<String, GameObject> gameObjects;
    private Map map;
    
    private int currentGameObjectId;

    public ServerGame(Player firstPlayer) {
        currentGameObjectId = 0;
        players = new Vector<>();
        players.add(firstPlayer);

        serverLoop = new ServerLoop(players, this);
        
        map = Map.generateMap();
        
        gameObjects = new HashMap<>();
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
    
    public void addGameObject(GameObject g) {
        gameObjects.put(String.valueOf(g.getId()), g);
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.NEW_GAME_OBJECT, g.toNetworkCommandArgs()));
    }
    
    public Map getMap() {
        return map;
    }
    
    public synchronized int getNextId() {
        return currentGameObjectId++;
    }
}
