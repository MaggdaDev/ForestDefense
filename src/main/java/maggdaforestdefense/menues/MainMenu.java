/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author David
 */
public class MainMenu extends VBox{
    private Button playBt;
    
    public MainMenu() {
        playBt = new Button("PLAY");
        playBt.setFont(new Font(30));
        
        setAlignment(Pos.CENTER);
        
        getChildren().add(playBt);
    }
}
