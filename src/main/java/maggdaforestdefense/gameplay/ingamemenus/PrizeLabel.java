/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class PrizeLabel extends HBox{
    public final static double COIN_ICON_SIZE = 20;
    public final static Font FONT = new Font(20);
    public PrizeLabel(int prize) {
        ImageView coinIcon = new ImageView(GameImage.COIN_ICON.getImage());
        coinIcon.setFitHeight(COIN_ICON_SIZE);
        coinIcon.setFitWidth(COIN_ICON_SIZE);
        
        Label prizeLabel = new Label(String.valueOf(prize));
        prizeLabel.setFont(FONT);
        
        setSpacing(5);
        setAlignment(Pos.CENTER);
        
        getChildren().addAll(coinIcon, prizeLabel);
        
    }
    
  
}
