/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ReadyCheckOverlay extends ContentBox {

    private ToggleButton readyButton;
    private ProgressBar readyProgress;
    private Label progressLabel;
    
    private DoubleProperty pathPercentage, distanceToBot;

    private boolean animationRunning = false;

    public ReadyCheckOverlay() {
        readyButton = new ToggleButton("START WAVE!");
        readyButton.setOnAction((ActionEvent e) -> {
            sendReadyCheck();

        });
        
        maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.rgb(0, 102, 0, 0.8), 10, 10, 10, 10, 0);
        
        progressLabel = new Label("Players ready:");
        readyProgress = new ProgressBar(0);
        MaggdaForestDefense.bindToHeight(readyProgress.prefWidthProperty(), 200);
        
        pathPercentage = new SimpleDoubleProperty(0);
        distanceToBot = new SimpleDoubleProperty(200);
        MaggdaForestDefense.bindToHeight(distanceToBot, 200);
        
        
        getChildren().addAll(progressLabel, readyProgress, new Separator(), readyButton);
        
        translateXProperty().bind(MaggdaForestDefense.screenWidthMidProperty().subtract(widthProperty().divide(2)));
        translateYProperty().bind(MaggdaForestDefense.screenHeightProperty().subtract(pathPercentage.multiply(heightProperty().add(distanceToBot))));
    }
    
    public void updateProgress(double progress) {
        readyProgress.setProgress(progress);
    }

    private void sendReadyCheck() {
        NetworkManager.getInstance().sendCommand(NetworkCommand.READY_FOR_NEXT_ROUND);
    }


    public void startAnimation() {
        readyProgress.setProgress(0);
        animationRunning = true;
    

        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(pathPercentage, 1)));
        setVisible(true);

        timeLine.setOnFinished((arg0) -> {
            animationRunning = false;
        });

        timeLine.play();
    }

    public void back() {
        

        Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(pathPercentage, 0)));

        timeLine.setOnFinished((ActionEvent e) -> {
            setVisible(false);
            readyButton.setSelected(false);
            animationRunning = false;
        });

        timeLine.play();
    }

    
}
