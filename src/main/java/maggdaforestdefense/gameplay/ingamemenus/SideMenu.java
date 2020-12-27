/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.NodeSizer;

/**
 *
 * @author DavidPrivat
 */
public class SideMenu extends StackPane {

    public static double EXPAND_ICON_WIDTH = 20;
    public static double maxWidth = 500, prefWidth = 400;
    private Parent content;
    public boolean shown;
    private boolean isRightSide;

    public SideMenu(boolean rightSide) {
        isRightSide = rightSide;
        



        if (rightSide) {
          
            maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.GRAY, BorderStrokeStyle.SOLID, 30, 0, 0, 30, 3);
            maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.GREEN, 30, 0, 0, 30, 3);
            MaggdaForestDefense.bindToWidth(prefWidthProperty(), prefWidth);
        } else {
            maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.GRAY, BorderStrokeStyle.SOLID, 0, 30, 30, 0, 3);
            maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.GREEN, 0, 30, 30, 0, 3);
        }

       


        MaggdaForestDefense.bindToWidth(maxWidthProperty(), maxWidth);
        setAlignment(Pos.CENTER);


        
        shown = false;

        /*maggdaforestdefense.MaggdaForestDefense.getInstance().addOnSceneResize(((observable, oldValue, newValue) -> {
            refreshPosition();
        }));
        super.widthProperty().addListener(((observable, oldValue, newValue) -> {
            refreshPosition();
        }));
         */
    }

    public void updateCoins(double coins) {
        if (content != null) {
            if (content instanceof PlantMenu) {
                ((PlantMenu) content).updateCoins(coins);
            } else if (content instanceof UpgradeMenu) {
                ((UpgradeMenu) content).updateCoins(coins);
            }
        }
    }

    public void setContent(Parent p) {
        if (content != null) {
            getChildren().remove(content);
        }
        content = p;
        getChildren().add(p);
    }

 
}
