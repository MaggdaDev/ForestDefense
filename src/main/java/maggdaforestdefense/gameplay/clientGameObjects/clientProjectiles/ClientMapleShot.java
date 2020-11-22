/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles;

import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientMapleShot extends ClientGameObject{
    public ClientMapleShot(int id, double x, double y) {
        super(id, GameImage.PROJECTILE_MAPLE_SHOT, GameObjectType.P_MAPLE_SHOT, x, y);
        setPreserveRatio(true);
        setFitHeight(0);
    }

    @Override
    public void update(NetworkCommand command) {
        double newX = command.getNumArgument("x");
        double newY = command.getNumArgument("y");
        double radius = command.getNumArgument("radius");
        setFitWidth(radius*2);
        
        setNewPos(newX - radius, newY - radius);
    }
    
    
    
    @Override 
    public void onRemove(){}
}
