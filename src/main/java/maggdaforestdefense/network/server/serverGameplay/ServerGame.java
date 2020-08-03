/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.Player;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class ServerGame extends Thread{
    public final static int START_COINS = 100, START_ESSENCE = 20;
    
    private int essence = START_ESSENCE, coins = START_COINS;
    
    
    private ServerLoop serverLoop;
    private Vector<Player> players;
    private ConcurrentHashMap<String, GameObject> gameObjects;
    private Map map;
    
    private int currentGameObjectId;
    
    private HashMap<String, Mob> mobsList;
    
    

    public ServerGame(Player firstPlayer) {
        currentGameObjectId = 0;
        players = new Vector<>();
        players.add(firstPlayer);

        serverLoop = new ServerLoop(players, this);
        
        map = Map.generateMap();
        
        gameObjects = new ConcurrentHashMap<>();
        
        mobsList = new HashMap<>();
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
                if(coins < Spruce.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Spruce(this, xPos, yPos);
                break;
                
                
                
            default:
                 throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        plantTree(newTower);
    }
    
    public void plantTree(Tower tower) {
        gameObjects.put(String.valueOf(tower.getId()), tower);
        coins -= tower.getPrize();
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.PLANT_TREE, new CommandArgument[]{
            new CommandArgument("id", String.valueOf(tower.getId())),
            new CommandArgument("xIndex", String.valueOf(tower.getXIndex())),
            new CommandArgument("yIndex", String.valueOf(tower.getYIndex())),
            new CommandArgument("type", String.valueOf(tower.getGameObjectType().ordinal())),
            new CommandArgument("growingTime", tower.getGrowingTime())
        }));
        
        notifyTowerChanges();
    }
    
    public void updateGameObjects(double timeElapsed) {
        gameObjects.forEach((String key, GameObject g)->{
            NetworkCommand comm = g.update(timeElapsed);
            if(comm != null) {
                sendCommandToAllPlayers(comm);
            }
        });
        
    }
    
    public void updateRessources() {
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_RESSOURCES, new CommandArgument[]{new CommandArgument("coins", coins), new CommandArgument("essence", essence)}));
    }
   
    public void sendCommandToAllPlayers(NetworkCommand command) {
        players.forEach((Player player)->{
            player.sendCommand(command);
        });
    }
    
    private void addGameObject(GameObject g) {
        gameObjects.put(String.valueOf(g.getId()), g);
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.NEW_GAME_OBJECT, g.toNetworkCommandArgs()));
    }
    
    private void removeGameObject(GameObject g) {
        gameObjects.remove(String.valueOf(g.getId()));
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.REMOVE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("id", String.valueOf(g.getId()))}));
    
    }
    
     
    
    public void addProjectile(Projectile projectile) {
        addGameObject(projectile);
    }
    
    public void removeProjectile(Projectile projectile) {
        removeGameObject(projectile);
    }
    
    public void addMob(Mob mob) {
        addGameObject(mob);
        mobsList.put(String.valueOf(mob.getId()), mob);
    }
    
    public void killMob(Mob mob) {
        mobsList.remove(String.valueOf(mob.getId()));
        removeGameObject(mob);
        coins += mob.getCoinValue();
    }
    
    
    public void killTower(Tower tower) {
        removeGameObject(tower);
        notifyTowerChanges();
    }
    
    public void notifyTowerChanges() {
        gameObjects.forEach((String key, GameObject gameObject)->{
            if(gameObject instanceof Tower) {
                ((Tower)gameObject).notifyTowerChanges();
            }
        });
    }
    
    public HashMap<String, Mob> getMobs() {
        return mobsList;
    }
    
    public Map getMap() {
        return map;
    }
    
    public synchronized int getNextId() {
        return currentGameObjectId++;
    }

    public void buyUpgrade(String id, int tier, int upgradeType) {
   
        
        Tower tower = (Tower)gameObjects.get(id);
        UpgradeSet upgrades = tower.getUpgradeSet();
        Upgrade upgrade = upgrades.getUpgrade(tier, upgradeType);
        if(coins >= upgrade.getPrize()) {
            coins -= (int)upgrade.getPrize();
        tower.addUpgrade(upgrade);
        Logger.logServer("Upgrade tier " + tier + "      type " + upgradeType);
        
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPGRADE_BUY_CONFIRMED, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("tier", tier), new CommandArgument("type", upgradeType)}));
        }
        
        notifyTowerChanges();
        
    }
    
    public ConcurrentHashMap<String, GameObject> getGameObjects() {
        return gameObjects;
    }

   


 

    
}
