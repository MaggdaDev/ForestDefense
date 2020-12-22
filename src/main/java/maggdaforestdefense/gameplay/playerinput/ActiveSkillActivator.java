/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.util.Duration;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.NodeSizer;

/**
 *
 * @author DavidPrivat
 */
public class ActiveSkillActivator extends ImageView {

    private EventHandler<MouseEvent> onClicked;
    private final static double SIZE = 20;

    private ColorAdjust colorAdjust;

    private boolean onCooldown, usable;

    private EventHandler<MouseEvent> onUse;

    private CooldownIndicator cooldownIndicator;

    public ActiveSkillActivator(GameImage image) {

        super(image.getImage());

        onCooldown = false;

        setFitWidth(SIZE);
        setFitHeight(SIZE);

        colorAdjust = new ColorAdjust();
        setEffect(colorAdjust);

        cooldownIndicator = new CooldownIndicator();
        NodeSizer.bindRegionToImageView(cooldownIndicator, this);
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(cooldownIndicator);

        setOnMouseClicked((MouseEvent e) -> {

            if (onUse != null && usable) {
                onUse.handle(e);
            }
        });

        setOnMouseEntered((MouseEvent e) -> {
            ScaleTransition trans = new ScaleTransition(Duration.seconds(0.1), this);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setToX(2);
            trans.setToY(2);
            trans.play();

            ScaleTransition trans2 = new ScaleTransition(Duration.seconds(0.1), cooldownIndicator);
            trans2.setInterpolator(Interpolator.EASE_BOTH);
            trans2.setToX(2);
            trans2.setToY(2);
            trans2.play();
            
            cooldownIndicator.setOpacity(0.7);

        });
        setOnMouseExited((MouseEvent e) -> {
            ScaleTransition trans = new ScaleTransition(Duration.seconds(0.1), this);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setToX(1);
            trans.setToY(1);
            trans.play();

            ScaleTransition trans2 = new ScaleTransition(Duration.seconds(0.1), cooldownIndicator);
            trans2.setInterpolator(Interpolator.EASE_BOTH);
            trans2.setToX(1);
            trans2.setToY(1);
            trans2.play();
            
            cooldownIndicator.setOpacity(1);
        });
        setOnMousePressed((MouseEvent e) -> {
            if (usable && !onCooldown) {
                colorAdjust.setBrightness(-0.5);
            }
        });
        setOnMouseReleased((MouseEvent e) -> {
            colorAdjust.setBrightness(0);
        });

    }
    
    public void setUsable(boolean b) {
        usable = b;
        if(b) {
            colorAdjust.setContrast(0);
        } else {
            colorAdjust.setContrast(-0.6);
        }
    }

    public void updateCooldown(double restTime) {
        if(restTime <= 0) {
            endCooldown();
            return;
        }
        cooldownIndicator.updateCooldown(restTime);
        if (!onCooldown) {
            onCooldown = true;
            setUsable(false);
        }
    }

    public void endCooldown() {
        cooldownIndicator.endCooldown();
        onCooldown = false;
        setUsable(true);
    }

    public void setOnUsed(EventHandler<MouseEvent> handler) {
        onUse = handler;
    }

    public void relocateCenter(double x, double y) {
        setLayoutX(x - getFitWidth() / 2);
        setLayoutY(y - getFitHeight() / 2);
    }

    public CooldownIndicator getCooldownIndicator() {
        return cooldownIndicator;
    }
    
    public static class CooldownIndicator extends Label {

        public CooldownIndicator() {
            setFont(new Font("ARIAL BLACK", ActiveSkillActivator.SIZE/1.5));
            setVisible(false);
            setViewOrder(-1);
            setText("");
            setMouseTransparent(true);
        }

        public void updateCooldown(double restCooldown) {
            setText(String.valueOf(1+(int) restCooldown));
            setVisible(true);
        }

        public void endCooldown() {
            setVisible(false);
            setText("");
        }

    }

}
