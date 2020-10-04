/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author DavidPrivat
 */
public class FXMLMenuLoader {

    public static final String PATH_TO_FXML = "maggdaforestdefense/menues/";
    private FXMLLoader loader;

    public FXMLMenuLoader() {
        loader = new FXMLLoader();
    }

    public Parent loadMenu(Menu menu) {
        Parent ret = null;
        try {
            ret = loader.load(getClass().getClassLoader().getResource(PATH_TO_FXML + menu.getFXMLName()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ret;

    }

    public static enum Menu {

        MAIN_MENU("main.fxml"),
        PLAY_MENU("play.fxml"),
        CREATE_GAME_MENU("create.fxml"),
        FIND_GAME_MENU("find.fxml"),
        
        EDITOR("mapeditor.fxml"),
        WAIT_FOR_PLAYERS_MENU("wait.fxml");

        private final String fxmlName;

        Menu(String name) {
            fxmlName = name;

        }

        public String getFXMLName() {
            return fxmlName;
        }
    }
}
