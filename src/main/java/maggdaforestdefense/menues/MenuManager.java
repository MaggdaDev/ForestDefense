/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import maggdaforestdefense.gameplay.Game;

import java.io.IOException;
import javafx.scene.text.Font;

/**
 *
 * @author David
 */
public class MenuManager {

    public static Font headingFont = new Font(40);

    private StackPane mainRoot;

    private Screen showingScreen, previousScreen;
    private static MenuManager instance;
    
    private FXMLMenuLoader menuLoader;

    // Menues
    private Parent mainMenu, playMenu, findGameMenu, createGameMenu;
    private WaitForPlayersMenu waitForPlayersMenu;
    private MapEditor mapEditor;

    private LaunchingScreen launchingScreen;

    public MenuManager(StackPane root) {
        instance = this;
        mainRoot = root;
        showingScreen = Screen.LAUNCHING_SCREEN;

        //Menues
        menuLoader = new FXMLMenuLoader();

        mainMenu = menuLoader.loadMenu(FXMLMenuLoader.Menu.MAIN_MENU);
        playMenu = menuLoader.loadMenu(FXMLMenuLoader.Menu.PLAY_MENU);
        createGameMenu = menuLoader.loadMenu(FXMLMenuLoader.Menu.CREATE_GAME_MENU);
        createWaitScreen();
        mapEditor = (MapEditor)menuLoader.loadMenu(FXMLMenuLoader.Menu.EDITOR);
        findGameMenu = menuLoader.loadMenu(FXMLMenuLoader.Menu.FIND_GAME_MENU);

        launchingScreen = new LaunchingScreen();

        setScreenShown(Screen.LAUNCHING_SCREEN);

    }

    public void start() {
        launchingScreen.start();
    }

    public void setScreenShown(Screen screen) {
        previousScreen = showingScreen;
        showingScreen = screen;
        switch (screen) {
            case LAUNCHING_SCREEN:
                showScreen(launchingScreen);
                break;
            case MAIN_MENU:
                showScreen(mainMenu);
                break;
            case MAP_EDITOR:
                mapEditor.firstInit();
                showScreen(mapEditor);
                break;
            case GAME:
                showScreen(Game.getInstance().getGameScreen());
                break;
            case CREATE_GAME:
                showScreen(createGameMenu);
                break;
            case FIND_GAME:
                showScreen(findGameMenu);
                break;
            case PLAY:
                showScreen(playMenu);
                break;
            case WAIT_FOR_PLAYERS:
                showScreen(waitForPlayersMenu);
                break;
        }
    }

    public void showPreviousScreen() {
        setScreenShown(previousScreen);
    }

    public void showScreen(Parent parent) {
        mainRoot.getChildren().clear();
        mainRoot.getChildren().add(parent);
    }

    public void createWaitScreen() {
        waitForPlayersMenu = (WaitForPlayersMenu)menuLoader.loadMenu(FXMLMenuLoader.Menu.WAIT_FOR_PLAYERS_MENU);
    }

    public enum Screen {

        LAUNCHING_SCREEN,
        MAIN_MENU,
        MAP_EDITOR,
        GAME,
        PLAY,
        FIND_GAME,
        CREATE_GAME, 
        WAIT_FOR_PLAYERS;
    }

    public static MenuManager getInstance() {
        return instance;
    }
}
