/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.auth.AuthenticationManager;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.config.Version;
import maggdaforestdefense.network.client.NetworkManager;

public class MainMenu {

    @FXML private Button playBtn;

    @FXML private Button userBtn;
    @FXML private Button mapEditorBtn;
    @FXML private Text userNameTxt;
    @FXML private Text emailTxt;
    @FXML private Text gameVersion;

    
   

    @FXML public void initialize() {
        
        emailTxt.setFont(new Font(emailTxt.getFont().getName(), emailTxt.getFont().getSize()*0.8));
        gameVersion.setText(Version.getVersion());
        gameVersion.setFont(new Font(gameVersion.getFont().getName(), gameVersion.getFont().getSize()*0.75));
        updateUserInfo();
    }

    @FXML private void playBtnOnClick(ActionEvent e) {
        //Game.createGame();
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }

    public void updateUserInfo() {
        userNameTxt.setText(AuthenticationManager.getInstance().getUser().getUsername());
        if (AuthenticationManager.getInstance().getUser().getEmail()==null) {
            emailTxt.setText("");
        } else {
            emailTxt.setText(AuthenticationManager.getInstance().getUser().getEmail());
        }
        if(AuthenticationManager.getInstance().getUser().isSignedIn()) {
            userBtn.setText("Sign out");
        } else {
            userBtn.setText("Sign in");
        }
    }

    @FXML private void userBtnOnClick(ActionEvent e) {
        if(AuthenticationManager.getInstance().getUser().isSignedIn()) {
            AuthenticationManager.getInstance().signOut();
            updateUserInfo();
        } else {
            AuthenticationManager.getInstance().signIn();
            updateUserInfo();
        }
        NetworkManager.getInstance().reset();
    }

    @FXML private void mapEditorBtnOnClick(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAP_EDITOR);
    }

}
