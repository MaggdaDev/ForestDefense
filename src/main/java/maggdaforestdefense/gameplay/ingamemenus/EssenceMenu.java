/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.scene.layout.BorderPane;

/**
 *
 * @author David
 */
public class EssenceMenu extends BorderPane{
    
    
    
    
    public EssenceMenu () {
        
        
        maggdaforestdefense.MaggdaForestDefense.getInstance().addOnSceneResize(((observable, oldValue, newValue) -> {
            refreshSize();
        }));
    }
    
    public void refreshSize() {
        setPrefHeight(maggdaforestdefense.MaggdaForestDefense.getWindowHeight());
   
    }
}
