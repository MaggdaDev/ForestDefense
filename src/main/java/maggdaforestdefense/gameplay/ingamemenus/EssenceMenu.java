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
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author David
 */
public class EssenceMenu extends SideMenu {

    private ImageView essenceBar, essenceBox;
    private Group contentGroup;
    private int maxEssence, essenceLevel;

    private double essenceBoxX, essenceBoxY;
    public static double ORIGINAL_BOX_HEIGHT = 1300, ORIGINAL_BOX_WIDTH = 133, ORIGINAL_BOX_BORDER = 6, SPACING = 150;

    public EssenceMenu() {
        super(false);
        maxEssence = ServerGame.START_ESSENCE;
        essenceLevel = ServerGame.START_ESSENCE;

        essenceBar = new ImageView(GameImage.ESSENCE_BAR.getImage());
        essenceBox = new ImageView(GameImage.ESSENCE_BOX.getImage());
        essenceBox.setPreserveRatio(true);
        contentGroup = new Group();
        contentGroup.getChildren().addAll(essenceBox, essenceBar);

        essenceBoxX = 200;
        essenceBoxY = 100;

        show();

        setContent(contentGroup);

    }

    @Override
    public void refreshPosition() {
        super.refreshPosition();
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();

        double newBoxHeight = windowHeight - (2 * SPACING + essenceBoxY);

        double ratio = newBoxHeight / ORIGINAL_BOX_HEIGHT;
        double newBoxWidth = ORIGINAL_BOX_WIDTH * ratio;
        double newBorder = ORIGINAL_BOX_BORDER * ratio;
        double newBarHeight = (newBoxHeight - 2 * newBorder) * ((double) essenceLevel / (double) maxEssence);

        essenceBox.setLayoutY(essenceBoxY + SPACING);
        essenceBox.setLayoutX(essenceBoxX);
        essenceBar.setLayoutY(essenceBoxY + SPACING + newBoxHeight - (newBarHeight + newBorder));
        essenceBar.setLayoutX(essenceBoxX + newBorder);

        essenceBox.setFitHeight(newBoxHeight);
        essenceBox.setFitWidth(newBoxWidth);
        essenceBar.setFitHeight(newBarHeight);
        essenceBar.setFitWidth(newBoxWidth - 2 * newBorder);

    }

    public void updateEssenceLevel(int essence, int maxEss) {
        if (essence != essenceLevel) {
            animateEssenceLevel(essenceLevel, essence);
        }
        essenceLevel = essence;
        maxEssence = maxEss;

    }

   

    public void animateEssenceLevel(int oldVal, int newVal) {
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();
        double newBoxHeight = windowHeight - (2 * SPACING + essenceBoxY);
        double ratio = newBoxHeight / ORIGINAL_BOX_HEIGHT;
        double newBorder = ORIGINAL_BOX_BORDER * ratio;
        double newBarHeight =  (newBoxHeight - 2 * newBorder) * ((double) newVal / (double) maxEssence);
        double newYPos =  essenceBoxY + SPACING + newBoxHeight - (newBarHeight + newBorder);
        
        if(newVal <= 0) {
            newBarHeight = 1;
        }
        
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), new KeyValue(essenceBar.fitHeightProperty(), newBarHeight, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(0.4), new KeyValue(essenceBar.layoutYProperty(), newYPos, Interpolator.EASE_BOTH)));
        timeline.play();
    }

}
