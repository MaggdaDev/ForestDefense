/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maggdaforestdefense.MaggdaForestDefense;

/**
 *
 * @author DavidPrivat
 */
public class ScalingButton extends Button {

    public ScalingButton(String s, Node content) {
        super(s, content);
        if (s.isEmpty()) {
            setFont(new Font(0));
            if (content != null) {
                
                maxWidthProperty().bind(Bindings.createDoubleBinding(() -> (content.getBoundsInParent().getWidth()), content.boundsInLocalProperty()));
                maxHeightProperty().bind(Bindings.createDoubleBinding(() -> (content.getBoundsInParent().getHeight()), content.boundsInLocalProperty()));

            }

        }


        getStyleClass().clear();
        getStyleClass().add("ScalingButton");

    }

    public ScalingButton(String s) {
        this(s, null);

    }
}
