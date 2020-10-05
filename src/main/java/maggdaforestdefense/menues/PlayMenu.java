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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.auth.AuthWindow;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class PlayMenu extends VBox{
    private FXMLLoader loader;

    @FXML private Button findGameBtn;

    @FXML private Button createGameBtn;
    @FXML private Button backBtn;

    public PlayMenu() {
        Logger.debugClient("FXML loading works");
    }

    public void initialize() {
        Logger.debugClient("FXML loading actually works");
        setPadding(new Insets(10, 10, 10, 10));
      
    }

    @FXML private void findGameBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.FIND_GAME);
        Game.getInstance().requestGames();
    }

    @FXML private void backBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
    }

    @FXML private void createGameBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.CREATE_GAME);
    }

   
    
}
