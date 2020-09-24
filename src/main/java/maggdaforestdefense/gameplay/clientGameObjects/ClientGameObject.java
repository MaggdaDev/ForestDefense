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
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientGameObject extends ImageView {

    protected final int id;
    private final GameObjectType gameObjectType;

    protected double xPos, yPos;

    protected ClientGameObject(int id, GameImage gameImage, GameObjectType objectType, double x, double y) {
        setImage(gameImage.getImage());
        gameObjectType = objectType;
        this.id = id;
        xPos = x;
        yPos = y;
        
        setMouseTransparent(true);
    }

    public abstract void update(NetworkCommand updateCommand);

    public GameObjectType getType() {
        return gameObjectType;
    }

    public int getGameObjectId() {
        return id;
    }

    protected void updateRotate(double newX, double newY) {
        double dX = newX - xPos;
        double dY = newY - yPos;

        setRotate(GameMaths.getDegAngleToYAxis(dX, dY));

    }
    
    public void setNewPos(double x, double y) {
        xPos = x;
        yPos = y;
        setLayoutX(x);
        setLayoutY(y);
    }
    
    public abstract void onRemove();


}
