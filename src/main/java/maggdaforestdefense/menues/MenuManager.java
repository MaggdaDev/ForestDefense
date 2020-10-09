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
import javafx.application.Platform;
import javafx.scene.text.Font;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.menues.FXMLMenu;

/**
 *
 * @author David
 */
public class MenuManager {

    public static Font headingFont = new Font(40);

    private StackPane mainRoot;

    private Screen showingScreen, previousScreen;
    private static MenuManager instance;



    // Menues
    private FXMLMenu<CreateGameMenu> createGameMenu;
    private FXMLMenu<FindGameMenu> findGameMenu;
    private FXMLMenu<MainMenu> mainMenu;
    private FXMLMenu<PlayMenu> playMenu;
    private FXMLMenu<WaitForPlayersMenu> waitForPlayersMenu;
    
    private MapEditor mapEditor;

    private LaunchingScreen launchingScreen;

    public MenuManager(StackPane root) {
        instance = this;
        mainRoot = root;
        showingScreen = Screen.LAUNCHING_SCREEN;

        //Menues
 

        mainMenu = new FXMLMenu<>(FXMLMenu.MenuType.MAIN_MENU);
        playMenu = new FXMLMenu<>(FXMLMenu.MenuType.PLAY_MENU);
        createGameMenu = new FXMLMenu<>(FXMLMenu.MenuType.CREATE_GAME_MENU);
        waitForPlayersMenu = new FXMLMenu<>(FXMLMenu.MenuType.WAIT_FOR_PLAYERS_MENU);
        //mapEditor = (MapEditor) menuLoader.loadMenu(FXMLMenuLoader.Menu.EDITOR);
        findGameMenu = new FXMLMenu<>(FXMLMenu.MenuType.FIND_GAME_MENU);

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
                showScreen(mainMenu.getRoot());
                break;
            case MAP_EDITOR:
                mapEditor.firstInit();
                showScreen(mapEditor);
                break;
            case GAME:
                showScreen(Game.getInstance().getGameScreen());
                break;
            case CREATE_GAME:
                showScreen(createGameMenu.getRoot());
                break;
            case FIND_GAME:
                showScreen(findGameMenu.getRoot());
                break;
            case PLAY:
                showScreen(playMenu.getRoot());
                break;
            case WAIT_FOR_PLAYERS:
                showScreen(waitForPlayersMenu.getRoot());
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

    
    public void showJoinableGames(NetworkCommand command) {
        CommandArgument[] args = command.getAllArguments();
        if (args.length == 0) {
            findGameMenu.getController().showEmpty();
        } else {
            for (CommandArgument arg : args) {
               findGameMenu.getController().addEntry(arg.getName(), arg.getValue());
            }
        }
    }

    public void resetWaitScreen() {
        waitForPlayersMenu.getController().reset();
    }

    public void resetFindGameMenu() {
        findGameMenu.getController().reset();
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
