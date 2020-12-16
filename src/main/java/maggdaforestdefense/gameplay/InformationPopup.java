/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

/**
 *
 * @author DavidPrivat
 */
public class InformationPopup extends VBox{
    private Label textLabel;
    private Polygon triangle;
    
    public InformationPopup(String text, double x, double y) {
        
        textLabel = new Label(text);
        textLabel.setFont(new Font(11));
        textLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(10), Insets.EMPTY)));
        textLabel.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        triangle = new Polygon(0,0,20,0,10,10);
        triangle.setFill(Color.GREEN);
       
        getChildren().addAll(textLabel,triangle);
        setAlignment(Pos.CENTER);
        setSpacing(0);
        
        setOpacity(0.8);
        setMouseTransparent(true);
        
        layoutXProperty().bind(Bindings.createDoubleBinding(()->(x - getWidth()/2), widthProperty()));
        layoutYProperty().bind(Bindings.createDoubleBinding(()->(y - getHeight()), heightProperty()));
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(this);
        
        setVisible(false);
    }
}
