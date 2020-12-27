/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.menues.MenuManager;

/**
 *
 * @author David
 */
public class InformationOverlay extends ContentBox {

    private DoubleProperty animationProgress;

    public InformationOverlay() {
        animationProgress = new SimpleDoubleProperty(-0.5);

        maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.rgb(0, 102, 0, 0.8), 10, 10, 10, 10, 0);
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(spacingProperty(), 30);
        
        layoutXProperty().bind(MaggdaForestDefense.screenWidthMidProperty().subtract(widthProperty().divide(2)));
        layoutYProperty().bind(animationProgress.multiply(MaggdaForestDefense.screenHeightMidProperty().subtract(heightProperty().divide(2))));

    }

    public void startAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(animationProgress, 1, Interpolator.EASE_BOTH)));
        timeline.play();

    }

    public void startAnimation(double fadeOutDelay) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(animationProgress, 1, Interpolator.EASE_BOTH)));

        timeline.setOnFinished((o) -> {
            PauseTransition trans = new PauseTransition(Duration.seconds(fadeOutDelay));
            trans.setOnFinished((p) -> {
                Timeline out = new Timeline(new KeyFrame(Duration.seconds(0.5), new KeyValue(animationProgress, -0.5, Interpolator.EASE_BOTH)));
                out.play();
            });
            trans.play();
        });

        timeline.play();
    }
}
