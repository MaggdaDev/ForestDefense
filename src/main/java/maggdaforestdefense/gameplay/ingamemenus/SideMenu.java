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

    protected Button expandButton;
    protected ImageView expandIcon;
    private Parent content;
    public boolean shown;
    private boolean isRightSide;

    public SideMenu(boolean rightSide) {
        isRightSide = rightSide;
        expandIcon = new ImageView(GameImage.MENUICON_EXPAND.getImage());
        expandIcon.setFitWidth(EXPAND_ICON_WIDTH);
        expandIcon.setFitHeight(EXPAND_ICON_WIDTH);

        expandButton = new Button("", expandIcon);
        if (rightSide) {
            expandIcon.setRotate(90);
            setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DASHED, new CornerRadii(30, 0, 0, 30, false), new BorderWidths(3))));
            setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(30, 0, 0, 30, false), new Insets(3))));
            add(expandButton, 0, 0);
        } else {
            expandIcon.setRotate(270);
            setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DASHED, new CornerRadii(0, 30, 30, 0, false), new BorderWidths(3))));
            setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(0, 30, 30, 0, false), new Insets(3))));
            add(expandButton, 1, 0);
        }

        setPrefHeight(500);

        setHgap(20);
        setVgap(20);

        setAlignment(Pos.CENTER);

        shown = false;

        maggdaforestdefense.MaggdaForestDefense.getInstance().addOnSceneResize(((observable, oldValue, newValue) -> {
            refreshPosition();
        }));
        super.widthProperty().addListener(((observable, oldValue, newValue) -> {
            refreshPosition();
        }));
        

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
        refreshPosition();
        expandButton.setOnAction((ActionEvent e) -> {
            show();
        });
    }

    public void show() {
        shown = true;
        refreshPosition();
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

    public void refreshPosition() {
        if (shown) {

            if (isRightSide) {
                setLayoutX(maggdaforestdefense.MaggdaForestDefense.getWindowWidth() - getWidth());
            } else {
                setLayoutX(0);
            }
        } else {
            if (isRightSide) {
                setLayoutX(maggdaforestdefense.MaggdaForestDefense.getWindowWidth() - expandButton.getWidth() - 10);
            } else {
                setLayoutX(expandButton.getWidth() - getWidth());
            }

        }
        updateButtonRotate();
        setPrefHeight(maggdaforestdefense.MaggdaForestDefense.getWindowHeight());
    }
}
