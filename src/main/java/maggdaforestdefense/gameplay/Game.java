/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author David
 */
public class Game {
    
    private GameLoop gameLoop;
    private boolean isInGame;
    private GameScreen gameScreen;
    private static Game instance;
    
    public Game() {
        instance = this;
        isInGame = false;
        gameLoop = new GameLoop();
        gameScreen = new GameScreen();
    }
    
    public void startGame() {
        isInGame = true;
        NetworkManager.getInstance().setInGame(true);
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.GAME);
        gameLoop.start();
        NetworkManager.getInstance().sendCommand(NetworkCommand.START_GAME);
    }
    
    public void generateMap(MapCell[][] mapCellArray) {
        gameScreen.generateMap(mapCellArray);
    }
    
    public void updateTestPosition(double x, double y) {
        gameLoop.updateCircle(x,y);
    }
    
    public GameScreen getGameScreen() {
        return gameScreen;
    }
    
    public static Game getInstance() {
        return instance;
    }
}
