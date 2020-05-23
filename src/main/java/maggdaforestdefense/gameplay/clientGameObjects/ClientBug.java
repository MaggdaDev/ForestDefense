/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects;

import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientBug extends ClientGameObject{
    public static final double width = 50, height = 50;
    
    private double xPos, yPos;
    public ClientBug(int id, double x, double y) {
        super(id, GameImage.MOB_BUG, GameObjectType.BUG);
        setFitWidth(width);
        setFitHeight(height);
        setNewPos(x, y);
        
    }
    
    public final void setNewPos(double x, double y) {
        xPos = x;
        yPos = y;
        setLayoutX(x);
        setLayoutY(y);
        
    }
    
    @Override
    public void update(NetworkCommand updateCommand) {
        setNewPos(updateCommand.getNumArgument("x")-width/2, updateCommand.getNumArgument("y")-height/2);
    }
}
