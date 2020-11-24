/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles;

import javafx.scene.image.ImageView;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;

/**
 *
 * @author DavidPrivat
 */
public class ClientSpruceShot extends ClientProjectile {
    
    public static double height = 50;
    public ClientSpruceShot(int id, double x, double y) {
        super(id, GameImage.PROJECTILE_SPRUCE_SHOT, GameObjectType.P_SPRUCE_SHOT, x, y);
        setPreserveRatio(true);
        setFitHeight(height);
    }

    @Override
    public void update(NetworkCommand command) {
        double newX = command.getNumArgument("x");
        double newY = command.getNumArgument("y");

        updateRotate(newX, newY);
        setNewPos(newX, newY);
    }
    
    
    
    @Override 
    public void onRemove(){}
}
