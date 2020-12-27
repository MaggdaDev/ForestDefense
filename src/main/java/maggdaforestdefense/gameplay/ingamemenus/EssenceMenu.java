/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author David
 */
public class EssenceMenu extends SideMenu {

    private ImageView essenceBar, essenceBox;
    private Group contentGroup;
    
    public static double ORIGINAL_BOX_HEIGHT = 1300, ORIGINAL_BOX_WIDTH = 133;
    private DoubleProperty boxBorder, essenceLevel, maxEssence, insets;

    public EssenceMenu() {
        super(false);
        maxEssence = new SimpleDoubleProperty(ServerGame.START_ESSENCE);
        essenceLevel = new SimpleDoubleProperty(ServerGame.START_ESSENCE);
        boxBorder = new SimpleDoubleProperty();
        insets = new SimpleDoubleProperty();

        essenceBar = new ImageView(GameImage.ESSENCE_BAR.getImage());
        essenceBox = new ImageView(GameImage.ESSENCE_BOX.getImage());

        contentGroup = new Group();
        contentGroup.getChildren().addAll(essenceBox, essenceBar);




        setContent(contentGroup);
        
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(boxBorder, 6);
        MaggdaForestDefense.bindToSizeFact(insets, 30);
        
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(essenceBox.fitHeightProperty(), ORIGINAL_BOX_HEIGHT);
        maggdaforestdefense.MaggdaForestDefense.bindToWidth(essenceBox.fitWidthProperty(), ORIGINAL_BOX_WIDTH);
        essenceBox.layoutXProperty().bind(insets);
        essenceBox.layoutYProperty().bind(insets);

        essenceBar.fitWidthProperty().bind(essenceBox.fitWidthProperty().subtract(boxBorder.multiply(2)));
        essenceBar.fitHeightProperty().bind(essenceBox.fitHeightProperty().subtract(boxBorder.multiply(2)).multiply(essenceLevel.divide(maxEssence)));
        essenceBar.layoutXProperty().bind(essenceBox.layoutXProperty().add(boxBorder));
        essenceBar.layoutYProperty().bind(essenceBox.layoutYProperty().add(essenceBox.fitHeightProperty()).subtract(essenceBar.fitHeightProperty().add(boxBorder)));
    }


    public void updateEssenceLevel(int essence, int maxEss) {
        maxEssence.set(maxEss);
        if (essence != (int)essenceLevel.get()) {
            animateEssenceLevel((int)essenceLevel.get(), essence);
        }


    }

   

    public void animateEssenceLevel(int oldVal, int newVal) {
        double newBarHeight =  (essenceBox.fitHeightProperty().get() - 2 * boxBorder.get()) * ((double) newVal / (double) maxEssence.get());
        
        if(newVal < 0) {
            newVal = 0;
        }
        
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), new KeyValue(essenceLevel, newVal, Interpolator.EASE_BOTH)));

        timeline.play();
    }

}
