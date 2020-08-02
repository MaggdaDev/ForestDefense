/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientMob extends ClientGameObject {

    protected HealthBar healthBar;
    protected DropShadow shadow;

    protected double shadowOffsetX = 5;
    protected double shadowOffsetY = 20;
    
    protected Mob.MovementType movementType;
    
    // Shadow offsets
    public final static double SHADOW_OFFSET_X_DIG = 0;
    public final static double SHADOW_OFFSET_Y_DIG = 0;
    
    public final static double SHADOW_OFFSET_X_WALK = 3;
    public final static double SHADOW_OFFSET_Y_WALK = 9;
    
    public final static double SHADOW_OFFSET_X_FLY = 9;
    public final static double SHADOW_OFFSET_Y_FLY = 27;
    

    public ClientMob(int id, GameImage image, GameObjectType type, double x, double y, double maxHealth, Mob.MovementType movement) {
        super(id, image, type, x, y);
        movementType = movement;
        healthBar = new HealthBar(maxHealth, GameImage.DISPLAY_HEALTH_BOX, GameImage.DISPLAY_HEALTH_BAR_MOB);

        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(healthBar);

        shadow = new DropShadow();
        shadow.setOffsetX(shadowOffsetX);
        shadow.setOffsetY(shadowOffsetY);
        shadow.setColor(Color.BLACK);
        shadow.setBlurType(BlurType.GAUSSIAN);
        setEffect(shadow);
    }

    protected void updateShadow() {
        switch(movementType) {
            case DIG:
                shadowOffsetX = SHADOW_OFFSET_X_DIG;
                shadowOffsetY = SHADOW_OFFSET_Y_DIG;
                setOpacity(0.4);
                break;
            case WALK:
                shadowOffsetX = SHADOW_OFFSET_X_WALK;
                shadowOffsetY = SHADOW_OFFSET_Y_WALK;
                setOpacity(1);
                break;
            case FLY:
                shadowOffsetX = SHADOW_OFFSET_X_FLY;
                shadowOffsetY = SHADOW_OFFSET_Y_FLY;
                setOpacity(1);
                break;
        }
        
        int direction = (int) ((getRotate() / 90) + 0.5);
        switch (direction) {
            case 0: case 4:
                shadow.setOffsetX(shadowOffsetX);
                shadow.setOffsetY(shadowOffsetY);
                break;
            case 1: case -3:
                shadow.setOffsetX(shadowOffsetY);
                shadow.setOffsetY(-shadowOffsetX);
                break;
            case 2: case -2:
                shadow.setOffsetX(-shadowOffsetX);
                shadow.setOffsetY(-shadowOffsetY);
                break;
            case 3: case -1:
                shadow.setOffsetX(-shadowOffsetY);
                shadow.setOffsetY(shadowOffsetX);
                break;

        }
    }

    protected void updateHealth(double h) {
        healthBar.update(xPos + getFitWidth() / 2, yPos, h);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    @Override
    public void onRemove() {
        if (Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().contains(healthBar)) {
            Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().remove(healthBar);
        }

    }

}
