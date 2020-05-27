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
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class SideMenu extends GridPane{
    protected VBox content;
    protected Button expandButton;
    protected ImageView expandIcon;
    public SideMenu() {
        expandIcon = new ImageView(GameImage.MENUICON_EXPAND.getImage());
        expandIcon.setFitWidth(20);
        expandIcon.setFitHeight(20);
        setShowingLeft(false);
        
        ImageView test = new ImageView(GameImage.TOWER_SPRUCE_1.getImage());
        content = new VBox(test);
        
        setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.DASHED, new CornerRadii(0,10,10,0,false), new BorderWidths(3))));
        setPrefHeight(500);
        setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(0,10,10,0,false), new Insets(3))));
        
        expandButton = new Button("", expandIcon);
        show();
        
        add(content, 0 ,0);
        add(expandButton, 1, 0);
        setAlignment(Pos.CENTER);
        
        
        
        
    }
    
   
    
    protected void hide() {
        setLayoutX(getWidth() * -1 + expandButton.getWidth());
        setShowingLeft(false);
        expandButton.setOnAction((ActionEvent e)->{
            show();
        });
    }
    
    protected void show() {
        setLayoutX(0);
        setShowingLeft(true);
        expandButton.setOnAction((ActionEvent e)->{
            hide();
        });
    }
    
    protected void setShowingLeft(boolean left) {
        if(!left) {
            expandIcon.setRotate(270);
        } else {
            expandIcon.setRotate(90);
        }
    }
}
