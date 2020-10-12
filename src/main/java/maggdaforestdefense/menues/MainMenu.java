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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import javafx.scene.text.Text;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.auth.AuthWindow;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.config.Version;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.storage.Logger;

public class MainMenu {

    @FXML private Button playBtn;

    @FXML private Button logoutBtn;
    @FXML private Button settingsBtn;
    @FXML private Button mapEditorBtn;
    @FXML private Text userNameTxt;
    @FXML private Text emailTxt;
    @FXML private Text gameVersion;

    
   

    @FXML public void initialize() {
        
        userNameTxt.setText(ConfigurationManager.getConfig().getAuth().getUserName());
        if (ConfigurationManager.getConfig().getAuth().getMwUser().getEmail().equals("")) {
            emailTxt.setText("");
        } else {
            emailTxt.setText(ConfigurationManager.getConfig().getAuth().getMwUser().getEmail());
        }
        emailTxt.setFont(new Font(emailTxt.getFont().getName(), emailTxt.getFont().getSize()*0.8));
        gameVersion.setText(Version.getVersion());
        gameVersion.setFont(new Font(gameVersion.getFont().getName(), gameVersion.getFont().getSize()*0.75));
    }

    @FXML private void playBtnOnClick(ActionEvent e) {
        //Game.createGame();
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }

    @FXML private void settingsBtnOnClick(ActionEvent e) {
        AuthWindow.openBrowser(AuthWindow.SETTINGS_URL);
    }

    @FXML private void logoutBtnOnClick(ActionEvent e) {
        Configuration cfg = ConfigurationManager.getConfig();
        cfg.getAuth().setSignedIn(false);
        ConfigurationManager.setConfig(cfg);
        MaggdaForestDefense.getInstance().exit();
        MaggdaForestDefense.main(new String[]{});
    }

    @FXML private void mapEditorBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAP_EDITOR);
    }

}
