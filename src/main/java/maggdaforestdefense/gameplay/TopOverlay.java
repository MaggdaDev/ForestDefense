/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class TopOverlay extends HBox{
    private RessourceDisplay coinsDisplay, essenceDisplay;
    
    
    public TopOverlay(int startCoins, int startEssence) {
        coinsDisplay = new RessourceDisplay(GameImage.COIN_ICON, startCoins);
        
        essenceDisplay = new RessourceDisplay(GameImage.ESSENCE_ICON, startEssence);
        
        setSpacing(200);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20, 100, 100,100));
        
        getChildren().addAll(coinsDisplay, essenceDisplay);
    }
    
    public void updateRessourceDisplays(int coins, int essence) {
        coinsDisplay.setValue(coins);
        essenceDisplay.setValue(essence);
    }
}
