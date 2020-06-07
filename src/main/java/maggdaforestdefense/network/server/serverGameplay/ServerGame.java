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
import java.util.concurrent.ConcurrentHashMap;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;

/**
 *
 * @author David
 */
public class ServerGame extends Thread{

    private ServerLoop serverLoop;
    private Vector<Player> players;
    private ConcurrentHashMap<String, GameObject> gameObjects;
    private Map map;
    
    private int currentGameObjectId;

    public ServerGame(Player firstPlayer) {
        currentGameObjectId = 0;
        players = new Vector<>();
        players.add(firstPlayer);

        serverLoop = new ServerLoop(players, this);
        
        map = Map.generateMap();
        
        gameObjects = new ConcurrentHashMap<>();
    }
    
    @Override
    public void run() {     //Start game!
        // Map!
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SHOW_MAP, new CommandArgument[]{new CommandArgument("map", map.toString())}));
        
        
        serverLoop.run();
    }
    
    public void addNewTower(double xPos, double yPos, GameObjectType type) {
        Tower newTower;
        switch(type) {
            case T_SPRUCE:
                newTower = new Spruce(this, xPos, yPos);
                break;
                
                
                
            default:
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        plantTree(newTower);
    }
    
    public void plantTree(Tower tower) {
        gameObjects.put(String.valueOf(tower.getId()), tower);
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.PLANT_TREE, new CommandArgument[]{
            new CommandArgument("id", String.valueOf(tower.getId())),
            new CommandArgument("xIndex", String.valueOf(tower.getXIndex())),
            new CommandArgument("yIndex", String.valueOf(tower.getYIndex())),
            new CommandArgument("type", String.valueOf(tower.getGameObjectType().ordinal()))
        }));
    }
    
    public void updateGameObjects(double timeElapsed) {
        gameObjects.forEach((String key, GameObject g)->{
            NetworkCommand comm = g.update(timeElapsed);
            if(comm != null) {
            sendCommandToAllPlayers(comm);
            }
        });
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
