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
public class SideMenu extends GridPane {

    public static double EXPAND_ICON_WIDTH = 20;
    public static double maxWidth = 500;
    protected Button expandButton;
    protected ImageView expandIcon;
    private Parent content;
    public boolean shown;
    private boolean isRightSide;

    public SideMenu(boolean rightSide) {
        isRightSide = rightSide;
        expandIcon = new ImageView(GameImage.MENUICON_EXPAND.getImage());
        expandIcon.setPreserveRatio(true);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(expandIcon.fitWidthProperty(), EXPAND_ICON_WIDTH);

        expandButton = new ScalingButton("", expandIcon);
        if (rightSide) {
            expandIcon.setRotate(90);
            maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.GRAY, BorderStrokeStyle.DASHED, 30, 0, 0, 30, 3);
            maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.GREEN, 30, 0, 0, 30, 3);
 
            add(expandButton, 0, 0);
        } else {
            expandIcon.setRotate(270);
            maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.GRAY, BorderStrokeStyle.DASHED, 0, 30, 30, 0, 3);
            maggdaforestdefense.MaggdaForestDefense.bindBackground(backgroundProperty(), Color.GREEN, 0, 30, 30, 0, 3);
            add(expandButton, 1, 0);
        }

       

        maggdaforestdefense.MaggdaForestDefense.bindToWidth(vgapProperty(), 20);
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(hgapProperty(), 20);
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
        if (isRightSide) {
            add(content, 1, 0);
        } else {
            add(content, 0, 0);
        }
    }

    public void hide() {
        shown = false;
        updateShowing();
        expandButton.setOnAction((ActionEvent e) -> {
            show();
        });
    }

    public void show() {
        shown = true;
        updateShowing();
        expandButton.setOnAction((ActionEvent e) -> {
            hide();
        });
    }

    public void updateButtonRotate() {
        if (shown == isRightSide) {
            expandIcon.setRotate(270);

        } else {
            expandIcon.setRotate(90);

        }
    }

    public void updateShowing() {
        if (content != null) {
            content.setVisible(shown);
        }
        updateButtonRotate();
    }
}
