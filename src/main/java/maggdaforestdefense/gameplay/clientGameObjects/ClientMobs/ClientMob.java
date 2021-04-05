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
import maggdaforestdefense.gameplay.InformationBubble;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
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
    protected double size;

    private double oldHealth;

    // Shadow offsets
    public final static double SHADOW_OFFSET_X_DIG_MULT = 0;
    public final static double SHADOW_OFFSET_Y_DIG_MULT = 0;

    public final static double SHADOW_OFFSET_X_WALK_MULT = 0.04;
    public final static double SHADOW_OFFSET_Y_WALK_MULT = 0.12;

    public final static double SHADOW_OFFSET_X_FLY_MULT = 0.18;
    public final static double SHADOW_OFFSET_Y_FLY_MULT = 0.54;

    public ClientMob(int id, GameImage image, GameObjectType type, double x, double y, double maxHealth, Mob.MovementType movement, double size) {
        super(id, image, type, x, y);
        this.size = size;
        movementType = movement;
        healthBar = new HealthBar(maxHealth, GameImage.DISPLAY_HEALTH_BOX, GameImage.DISPLAY_HEALTH_BAR_MOB, size);

        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(healthBar);

        shadow = new DropShadow();
        shadow.setOffsetX(shadowOffsetX);
        shadow.setOffsetY(shadowOffsetY);
        shadow.setColor(Color.color(0, 0, 0, 0.2));
        shadow.setBlurType(BlurType.GAUSSIAN);
        setEffect(shadow);

        double fitWidth = getImage().getWidth();
        double fitHeight = getImage().getHeight();
        setPreserveRatio(true);
        if (fitWidth > fitHeight) {
            setFitWidth(size);
            setFitHeight(fitHeight * (size / fitWidth));
        } else {
            setFitHeight(size);
            setFitWidth(fitWidth * (size / fitHeight));
        }

        setViewOrder(ViewOrder.MOB);

        oldHealth = maxHealth;
    }
    


    protected void updateShadow() {
        switch (movementType) {
            case DIG:
                shadowOffsetX = SHADOW_OFFSET_X_DIG_MULT * size;
                shadowOffsetY = SHADOW_OFFSET_Y_DIG_MULT * size;
                setOpacity(0.4);
                break;
            case WALK:
                shadowOffsetX = SHADOW_OFFSET_X_WALK_MULT * size;
                shadowOffsetY = SHADOW_OFFSET_Y_WALK_MULT * size;
                setOpacity(1);
                break;
            case FLY:
                shadowOffsetX = SHADOW_OFFSET_X_FLY_MULT * size;
                shadowOffsetY = SHADOW_OFFSET_Y_FLY_MULT * size;
                setOpacity(1);
                break;
        }

        int direction = (int) ((getRotate() / 90) + 0.5);
        
        switch (direction) {
            case 0:
            case 4:
                shadow.setOffsetX(shadowOffsetX);
                shadow.setOffsetY(shadowOffsetY);
                break;
            case 1:
            case -3:
                shadow.setOffsetX(shadowOffsetY);
                shadow.setOffsetY(-shadowOffsetX);
                break;
            case 2:
            case -2:
                shadow.setOffsetX(-shadowOffsetX);
                shadow.setOffsetY(-shadowOffsetY);
                break;
            case 3:
            case -1:
                shadow.setOffsetX(-shadowOffsetY);
                shadow.setOffsetY(shadowOffsetX);
                break;

        }
    }

    protected void handleEffects(EffectSet set) {
        if (set.isActive(EffectSet.EffectType.SENSITIVE)) {
            addColoredShadow(5, Color.YELLOW);
        } else if (set.isActive(EffectSet.EffectType.GOLDED)) {
            addColoredShadow(8, Color.ORANGE);
        } else {
            addColoredShadow(0, Color.TRANSPARENT);
        }
    }

    protected void updateHealth(double h) {

        healthBar.update(xPos + (getFitWidth() * 0.5d), yPos, h);
        if (oldHealth != h) {
            int hpDiff = (int) (h - oldHealth);
            if (hpDiff != 0) {
                Game.addGamePlayNode(new InformationBubble(String.valueOf(hpDiff), InformationBubble.InformationType.MOB_HP, xPos, yPos));
            }
        }

        oldHealth = h;

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
    
    @Override
    public void addColoredShadow(double radius, Color c) {
        if (!(getEffect() instanceof DropShadow && ((DropShadow) getEffect()).getColor().equals(c))) {
            shadow.setInput(new DropShadow(radius, c));
        }
    }

}
