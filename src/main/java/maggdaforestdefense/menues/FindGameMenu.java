/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class FindGameMenu extends VBox{
    private FXMLLoader loader;

    @FXML
    private Button backBtn;
    
    @FXML
    private VBox contentVBox;

    public FindGameMenu() {
        Logger.debugClient("FXML loading works");
    }

    public void initialize() {
        Logger.debugClient("FXML loading actually works");

    }
    
    public void addEntry(String id, String name) {
        contentVBox.getChildren().addAll(new Separator(), new JoinGameEntry(id, name));
    }
    
    @FXML private void back(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }
    
    public static class JoinGameEntry extends HBox{
        private String gameId, gameName;
        private Label idLabel, nameLabel;
        private Button joinBtn;
        public JoinGameEntry(String id, String name) {
            gameId = id;
            gameName = name;
            idLabel = new Label("Game-ID: " + id);
            nameLabel = new Label("Name: " + name);
            joinBtn = new Button("JOIN");
            
            setSpacing(50);
            
            getChildren().addAll(idLabel, nameLabel, joinBtn);
        }
    }

}
