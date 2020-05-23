/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects;

import javafx.scene.image.ImageView;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientGameObject extends ImageView{
    private final int id;
    private final GameObjectType gameObjectType;
    protected ClientGameObject(int id, GameImage gameImage, GameObjectType objectType) {
        setImage(gameImage.getImage());
        gameObjectType = objectType;
        this.id = id;
    }
    
    public abstract void update(NetworkCommand updateCommand);
    
    public GameObjectType getType() {
        return gameObjectType;
    }
    
    public int getGameObjectId() {
        return id;
    }
    
 
}
