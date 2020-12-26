/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author David
 */
public class ContentBox extends VBox{
    public ContentBox() {
        setAlignment(Pos.CENTER);    
        maggdaforestdefense.MaggdaForestDefense.bindPadding(paddingProperty(), 20);
        maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.DARKGREEN, BorderStrokeStyle.SOLID, 10, 3);
        setFillWidth(true);
    }
}
