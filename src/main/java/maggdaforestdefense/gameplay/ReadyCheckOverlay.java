/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ReadyCheckOverlay extends InformationOverlay {

    private ToggleButton readyButton;
    private final static double DISTANCE_TO_SCREEN_BOT = 200;
    private ProgressBar readyProgress;
    private Label progressLabel;

    private boolean animationRunning = false;

    public ReadyCheckOverlay() {
        readyButton = new ToggleButton("START WAVE!");
        readyButton.setOnAction((ActionEvent e) -> {
            sendReadyCheck();

        });
        
        progressLabel = new Label("Players ready:");
        readyProgress = new ProgressBar(0);
        

        getChildren().addAll(progressLabel, readyProgress, new Separator(), readyButton);
    }
    
    public void updateProgress(double progress) {
        readyProgress.setProgress(progress);
    }

    private void sendReadyCheck() {
        NetworkManager.getInstance().sendCommand(NetworkCommand.READY_FOR_NEXT_ROUND);
    }

    @Override
    public void startAnimation() {
        readyProgress.setProgress(0);
        animationRunning = true;
        double windowWidth = maggdaforestdefense.MaggdaForestDefense.getWindowWidth();
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();
        double thisHeight = getPrefHeight();
        double thisWidth = getPrefWidth();

        double xPos = windowWidth / 2 - thisWidth / 2;
        double yPos = windowHeight - (thisHeight + DISTANCE_TO_SCREEN_BOT);

        setTranslateX(xPos);
        setTranslateY(windowHeight);

        Path path = new Path();

        MoveTo moveTo = new MoveTo(xPos, windowHeight);
        VLineTo curve = new VLineTo(yPos);
        path.getElements().addAll(moveTo, curve);

        PathTransition trans = new PathTransition(Duration.seconds(0.5), path, this);
        setVisible(true);

        trans.setOnFinished((arg0) -> {
            animationRunning = false;
        });

        trans.play();
    }

    public void back() {
        double windowWidth = maggdaforestdefense.MaggdaForestDefense.getWindowWidth();
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();
        double thisHeight = getPrefHeight();
        double thisWidth = getPrefWidth();

        double xPos = windowWidth / 2 - thisWidth / 2;
        double yPos = windowHeight - (thisHeight + DISTANCE_TO_SCREEN_BOT);

        setTranslateX(xPos);
        setTranslateY(windowHeight);

        Path path = new Path();

        MoveTo moveTo = new MoveTo(xPos, yPos);
        VLineTo curve = new VLineTo(windowHeight);
        path.getElements().addAll(moveTo, curve);

        PathTransition trans = new PathTransition(Duration.seconds(0.5), path, this);

        trans.setOnFinished((ActionEvent e) -> {
            setVisible(false);
            readyButton.setSelected(false);
            animationRunning = false;
        });

        trans.play();
    }

    
}
