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
    private RessourceDisplay coinsDisplay;
    private RessourceDisplay.EssenceDisplay essenceDisplay;
    
    
    public TopOverlay(int startCoins, int startEssence) {
        coinsDisplay = new RessourceDisplay(GameImage.COIN_ICON, startCoins);
        
        essenceDisplay = new RessourceDisplay.EssenceDisplay(GameImage.ESSENCE_ICON, startEssence, startEssence);
        
        maggdaforestdefense.MaggdaForestDefense.bindToWidth(spacingProperty(), 200);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20, 100, 100,100));
        
        getChildren().addAll(coinsDisplay, essenceDisplay);
    }
    
    public void updateRessourceDisplays(int coins, int essence, int maxEssence) {
        coinsDisplay.setValue(coins);
        essenceDisplay.setValue(essence);
        essenceDisplay.setMaxEssence(maxEssence);
    }
}
