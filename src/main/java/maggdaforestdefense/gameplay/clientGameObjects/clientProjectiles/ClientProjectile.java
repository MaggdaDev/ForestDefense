/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles;

import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientProjectile extends ClientGameObject{
    
    public ClientProjectile(int id, GameImage gameImage, GameObjectType objectType, double x, double y) {
        super(id, gameImage, objectType, x, y);
        setViewOrder(ViewOrder.PROJECTILE);
        setMouseTransparent(true);
    }
    
}
