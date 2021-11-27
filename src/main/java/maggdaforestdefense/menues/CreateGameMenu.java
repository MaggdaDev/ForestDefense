/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class CreateGameMenu {

    @FXML
    private TextField gameNameTF;

    @FXML
    private Button okBtn;

    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        Logger.debugClient("FXML loading actually works");

        gameNameTF.textProperty().addListener((ChangeListener<String>) (a, b, c) -> {
            okBtn.setDisable(gameNameTF.getText().isBlank());
        });
        okBtn.setDisable(true);

    }

    @FXML
    private void okBtnOnClick(ActionEvent e) {
        String gameName = gameNameTF.getText();
        NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.CREATE_GAME, new CommandArgument[]{new CommandArgument("name", gameName)}));
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.WAIT_FOR_PLAYERS);
    }

    @FXML
    private void backBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }

}
