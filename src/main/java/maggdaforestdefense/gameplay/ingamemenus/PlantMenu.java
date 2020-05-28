/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import maggdaforestdefense.storage.GameImage;


/**
 *
 * @author DavidPrivat
 */
public class PlantMenu extends VBox{
    
    
    public PlantMenu() {
        
        ImageView test = new ImageView(GameImage.TOWER_SPRUCE_1.getImage());
        getChildren().add(test);
    }
}
