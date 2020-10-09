/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import maggdaforestdefense.storage.Logger;
import org.panda_lang.panda.framework.language.parser.implementation.general.number.NumberUtils;

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
        maggdaforestdefense.MaggdaForestDefense.launchGame(gameName);
    }

    @FXML
    private void backBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }

}
