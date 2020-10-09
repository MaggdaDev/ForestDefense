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
import javafx.scene.layout.HBox;
import maggdaforestdefense.gameplay.Game;
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
    public void initialize() {
        Logger.debugClient("FXML loading actually works");

    }

    @FXML
    private void startGame(ActionEvent e) {
        Game.getInstance().startGame();
    }

    @FXML
    private void cancel(ActionEvent e) {

    }

    public void reset() {
       
    }

}
