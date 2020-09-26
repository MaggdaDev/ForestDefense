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

    // Menues
    private MainMenu mainMenu;
    private LaunchingScreen launchingScreen;
    private MapEditor mapEditor;
    private PlayMenu playMenu;
    private FindGameMenu findGameMenu;
    private CreateGameMenu createGameMenu;

    public MenuManager(StackPane root) {
        instance = this;
        mainRoot = root;
        showingScreen = Screen.LAUNCHING_SCREEN;

        try {
            //Menues

            FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("maggdaforestdefense/menues/main.fxml"));
            mainLoader.load();
            mainMenu = mainLoader.getRoot();
            
            FXMLLoader playLoader = new FXMLLoader(getClass().getClassLoader().getResource("maggdaforestdefense/menues/play.fxml"));
            playLoader.load();
            playMenu = playLoader.getRoot();
            
            FXMLLoader createLoader = new FXMLLoader(getClass().getClassLoader().getResource("maggdaforestdefense/menues/create.fxml"));
            createLoader.load();
            createGameMenu = createLoader.getRoot();

            FXMLLoader mapEditorLoader = new FXMLLoader(getClass().getClassLoader().getResource("maggdaforestdefense/menues/mapeditor.fxml"));
            mapEditorLoader.load();
            mapEditor = mapEditorLoader.getRoot();

            launchingScreen = new LaunchingScreen();
            
            
            findGameMenu = new FindGameMenu();


        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

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
        }
    }
    
    public void showPreviousScreen() {
        setScreenShown(previousScreen);
    }

    public void showScreen(Parent parent) {
        mainRoot.getChildren().clear();
        mainRoot.getChildren().add(parent);
    }

    public enum Screen {

        LAUNCHING_SCREEN,
        MAIN_MENU,
        MAP_EDITOR,
        GAME,
        PLAY,
        FIND_GAME,
        CREATE_GAME;
    }
    
    public static MenuManager getInstance() {
        return instance;
    }
}
