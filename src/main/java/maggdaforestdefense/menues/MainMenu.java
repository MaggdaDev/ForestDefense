/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import maggdaforestdefense.gameplay.Game;

/**
 *
 * @author David
 */
public class MainMenu extends VBox{
    private Button playBt;
    
    public MainMenu() {
        playBt = new Button("PLAY");
        playBt.setFont(new Font(30));
        playBt.setOnAction((ActionEvent e)->{
            Game.getInstance().startGame();
        });
        
        setAlignment(Pos.CENTER);
        
        getChildren().add(playBt);
    }
}
