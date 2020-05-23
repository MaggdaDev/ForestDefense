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
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;

/**
 *
 * @author David
 */
public class Game {
    
    private GameLoop gameLoop;
    private boolean isInGame;
    private GameScreen gameScreen;
    
    private HashMap<String, ClientGameObject> gameObjects;
    private static Game instance;
    
    private Vector<KeyEventHandler> keyEventHandlers;
    public Game() {
        instance = this;
        isInGame = false;
        keyEventHandlers = new Vector();
        gameLoop = new GameLoop();
        gameScreen = new GameScreen();
        gameObjects = new HashMap<>();
        
    }
    
    public void startGame() {
        isInGame = true;
        NetworkManager.getInstance().setInGame(true);
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.GAME);
        gameLoop.start();
        NetworkManager.getInstance().sendCommand(NetworkCommand.START_GAME);
        
        // Key Events
        
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnKeyPressed((KeyEvent event)->{
            handleKeyEvent(event.getCode());
        });

    }
    
    // General
    public void addGameObject(ClientGameObject gameObject) {
        gameObjects.put(String.valueOf(gameObject.getGameObjectId()), gameObject);
        gameScreen.addGameObject(gameObject);
    }
    
    private void handleKeyEvent(KeyCode keyCode) {
        for(KeyEventHandler handler: keyEventHandlers) {
            if(handler.getKeyCode().equals(keyCode)) {
                handler.handle();
            }
        }
    }
    
    public void addKeyListener(KeyEventHandler handler) {
        keyEventHandlers.add(handler);
    }
    
    
    // Map stuff
    public void generateMap(MapCell[][] mapCellArray) {
        gameScreen.generateMap(mapCellArray);
    }
    
    public void updateMapFocus() {
        gameScreen.updateFocus();
    }
   
    
    
    
    public GameScreen getGameScreen() {
        return gameScreen;
    }
    
    public static Game getInstance() {
        return instance;
    }

    public void updateGameObject(NetworkCommand command) {
        ClientGameObject gObj = gameObjects.get(command.getArgument("id"));
        gObj.update(command);
    }
}
