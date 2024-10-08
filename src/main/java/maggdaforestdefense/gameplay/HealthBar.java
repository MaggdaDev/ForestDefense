/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class HealthBar extends Group{

    

    public static final double BOX_HEIGHT_MULT = 0.2;
    public static final double BOX_BORDER_MULT = 0.02 ;
    
    private final double size;
    
    
    private double maxHealth;
    private ImageView box, bar;
    public HealthBar(double max, GameImage boxImage, GameImage barImage, double size) {
        this.size = size;
        maxHealth = max;
        
        box = new ImageView(boxImage.getImage());
        box.setFitWidth(size);
        box.setFitHeight(BOX_HEIGHT_MULT * size);
        
        bar = new ImageView(barImage.getImage());
        bar.setFitWidth((1-BOX_BORDER_MULT*2)*size + 1);
        bar.setFitHeight((BOX_HEIGHT_MULT-BOX_BORDER_MULT*2)*size + 1);
        bar.setLayoutX(BOX_BORDER_MULT*size);
        bar.setLayoutY(BOX_BORDER_MULT*size);
        
        setVisible(false);
        
        getChildren().addAll(box,bar);
        
        setViewOrder(ViewOrder.POPUP);
    }
    
    public void update(double x, double y, double health) {
        if(health <= 0) {
            health = 0.01;
        }
        setLayoutX(x - 0.5 * size);
        setLayoutY(y - 1.5 * size * BOX_HEIGHT_MULT);
       
        bar.setFitWidth((1 - BOX_BORDER_MULT*2) * size * (health / maxHealth));
        
        
        setVisible(true);
    }
    
    public void setMaxHealth(double m) {
        maxHealth = m;
    }
    
    
}
