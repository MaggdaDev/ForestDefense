/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

/**
 *
 * @author DavidPrivat
 * @param <E>
 */
public class FXMLMenu <E> {

    public static final String PATH_TO_FXML = "maggdaforestdefense/menues/";

    private FXMLLoader loader;

    private Parent root;
    private E controller;
    public FXMLMenu(MenuType type) {
        loader = new FXMLLoader(getClass().getClassLoader().getResource(PATH_TO_FXML + type.getFXMLName()));
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        controller = (E) loader.getController();
        root = loader.getRoot();

    }
    
    public Parent getRoot() {
        return root;
    }
    
    public E getController() {
        return controller;
    }

    public static enum MenuType {

        MAIN_MENU("main.fxml"),
        PLAY_MENU("play.fxml"),
        CREATE_GAME_MENU("create.fxml"),
        FIND_GAME_MENU("find.fxml"),
        EDITOR("mapeditor.fxml"),
        WAIT_FOR_PLAYERS_MENU("wait.fxml");

        private final String fxmlName;

        MenuType(String name) {
            fxmlName = name;

        }

        public String getFXMLName() {
            return fxmlName;
        }
    }
}
