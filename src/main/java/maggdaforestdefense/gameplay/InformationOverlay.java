/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Font;
import javafx.util.Duration;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.menues.MenuManager;

/**
 *
 * @author David
 */
public class InformationOverlay extends ContentBox {

    public InformationOverlay() {


        setBackground(new Background(new BackgroundFill(Color.rgb(0, 102, 0, 0.8), new CornerRadii(10), Insets.EMPTY)));

        setSpacing(30);
        setVisible(false);

    }

    public void startAnimation() {
        double windowWidth = maggdaforestdefense.MaggdaForestDefense.getWindowWidth();
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();
        double thisHeight = getPrefHeight();
        double thisWidth = getPrefWidth();

        double midX = windowWidth / 2 - thisWidth / 2;
        double midY = windowHeight / 2 - thisHeight / 2;

        setTranslateX(midX);
        setTranslateY(-thisHeight);

        Path path = new Path();

        MoveTo moveTo = new MoveTo(midX, -thisHeight);
        VLineTo curve = new VLineTo(midY);
        path.getElements().addAll(moveTo, curve);

        PathTransition trans = new PathTransition(Duration.seconds(0.5), path, this);
        setVisible(true);
        

        trans.play();

    }

    public void startAnimation(double fadeOutDelay) {
        double windowWidth = maggdaforestdefense.MaggdaForestDefense.getWindowWidth();
        double windowHeight = maggdaforestdefense.MaggdaForestDefense.getWindowHeight();
        double thisHeight = getPrefHeight();
        double thisWidth = getPrefWidth();

        double midX = windowWidth / 2 - thisWidth / 2;
        double midY = windowHeight / 2 - thisHeight / 2;

        setTranslateX(midX);
        setTranslateY(-thisHeight);

        Path path = new Path();

        MoveTo moveTo = new MoveTo(midX, -thisHeight);
        VLineTo curve = new VLineTo(midY);
        path.getElements().addAll(moveTo, curve);

        PathTransition trans = new PathTransition(Duration.seconds(0.5), path, this);
        setVisible(true);
        trans.play();
        trans.setOnFinished((ActionEvent e) -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(fadeOutDelay));
            pause.setOnFinished((ActionEvent ev) -> {
                Path pathBack = new Path();

                MoveTo moveToBack = new MoveTo(midX, midY);
                VLineTo curveBack = new VLineTo(-thisHeight);
                pathBack.getElements().addAll(moveToBack, curveBack);

                PathTransition transBack = new PathTransition(Duration.seconds(0.5), pathBack, this);
                transBack.setOnFinished((ActionEvent eve)->{
                    setVisible(false);
                });
                transBack.play();
                
            });
            pause.play();
        });

    }
}
