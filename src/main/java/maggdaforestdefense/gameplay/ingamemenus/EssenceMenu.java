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
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class EssenceMenu extends SideMenu {

    private ImageView essenceBar, essenceBox;
    private Group contentGroup;
    private int animationGoal = 0;
    public static double ORIGINAL_BOX_HEIGHT = 1200, ORIGINAL_BOX_WIDTH = 133;
    private DoubleProperty boxBorder, essenceLevel, maxEssence, insets;
    
    private Button play, fastForward;
    private ImageView playView, fastForwardView;
    private HBox playSpeedHBox;
    private VBox content;

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




        
        
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(boxBorder, 5);
        MaggdaForestDefense.bindToSizeFact(insets, 30);
        
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(essenceBox.fitHeightProperty(), ORIGINAL_BOX_HEIGHT);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(essenceBox.fitWidthProperty(), ORIGINAL_BOX_WIDTH);
        essenceBox.layoutXProperty().bind(insets);
        essenceBox.layoutYProperty().bind(insets);

        essenceBar.fitWidthProperty().bind(essenceBox.fitWidthProperty().subtract(boxBorder.multiply(2)));
        essenceBar.fitHeightProperty().bind(essenceBox.fitHeightProperty().subtract(boxBorder.multiply(2)).multiply(essenceLevel.divide(maxEssence)));

        essenceBar.layoutXProperty().bind(essenceBox.layoutXProperty().add(boxBorder));
        essenceBar.layoutYProperty().bind(essenceBox.layoutYProperty().add(essenceBox.fitHeightProperty()).subtract(essenceBar.fitHeightProperty().add(boxBorder)));
        
        // play speed
        playView = new ImageView(GameImage.DISPLAY_PLAY_BUTTON.getImage());
        fastForwardView = new ImageView(GameImage.DISPLAY_FAST_FORWARD.getImage());
        play = new Button("", playView);
        play.getStyleClass().clear();
        play.getStyleClass().add("playSpeedButton");
        fastForward = new Button("", fastForwardView);
        fastForward.getStyleClass().clear();
        fastForward.getStyleClass().add("playSpeedButton");
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(playView.fitWidthProperty(), 50);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(playView.fitHeightProperty(), 50);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(fastForwardView.fitWidthProperty(), 50);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(fastForwardView.fitHeightProperty(), 50);
        

        playSpeedHBox = new HBox(play, fastForward);
        
        
        maggdaforestdefense.MaggdaForestDefense.bindToWidth(playSpeedHBox.spacingProperty(), 40);
        
        content = new VBox(contentGroup, playSpeedHBox);
        MaggdaForestDefense.bindToHeight(content.spacingProperty(), 30);
        setContent(content);
        play.setDisable(true);
        play.setOnAction((ActionEvent e)->{
            NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.REQUEST_PLAYSPEED_CHANGE, new CommandArgument[]{new CommandArgument("speedId", 0)}));
        });
        fastForward.setOnAction((ActionEvent e)->{
            NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.REQUEST_PLAYSPEED_CHANGE, new CommandArgument[]{new CommandArgument("speedId", 1)}));
        });
    }
    
    public void notifyPlayspeedChange(NetworkCommand command) {
        switch((int)command.getNumArgument("speedId")) {
            case 0:
                play.setDisable(true);
                fastForward.setDisable(false);
                break;
            case 1: 
                play.setDisable(false);
                fastForward.setDisable(true);
                break;
        }
    }


    public void updateEssenceLevel(int essence, int maxEss) {
        
        maxEssence.set(maxEss);
        if (animationGoal != essence) {
            animateEssenceLevel(essence);
        }
        animationGoal = essence;


    }

   

    public void animateEssenceLevel(int newVal) {
        
        
        double newDouble = newVal;
        if(newVal <= 0) {
            newDouble = 0.001;
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), new KeyValue(essenceLevel, newDouble, Interpolator.EASE_BOTH)));
        timeline.play();
    }

}
