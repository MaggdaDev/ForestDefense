/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class PlayMenu {

    @FXML
    private Button findGameBtn;

    @FXML
    private Button createGameBtn;
    @FXML
    private Button backBtn;

    @FXML
    public void initialize() {
        Logger.debugClient("FXML loading actually works");
        

    }

    @FXML
    private void findGameBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().resetFindGameMenu();
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.FIND_GAME);
        
        NetworkManager.getInstance().sendCommand(NetworkCommand.LIST_AVAILABLE_GAMES);
    }

    @FXML
    private void backBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
    }

    @FXML
    private void createGameBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.CREATE_GAME);
    }

}
