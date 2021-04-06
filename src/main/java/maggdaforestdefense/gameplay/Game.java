/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import java.util.HashMap;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.util.KeyEventHandler;

import java.util.Vector;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import language.Deutsch;
import language.Language;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientLorbeer;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.sound.SoundEngine;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class Game {

    public static Font HEADING_FONT, USUAL_FONT, DESCRIPTION_FONT;

    public static Language language = new Deutsch();

    private GameLoop gameLoop;
    private boolean isInGame;
    private GameScreen gameScreen;

    private HashMap<String, ClientGameObject> gameObjects;
    private static Game instance;

    private Vector<KeyEventHandler> keyEventHandlers;

    private int essence = 0, coins = 0, maxEssence = 0;

    private Vector<Upgrade> lorbeerTradingUpgrades;

    private Game(String gameName) {
        instance = this;
        isInGame = false;
        keyEventHandlers = new Vector();
        gameLoop = new GameLoop();
        gameScreen = new GameScreen();
        gameObjects = new HashMap<>();
        lorbeerTradingUpgrades = new Vector<>();

    }

    public static Game createGame(String gameName) {
        instance = new Game(gameName);
        return instance;
    }

    public void startGame() {
        gameScreen = new GameScreen();
        gameObjects = new HashMap<>();
        isInGame = true;
        NetworkManager.getInstance().resetCommandHandler();
        NetworkManager.getInstance().setInGame(true);

        MenuManager.getInstance().setScreenShown(MenuManager.Screen.GAME);
        gameLoop.start();
        gameLoop.setGameRunning(true);

        // Key Events
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnKeyPressed((KeyEvent event) -> {
            handleKeyEvent(event.getCode());
        });

        maggdaforestdefense.MaggdaForestDefense.getSoundEngine().playSound(SoundEngine.Sound.RUNDEN_1_INTRO);

    }

    public void endGame(NetworkCommand command) {

        gameScreen.showGameOverOverlay();

        Logger.logClient("GAMEOVER");
        NetworkManager.getInstance().setInGame(false);
        NetworkManager.getInstance().resetCommandHandler();

        maggdaforestdefense.MaggdaForestDefense.getSoundEngine().playSound(SoundEngine.Sound.GAMEOVER);
        gameLoop.stop();
        gameLoop.setGameRunning(false);
    }

    // General
    public void addGameObject(ClientGameObject gameObject) {
        gameObjects.put(String.valueOf(gameObject.getGameObjectId()), gameObject);
        gameScreen.addGameObject(gameObject);
    }

    public void removeGameObject(String id) {
        ClientGameObject remove = gameObjects.get(id);

        gameObjects.remove(id);
        if (remove != null) {
            gameScreen.removeGameObject(remove);
        }
    }

    private void handleKeyEvent(KeyCode keyCode) {
        for (KeyEventHandler handler : keyEventHandlers) {
            if (handler.getKeyCode().equals(keyCode)) {
                handler.handle();
            }
        }
    }

    public void addKeyListener(KeyEventHandler handler) {
        keyEventHandlers.add(handler);
    }

    public void setOnMouseMoved(EventHandler<MouseEvent> h) {
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnMouseMoved(h);
    }

    // Map stuff
    public void generateMap(ClientMapCell[][] mapCellArray) {
        gameScreen.generateMap(mapCellArray);
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public static Game getInstance() {
        return instance;
    }

    // COMMAND HANDLES
    public void updateGameObject(NetworkCommand command) {
        ClientGameObject gObj = gameObjects.get(command.getArgument("id"));
        if (gObj != null) {
            gObj.update(command);
        }
    }

    public void performActiveSkill(NetworkCommand command) {
        ((ClientTower) gameObjects.get(command.getArgument("id"))).performActiveSkill(ActiveSkill.values()[(int) command.getNumArgument("skill")]);
    }

    public void plantTree(NetworkCommand command) {
        ClientTower tree = (ClientTower) GameObject.generateClientGameObject(command);
        gameObjects.put(String.valueOf(tree.getGameObjectId()), tree);

        gameScreen.addTower(tree);
    }

    public void updateRessources(NetworkCommand command) {
        coins = (int) command.getNumArgument("coins");
        essence = (int) command.getNumArgument("essence");
        maxEssence = (int) command.getNumArgument("maxEssence");

        gameScreen.getTopOverlay().updateRessourceDisplays(coins, essence, maxEssence);
        gameScreen.getEssenceMenu().updateEssenceLevel(essence, maxEssence);
        gameScreen.getSideMenu().updateCoins(coins);
    }

    public void buyUpgrade(NetworkCommand command) {
        String id = command.getArgument("id");
        int tier = (int) command.getNumArgument("tier");
        int type = (int) command.getNumArgument("type");

        ClientTower tower = (ClientTower) gameObjects.get(id);
        tower.buyUpgrade(tier, type);
    }

    public void announceWave(NetworkCommand command) {
        int wave = (int) command.getNumArgument("wave");
        gameScreen.hideReadyCheck();
        gameScreen.announceWave(wave);

        Vector<String> idsToRemove = new Vector<>();

        gameObjects.forEach((String id, ClientGameObject gameObj) -> {
            if (!(gameObj instanceof ClientTower)) {
                idsToRemove.add(id);
            }
        });
        for (String idToRemove : idsToRemove) {
            gameObjects.remove(idsToRemove);
        }
    }

    public void doEssenceAnimtion(NetworkCommand command) {
        String id = command.getArgument("id");
        ClientTower tower = (ClientTower) gameObjects.get(id);
        gameScreen.doEssenceAnimtionTo(tower);
    }

    public void readyCheck() {
        gameScreen.showReadyCheck();
    }

    public int getCoins() {
        return coins;
    }

    public void essenceAnimationFinished(EssenceAnimation animation, ClientTower tower) {
        gameScreen.getGamePlayGroup().getChildren().remove(animation);
        tower.doReceiveEssenceAnimation();
    }

    public void waveFinished() {

    }

    public void towerNeedEssence(NetworkCommand command) {
        String id = command.getArgument("id");
        ((ClientTower) gameObjects.get(id)).essenceNeeded();
    }

    public void updateReadyCheck(NetworkCommand command) {
        Platform.runLater(() -> {
            gameScreen.updateReadyCheck(command.getNumArgument("progress"));
        });
    }

    public void addLorbeerTradingUpgrade(Upgrade upgrade) {
        lorbeerTradingUpgrades.add(upgrade);
        refreshLorbeerTrading();
    }

    public void removeLorbeerTradingUpgrade(Upgrade upgrade) {
        if (lorbeerTradingUpgrades.contains(upgrade)) {
            lorbeerTradingUpgrades.remove(upgrade);
        }
        refreshLorbeerTrading();
    }

    public void removeAllLorbeerTradingUpgrades(Vector<Upgrade> tradeUpgrades) {
        lorbeerTradingUpgrades.removeAll(tradeUpgrades);
        refreshLorbeerTrading();
    }

    public void clearLorbeerTradingUpgrades() {

        lorbeerTradingUpgrades.clear();
        refreshLorbeerTrading();
    }

    public void refreshLorbeerTrading() {
        gameObjects.forEach((String id, ClientGameObject gameObject) -> {
            if (gameObject instanceof ClientTower) {
                ClientTower tower = (ClientTower) gameObject;
                tower.clearLorbeerTrading();
                lorbeerTradingUpgrades.forEach((Upgrade upgrade) -> {
                    if (upgrade.getOwnerType() == tower.getType()) {
                        tower.addLorbeerTrade(upgrade);
                    }
                });
            }
        });
    }

    public void suggestMusic(NetworkCommand command) {
        int musicId = (int) command.getNumArgument("id");
        int isLater = (int) command.getNumArgument("later");
        if (isLater == 0) {
            maggdaforestdefense.MaggdaForestDefense.getSoundEngine().playSound(SoundEngine.Sound.values()[musicId]);
        } else {
            maggdaforestdefense.MaggdaForestDefense.getSoundEngine().playLater(SoundEngine.Sound.values()[musicId]);
        }
    }

    public void editTauschhandel(NetworkCommand command) {
        String id = command.getArgument("id");
        ((ClientLorbeer) gameObjects.get(id)).editTauschhandel(command);

    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public static void addGamePlayNode(Node node) {
        instance.getGameScreen().getGamePlayGroup().getChildren().add(node);
    }

    public static void removeGamePlayNode(Node node) {
        instance.getGameScreen().safeRemoveGameplayNode(node);
    }

    public Vector<Upgrade> getLorbeerTrades() {
        return lorbeerTradingUpgrades;
    }

}
