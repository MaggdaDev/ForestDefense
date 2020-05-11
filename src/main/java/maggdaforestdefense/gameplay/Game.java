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

/**
 *
 * @author David
 */
public class Game {
    
    private GameLoop gameLoop;
    private boolean isInGame;
    private GameScreen gameScreen;
    private static Game instance;
    
    private Circle testCircle;
    public Game() {
        instance = this;
        isInGame = false;
        testCircle = new Circle(100, Color.RED);
        gameLoop = new GameLoop(testCircle);
        gameScreen = new GameScreen(testCircle);
    }
    
    public void startGame() {
        isInGame = true;
        NetworkManager.getInstance().setInGame(true);
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.GAME);
        gameLoop.start();
        NetworkManager.getInstance().sendCommand(NetworkCommand.START_GAME);
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
