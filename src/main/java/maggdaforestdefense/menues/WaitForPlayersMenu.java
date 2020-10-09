/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class WaitForPlayersMenu {

    @FXML
    private Button startBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private VBox contentVBox;

    @FXML
    public void initialize() {
        Logger.debugClient("FXML loading actually works");

    }

    @FXML
    private void startGame(ActionEvent e) {
        NetworkManager.getInstance().sendCommand(NetworkCommand.REQUEST_START_GAME);

    }

    @FXML
    private void cancel(ActionEvent e) {
        
    }
    
    public void addPlayerEntry(String name) {
        contentVBox.getChildren().addAll(new PlayerEntry(name), new Separator());
    }

    public void reset() {
       contentVBox.getChildren().clear();
    }
    
    private static class PlayerEntry extends HBox{
        private Label nameLabel;
        
        public PlayerEntry(String name) {
            nameLabel = new Label(name);
            nameLabel.setTextFill(Color.DARKGREEN);
            getChildren().add(nameLabel);
        }
    }

    

}
