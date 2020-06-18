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

/**
 *
 * @author DavidPrivat
 */
public class SideMenu extends GridPane {

    protected Button expandButton;
    protected ImageView expandIcon;
    private Parent content;
    public boolean shown;

    public SideMenu() {
        expandIcon = new ImageView(GameImage.MENUICON_EXPAND.getImage());
        expandIcon.setFitWidth(20);
        expandIcon.setFitHeight(20);
        setShowingLeft(false);


        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DASHED, new CornerRadii(30, 0, 0, 30, false), new BorderWidths(3))));
        setPrefHeight(500);
        setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(30, 0, 0, 30, false), new Insets(3))));
        setHgap(20);
        setVgap(20);

        expandButton = new Button("", expandIcon);

        add(expandButton, 0, 0);
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
       if(content != null  ) {
           if(content instanceof PlantMenu) {
               ((PlantMenu)content).updateCoins(coins);
           }
       }
    }

    public void setContent(Parent p) {
        if(content != null) {
            getChildren().remove(content);
        }
        content = p;
        add(content, 1, 0);
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

    protected void setShowingLeft(boolean left) {
        if (left) {
            expandIcon.setRotate(270);
        } else {
            expandIcon.setRotate(90);
        }
    }

    public void refreshPosition() {
        if (shown) {
            setLayoutX(maggdaforestdefense.MaggdaForestDefense.getWindowWidth() - getWidth());
            setShowingLeft(true);
        } else {
            setLayoutX(maggdaforestdefense.MaggdaForestDefense.getWindowWidth() - expandButton.getWidth());
            setShowingLeft(false);
        }
        setPrefHeight(maggdaforestdefense.MaggdaForestDefense.getWindowHeight());
    }
}
