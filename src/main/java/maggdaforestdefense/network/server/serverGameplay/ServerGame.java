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
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.network.server.serverGameplay.mobs.Blattlaus;
import maggdaforestdefense.network.server.serverGameplay.mobs.Borkenkaefer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Bug;
import maggdaforestdefense.network.server.serverGameplay.mobs.Hirschkaefer;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.mobs.Schwimmkaefer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Wanderlaeufer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Wasserlaeufer;
import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;
import maggdaforestdefense.network.server.serverGameplay.spawning.Spawnable;
import maggdaforestdefense.network.server.serverGameplay.towers.Maple;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Waiter;

/**
 *
 * @author David
 */
public class ServerGame extends Thread {

    public final static int START_COINS = 100, START_ESSENCE = 8;
    
    // MANAGEMENT
    private final String name;
    private boolean isStarted = false;
    private String gameId = null;
    
    // GAMEPLAY
    private int coins = START_COINS;
    private Base base;

    private ServerLoop serverLoop;
    private Vector<Player> players;
    private ConcurrentHashMap<String, GameObject> gameObjects;
    private Map map;

    private int currentGameObjectId;

    private HashMap<String, Mob> mobsList;
    
    private Vector<NetworkCommand> commandsToSend;

    public ServerGame(Player firstPlayer, String name) {
        this.name = name;
        currentGameObjectId = 0;
        players = new Vector<>();
        addPlayer(firstPlayer);

        serverLoop = new ServerLoop(players, this);

        map = Map.generateMap(this);
        base = map.getBase();

        gameObjects = new ConcurrentHashMap<>();

        mobsList = new HashMap<>();
        
        commandsToSend = new Vector<>();
    }

