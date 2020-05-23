/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects;

import javafx.scene.image.ImageView;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientGameObject extends ImageView{
    private final int id;
    protected ClientGameObject(int id, GameImage gameImage) {
        setImage(gameImage.getImage());
        this.id = id;
    }
    
    public int getGameObjectId() {
        return id;
    }
    
 
}
