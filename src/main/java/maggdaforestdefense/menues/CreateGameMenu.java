/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

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
public class CreateGameMenu extends VBox {

    private FXMLLoader loader;

    @FXML
    private TextField playerAmountTF;

    @FXML
    private Button okBtn;
    @FXML
    private Button backBtn;

    public CreateGameMenu() {
        Logger.debugClient("FXML loading works");
    }

    public void initialize() {
        Logger.debugClient("FXML loading actually works");
        playerAmountTF.textProperty().addListener(((obs, oldVal, newVal) -> {
            if(!NumberUtils.isNumber(newVal)) {
                playerAmountTF.setText(oldVal);
            }
        }));

    }
    
    @FXML private void okBtnOnClick(ActionEvent e) {
        
    }
    
    @FXML private void backBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }

   
}
