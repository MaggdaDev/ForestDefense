/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
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
import maggdaforestdefense.network.server.serverGameplay.mobs.Caterpillar;
import maggdaforestdefense.network.server.serverGameplay.mobs.Hirschkaefer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Marienkaefer;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.mobs.Schwimmkaefer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Wanderlaeufer;
import maggdaforestdefense.network.server.serverGameplay.mobs.Wasserlaeufer;
import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;
import maggdaforestdefense.network.server.serverGameplay.spawning.Spawnable;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer;
import maggdaforestdefense.network.server.serverGameplay.towers.Maple;
import maggdaforestdefense.network.server.serverGameplay.towers.Oak;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Waiter;

/**
 *
 * @author David
 */
public class ServerGame extends Thread {
    public final static boolean DEBUG_MODE = false;
    
    

    public static int START_COINS = 100, START_ESSENCE = 2;

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

    private ArrayList<Mob> mobsList;

    private Vector<NetworkCommand> commandsToSend;

    private boolean hasRemovedTrade = false, endGame = false;

    public ServerGame(Player firstPlayer, String name) {
        // DEBUG
        
        if(DEBUG_MODE) {
            START_COINS = 100000;
            coins = START_COINS;
            START_ESSENCE = 1000;
        }
        
        // DEBUG END
        
        
        this.name = name;
        currentGameObjectId = 0;
        players = new Vector<>();
        addPlayer(firstPlayer);

        serverLoop = new ServerLoop(players, this);

        map = Map.generateMap(this);
        base = map.getBase();

        gameObjects = new ConcurrentHashMap<>();

        mobsList = new ArrayList<>();

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
        updatePlayersWaiting();
        player.sendCommand(new NetworkCommand(NetworkCommand.CommandType.LAUNCH_GAME, new CommandArgument[]{new CommandArgument("name", name)}));
    }

