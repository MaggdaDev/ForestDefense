/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class HealthBar extends Group{
    public static final double SIZE_FACTOR = 1;
    
    public static final double BOX_WIDTH = 50 * SIZE_FACTOR;
    public static final double BOX_HEIGHT = 10 * SIZE_FACTOR;
    public static final double BOX_BORDER = 2 * SIZE_FACTOR;
    
    
    
    
    private double maxHealth;
    private ImageView box, bar;
    public HealthBar(double max, GameImage boxImage, GameImage barImage) {
        maxHealth = max;
        
        box = new ImageView(boxImage.getImage());
        box.setFitWidth(BOX_WIDTH);
        box.setFitHeight(BOX_HEIGHT);
        
        bar = new ImageView(barImage.getImage());
        bar.setFitWidth(BOX_WIDTH-BOX_BORDER*2);
        bar.setFitHeight(BOX_HEIGHT-BOX_BORDER*2);
        bar.setLayoutX(BOX_BORDER);
        bar.setLayoutY(BOX_BORDER);
        
        setVisible(false);
        
        getChildren().addAll(box, bar);
    }
    
    public void update(double x, double y, double health) {
        setLayoutX(x - 0.5 * BOX_WIDTH);
        setLayoutY(y - 1.5 * BOX_HEIGHT);
        
        bar.setFitWidth((BOX_WIDTH - BOX_BORDER*2) * (health / maxHealth));
        
        setVisible(true);
    }
    
    
}
