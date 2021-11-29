/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects;

import java.util.Vector;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.GameMaths;
import maggdaforestdefense.util.Handler;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientGameObject extends ImageView {

    protected final int id;
    private final GameObjectType gameObjectType;

    protected double xPos, yPos;
    
    protected Vector<Handler> onRemove;

    protected ClientGameObject(int id, GameImage gameImage, GameObjectType objectType, double x, double y) {
        if (gameImage != null) {
            setImage(gameImage.getImage());
        }
        gameObjectType = objectType;
        this.id = id;
        setNewPos(x, y);

        setMouseTransparent(true);
        
        onRemove = new Vector<>();
    }

    public void addColoredShadow(double radius, Color c) {
        if (!(getEffect() instanceof DropShadow && ((DropShadow) getEffect()).getColor().equals(c))) {
            setEffect(new DropShadow(radius, c));
        }
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

    public void onRemove() {
        onRemove.forEach((Handler h)->{
            h.handle();
        });
    }

}