    @Override
    public void run() {     //Start game!
        // Map!
        isStarted = true;
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SHOW_MAP, new CommandArgument[]{new CommandArgument("map", map.toString())}));

        serverLoop.run();
    }
    
    public void addPlayer(Player player) {
        players.add(player);
        CommandArgument[] args = new CommandArgument[players.size()];
        for(int i = 0; i < players.size(); i++) {
            args[i] = new CommandArgument("name", players.get(i).getUserName());
        }
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SHOW_WAITING_PLAYERS, args));
        player.sendCommand(new NetworkCommand(NetworkCommand.CommandType.LAUNCH_GAME, new CommandArgument[]{new CommandArgument("name", name)}));
    }
    
    public void removePlayer(Player owner) {
        players.remove(owner);
        checkPlayers();
    }

    public void endGame() {
        serverLoop.endGame();
        sendCommandToAllPlayers(NetworkCommand.END_GAME);
        Server.getInstance().getGameHandler().removeGame(gameId);
    }
    
    public void flushCommands() {
        CommandArgument[] args = new CommandArgument[commandsToSend.size()];
        for(int i = 0; i < commandsToSend.size(); i++) {
            args[i] = new CommandArgument(String.valueOf(i), commandsToSend.get(i));
        }
        commandsToSend.clear();
        
        NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.UPDATE, args);
        sendCommandToAllPlayers(command);
    }
    
    public void checkPlayers() {
        if(players.size() == 0) {
            Logger.logServer("GAME ABANDONED BECAUSE OF NO PLAYERS!!!   GAME ID: " + gameId + "      GAME NAME: " + name);
            endGame();
        }
    }
    
    public void updateReadyProgress() {
        int counter = 0;
        for(Player player: players) {
            if(player.isReadyForNextRound()) {
                counter++;
            }
        }
        double progress = ((double)counter)/((double)players.size());
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPDATE_READY_CHECK, new CommandArgument[]{new CommandArgument("progress", progress)}));
    }

    public void spawnMob(Spawnable toSpawn) {
        switch (toSpawn.getType()) {
            case M_BORKENKAEFER:
                addMob(new Borkenkaefer(this));
                break;
            case M_HIRSCHKAEFER:
                addMob(new Hirschkaefer(this));
                break;
            case M_SCHWIMMKAEFER:
                addMob(new Schwimmkaefer(this));
                break;
            case M_WANDERLAUFER:
                addMob(new Wanderlaeufer(this));
                break;
            case M_WASSERLAEUFER:
                addMob(new Wasserlaeufer(this));
                break;
            case M_BLATTLAUS:
                addMob(new Blattlaus(this));
                break;
                default:
                    throw new UnsupportedOperationException();
        }
    }

    public void addNewTower(double xPos, double yPos, GameObjectType type) {
        Tower newTower;
        switch (type) {
            case T_SPRUCE:
                if (coins < Spruce.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Spruce(this, xPos, yPos);
                break;
            case T_MAPLE:
                if(coins < Maple.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Maple(this, xPos, yPos);
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
        gameObjects.forEach((String key, GameObject g) -> {
            NetworkCommand comm = g.update(timeElapsed);
            if (comm != null) {
                queueUpdateCommand(comm);
            }
        });

    }

    public void handleEssenceAfterRound() {
        sendCommandToAllPlayers(NetworkCommand.WAVE_FINISHED);
        gameObjects.forEach((String id, GameObject gObj)->{
            if(gObj instanceof Tower) {
                Tower tower = (Tower)gObj;
                tower.handleAfterWave();
            }
        });
    }
    
    public void requestEssence(String id) {
         
        
        Tower tower = (Tower)gameObjects.get(id);
        if(base.decreaseEssenceIfPossible()) {
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.DO_ESSENCE_ANIMATION, new CommandArgument[]{new CommandArgument("id", id)}));
            updateRessources();
            tower.supplyEssence();
        }
        

    }
    
    public void handleTreesDieing() {
        gameObjects.forEach((String id, GameObject gObj)->{
            if(gObj instanceof Tower) {
                Tower tower = (Tower) gObj;
                tower.checkEssenceFed();
            }
        });
    }
    
    public void handleEssenceNewRound() {
        base.refillEssence();
    }

    public void updateRessources() {
        queueUpdateCommand(new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_RESSOURCES, new CommandArgument[]{new CommandArgument("coins", coins), new CommandArgument("essence", base.getEssence()), new CommandArgument("maxEssence", base.getMaxEssence())}));
    }

    public void damageBase(Mob mob) {
        base.damageBase(mob);
    }
    
    public void queueUpdateCommand(NetworkCommand command) {
        if(serverLoop.isInWave()) {
        commandsToSend.add(command);
        } else {
            sendCommandToAllPlayers(command);
        }
    }

    public void sendCommandToAllPlayers(NetworkCommand command) {
        players.forEach((Player player) -> {
            player.sendCommand(command);
        });
    }

    private void addGameObject(GameObject g) {
        gameObjects.put(String.valueOf(g.getId()), g);
        queueUpdateCommand(new NetworkCommand(NetworkCommand.CommandType.NEW_GAME_OBJECT, g.toNetworkCommandArgs()));
    }

    private void removeGameObject(GameObject g) {
        gameObjects.remove(String.valueOf(g.getId()));
        queueUpdateCommand(new NetworkCommand(NetworkCommand.CommandType.REMOVE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("id", String.valueOf(g.getId()))}));

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

    public void killMob(Mob mob, boolean getGold) {
        mobsList.remove(String.valueOf(mob.getId()));
        removeGameObject(mob);
        if (getGold) {
            coins += mob.getCoinValue();
        }
        serverLoop.notifyMobDeath();
    }

    public void killTower(Tower tower) {
        removeGameObject(tower);
        notifyTowerChanges();
    }

    public void notifyTowerChanges() {
        gameObjects.forEach((String key, GameObject gameObject) -> {
            if (gameObject instanceof Tower) {
                ((Tower) gameObject).notifyTowerChanges();
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

        Tower tower = (Tower) gameObjects.get(id);
        UpgradeSet upgrades = tower.getUpgradeSet();
        Upgrade upgrade = upgrades.getUpgrade(tier, upgradeType);
        if (coins >= upgrade.getPrize()) {
            coins -= (int) upgrade.getPrize();
            tower.addUpgrade(upgrade);
            Logger.logServer("Upgrade tier " + tier + "      type " + upgradeType);

            sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPGRADE_BUY_CONFIRMED, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("tier", tier), new CommandArgument("type", upgradeType)}));
        }

        notifyTowerChanges();

    }

    public ConcurrentHashMap<String, GameObject> getGameObjects() {
        return gameObjects;
    }
    
    public String getGameName() {
        return name;
    }
    
    public boolean isStarted() {
        return isStarted;
    }

    public void setGameId(String id) {
        this.gameId = id;
    }

    public void notifyTowersNewRound() {
        gameObjects.forEach((String key, GameObject gameObject)->{
            if(gameObject instanceof Tower) {
                ((Tower)gameObject).notifyNextRound();
            }
        });
    }

   

    

    

  

    

   

    

    

}
