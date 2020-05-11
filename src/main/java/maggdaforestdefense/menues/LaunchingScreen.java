/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import maggdaforestdefense.network.client.NetworkManager;

/**
 *
 * @author David
 */
public class LaunchingScreen extends StackPane{
    Label textLabel;
    public LaunchingScreen() {
        textLabel = new Label("Launching...");
        textLabel.setFont(new Font(30));
        getChildren().add(textLabel);
    }
    
    public void start() {
        // Connect to server
        textLabel.setText("Connecting to server...");
        NetworkManager.getInstance().connect();
        textLabel.setText("Connected!");
        
        // Finished!
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
    }
}
