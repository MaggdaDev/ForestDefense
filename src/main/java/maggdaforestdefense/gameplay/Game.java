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
import javafx.scene.input.MouseEvent;
import language.Deutsch;
import language.Language;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class Game {

    public static Language language = new Deutsch();

    private GameLoop gameLoop;
    private boolean isInGame;
    private GameScreen gameScreen;

    private HashMap<String, ClientGameObject> gameObjects;
    private static Game instance;

    private Vector<KeyEventHandler> keyEventHandlers;

    private int essence = 0, coins = 0, maxEssence = 0;

    private Game(String gameName) {
        instance = this;
        isInGame = false;
        keyEventHandlers = new Vector();
        gameLoop = new GameLoop();
        gameScreen = new GameScreen();
        gameObjects = new HashMap<>();

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

        // Key Events
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnKeyPressed((KeyEvent event) -> {
            handleKeyEvent(event.getCode());
        });

    }

    public void endGame(NetworkCommand command) {
        gameLoop.stop();
        gameLoop.setGameRunning(false);
        gameScreen.showGameOverOverlay();

        Logger.logClient("GAMEOVER");
        NetworkManager.getInstance().resetCommandHandler();
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

}
