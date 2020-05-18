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
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.storage.Logger;

public class MainMenu extends VBox {
    private FXMLLoader loader;

    @FXML private Button playBtn;

    @FXML private Button logoutBtn;
    @FXML private Button settingsBtn;
    @FXML private Text userNameTxt;
    @FXML private Text emailTxt;

    
    public MainMenu() {
        Logger.debugClient("FXML loading works");
    }

    public void initialize() {
        Logger.debugClient("FXML loading actually works");
        setPadding(new Insets(10, 10, 10, 10));
        userNameTxt.setText(ConfigurationManager.getConfig().getAuth().getUserName());
        if (ConfigurationManager.getConfig().getAuth().getMwUser().getEmail().equals("")) {
            emailTxt.setText("");
        } else {
            emailTxt.setText(ConfigurationManager.getConfig().getAuth().getMwUser().getEmail());
        }
        emailTxt.setFont(new Font(emailTxt.getFont().getName(), emailTxt.getFont().getSize()*0.8));
    }

    @FXML private void playBtnOnClick(ActionEvent e) {
        Game.getInstance().startGame();
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

}
