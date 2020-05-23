/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects;

import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientBug extends ClientGameObject{
    
    private double xPos, yPos;
    public ClientBug(int id, double x, double y) {
        super(id, GameImage.MOB_BUG);
        setNewPos(x, y);
        
    }
    
    public final void setNewPos(double x, double y) {
        xPos = x;
        yPos = y;
        setLayoutX(x);
        setLayoutY(y);
        
    }
}
