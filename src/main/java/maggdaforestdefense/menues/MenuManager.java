/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 *
 * @author David
 */
public class MenuManager {

    private StackPane mainRoot;

    private Screen showingScreen, previousScreen;
    private static MenuManager instance;

    // Menues
    private MainMenu mainMenu;
    private LaunchingScreen launchingScreen;

    public MenuManager(StackPane root) {
        instance = this;
        mainRoot = root;
        showingScreen = Screen.LAUNCHING_SCREEN;

        //Menues
        mainMenu = new MainMenu();
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
        GAME;
    }
    
    public static MenuManager getInstance() {
        return instance;
    }
}
