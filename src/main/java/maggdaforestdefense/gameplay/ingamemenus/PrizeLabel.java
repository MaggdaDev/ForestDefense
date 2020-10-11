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
import javafx.scene.paint.Color;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.NodeSizer;

/**
 *
 * @author DavidPrivat
 */
public class PrizeLabel extends HBox{
    public final static double COIN_ICON_SIZE = 20;
    private Label prizeLabel;
    public PrizeLabel(int prize) {
       
        
        ImageView coinIcon = new ImageView(GameImage.COIN_ICON.getImage());
        coinIcon.setFitHeight(COIN_ICON_SIZE);
        coinIcon.setFitWidth(COIN_ICON_SIZE);
        
        prizeLabel = new Label(String.valueOf(prize));
                
        setSpacing(5);
        setAlignment(Pos.CENTER);
        
        getChildren().addAll(coinIcon, prizeLabel);
        
        
        // SIZING
        new NodeSizer<ImageView>(coinIcon, COIN_ICON_SIZE, COIN_ICON_SIZE, true);

        
        
    }
    
    public void setBuyable(boolean b) {
        if(b) {
           prizeLabel.setTextFill(Color.BLACK);
        } else {
            prizeLabel.setTextFill(Color.RED);

        }
    }
    
  
}
