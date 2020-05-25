/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.scene.input.MouseEvent;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.Game;

/**
 *
 * @author DavidPrivat
 */
public class PlayerInputHandler {
    private SelectionSqare selectionSquare;
    private ClientMap map;
    
    public PlayerInputHandler() {
        
        
    }
    
    public void setMap(ClientMap map) {
        this.map = map;
        selectionSquare = new SelectionSqare(map);
        
        Game.getInstance().setOnMouseMoved((MouseEvent e)->{
            //selectionSquare.updatePosition(e);
        });
    } 
}