    public void updatePlayersWaiting() {
        CommandArgument[] args = new CommandArgument[players.size()];
        for (int i = 0; i < players.size(); i++) {
            args[i] = new CommandArgument("name", players.get(i).getUserName());
        }
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.SHOW_WAITING_PLAYERS, args));

    }

    public void removePlayer(Player owner) {
        players.remove(owner);
        checkPlayers();
        if (!isStarted) {
            updatePlayersWaiting();
        }

    }

    public void endGame() {
        this.endGame = true;
        serverLoop.endGame();
        sendCommandToAllPlayers(NetworkCommand.END_GAME);
        Server.getInstance().getGameHandler().removeGame(gameId);
    }

    public void checkPlayers() {
        if (players.size() == 0) {
            Logger.logServer("GAME ABANDONED BECAUSE OF NO PLAYERS!!!   GAME ID: " + gameId + "      GAME NAME: " + name);
            endGame();
        }
    }

    public void updateReadyProgress() {
        int counter = 0;
        for (Player player : players) {
            if (player.isReadyForNextRound()) {
                counter++;
            }
        }
        double progress = ((double) counter) / ((double) players.size());
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPDATE_READY_CHECK, new CommandArgument[]{new CommandArgument("progress", progress)}));
    }

    public void spawnMob(Spawnable toSpawn) {
        switch (toSpawn.getType()) {
            case M_BORKENKAEFER:
                addMob(new Borkenkaefer(this, serverLoop.getCurrentWaveIndex()));
                break;
            case M_HIRSCHKAEFER:
                addMob(new Hirschkaefer(this, serverLoop.getCurrentWaveIndex()));
                break;
            case M_SCHWIMMKAEFER:
                addMob(new Schwimmkaefer(this));
                break;
            case M_WANDERLAUFER:
                addMob(new Wanderlaeufer(this, serverLoop.getCurrentWaveIndex()));
                break;
            case M_WASSERLAEUFER:
                addMob(new Wasserlaeufer(this));
                break;
            case M_BLATTLAUS:
                addMob(new Blattlaus(this, serverLoop.getCurrentWaveIndex()));
                break;
            case M_MARIENKAEFER:
                addMob(new Marienkaefer(this, serverLoop.getCurrentWaveIndex()));
                break;
            case M_BOSS_CATERPILLAR:
                addMob(new Caterpillar(this, serverLoop.getCurrentWaveIndex()));
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
                if (coins < Maple.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Maple(this, xPos, yPos);
                break;
            case T_LORBEER:
                if (coins < Lorbeer.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Lorbeer(this, xPos, yPos);
                break;

            case T_OAK:
                if (coins < Oak.DEFAULT_PRIZE) {
                    return;
                }
                newTower = new Oak(this, xPos, yPos);
                break;
            default:
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        plantTree(newTower);
    }

    public void plantTree(Tower tower) {
        if(placeOccupied(tower)) {
            Logger.logServer("Place occupied! Tower cant be placed");
            return;
        }
        gameObjects.put(String.valueOf(tower.getId()), tower);
        coins -= tower.getPrize();
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.PLANT_TREE, new CommandArgument[]{
            new CommandArgument("id", String.valueOf(tower.getId())),
            new CommandArgument("xIndex", String.valueOf(tower.getXIndex())),
            new CommandArgument("yIndex", String.valueOf(tower.getYIndex())),
            new CommandArgument("type", String.valueOf(tower.getGameObjectType().ordinal())),
            new CommandArgument("growingTime", tower.getGrowingTime())
        }));

        updateRessources();

        notifyTowerChanges();
    }
    
    private boolean placeOccupied(Tower tower) {
        Tower currTower;
        for(GameObject currObj: gameObjects.values()) {
            if(currObj instanceof Tower) {
                currTower = (Tower)currObj;
                if(tower.getXIndex() == currTower.getXIndex() && tower.getYIndex() == currTower.getYIndex()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateGameObjects(double timeElapsed) {
        if (!endGame) {
            gameObjects.forEach((String key, GameObject g) -> {
                NetworkCommand comm = g.update(timeElapsed);
                if (comm != null) {
                    sendCommandToAllPlayersUDP(comm);
                }
            });
        }

    }

    public void handleEssenceAfterRound() {
        if (!endGame) {
            sendCommandToAllPlayers(NetworkCommand.WAVE_FINISHED);
            gameObjects.forEach((String id, GameObject gObj) -> {
                if (gObj instanceof Tower) {
                    Tower tower = (Tower) gObj;
                    tower.handleAfterWave();
                }
            });
        }
    }

    public void requestEssence(String id) {

        Tower tower = (Tower) gameObjects.get(id);
        if (base.decreaseEssenceIfPossible()) {
            sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.DO_ESSENCE_ANIMATION, new CommandArgument[]{new CommandArgument("id", id)}));
            updateRessources();
            tower.supplyEssence();
        }

    }

    public void handleTreesDieing() {
        gameObjects.forEach((String id, GameObject gObj) -> {
            if (gObj instanceof Tower) {
                Tower tower = (Tower) gObj;
                tower.checkEssenceFed();
            }
        });
    }

    public void performActiveSkill(String id, ActiveSkill skill) {
        ((Tower) gameObjects.get(id)).performActiveSkill(skill);
    }

    public void handleEssenceNewRound() {

        base.refillEssence();
    }

    public void increaseMaxEssence(int i) {
        base.increaseMaxEssence(i);
        updateRessources();
    }

    public void updateRessources() {
        if (!endGame) {
            sendCommandToAllPlayersUDP(new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_RESSOURCES, new CommandArgument[]{new CommandArgument("coins", coins), new CommandArgument("essence", base.getEssence()), new CommandArgument("maxEssence", base.getMaxEssence())}));
        }
    }

    public void damageBase(Mob mob) {
        base.damageBase(mob);
    }

    public void sendCommandToAllPlayers(NetworkCommand command) {
        players.forEach((Player player) -> {
            player.sendCommand(command);
        });
    }

    public void sendCommandToAllPlayersUDP(NetworkCommand command) {
        players.forEach((Player player) -> {
            player.sendCommandUDP(command);
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
        mobsList.add(mob);
        if(mob.getGameObjectType().equals(GameObjectType.M_BOSS_CATERPILLAR)) {
            mobsList.addAll(Arrays.asList(((Caterpillar)mob).getSegments()));
        }
    }

    public void killMob(Mob mob, boolean getGold) {
        mobsList.remove(mob);
        if(mob.getGameObjectType().equals(GameObjectType.M_BOSS_CATERPILLAR)) {
            mobsList.removeAll(Arrays.asList(((Caterpillar)mob).getSegments()));
        }
        removeGameObject(mob);
        if (getGold) {
            coins += mob.calculateCoinValue();
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
    
    public void deleteAllProjectiles() {
        ArrayList<Projectile> projectiles = new ArrayList<>();
        for(GameObject currGameObj : gameObjects.values()) {
            if(currGameObj instanceof Projectile) {
                projectiles.add((Projectile)currGameObj);
            }
        }
        
        projectiles.forEach((Projectile proj)->{
            removeProjectile(proj);
        });
    }

    public void addGold(int i) {
        coins += i;
    }

    public ArrayList<Mob> getMobs() {
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
        updateRessources();
        notifyTowerChanges();

    }

    public void useLorbeerTrade(String id, Upgrade upgrade) {
        Tower tower = (Tower) gameObjects.get(id);
        tower.addUpgrade(upgrade);
        sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPGRADE_BUY_CONFIRMED, new CommandArgument[]{
            new CommandArgument("id", id),
            new CommandArgument("tier", tower.getUpgradeSet().getTier(upgrade)),
            new CommandArgument("type", tower.getUpgradeSet().getType(upgrade))}));
        hasRemovedTrade = false;
        gameObjects.forEach((String key, GameObject gameObject) -> {
            if ((!hasRemovedTrade) && gameObject instanceof Lorbeer) {
                Lorbeer currentLorbeer = (Lorbeer) gameObject;
                if (currentLorbeer.removeTradeUpgrade(upgrade)) {
                    hasRemovedTrade = true;
                }
            }
        });
        notifyTowerChanges();
    }
    
    public void editPlayspeed(int playspeedId) {
        switch(playspeedId) {
            case 0:
                serverLoop.setPlayspeedFact(1);
                break;
            case 1:
                serverLoop.setPlayspeedFact(2);
                break;
        }
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
        gameObjects.forEach((String key, GameObject gameObject) -> {
            if (gameObject instanceof Tower) {
                ((Tower) gameObject).notifyNextRound();
            }
        });
    }

   

    

}
