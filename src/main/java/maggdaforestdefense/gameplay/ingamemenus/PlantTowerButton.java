/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.Exceptions;

/**
 *
 * @author DavidPrivat
 */
public class PlantTowerButton extends Button{
    
    private ImageView imageView;
    private int xIndex, yIndex;
    private GameObjectType gameObjectType;
    public PlantTowerButton(GameObjectType type, int x, int y) throws Exceptions.GameObjectNotCompatibleException {
        gameObjectType = type;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(100);
 
        xIndex = x;
        yIndex = y;
        
        setOnAction((ActionEvent e)->{
            plantTower();
        });
        
        setPrefWidth(100);
        setPrefHeight(100);
        
        
        setGraphic(imageView);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        
        getStyleClass().clear();
        getStyleClass().add("addTowerButton");
        
        switch(type) {
            case T_SPRUCE:
            imageView.setImage(GameImage.TOWER_SPRUCE_1.getImage());
            break;
            default:
                throw new Exceptions.GameObjectNotCompatibleException();

        }
        
        
        
        
    }
    
    private void plantTower() {
        NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.ADD_TOWER, new CommandArgument[]{
            new CommandArgument("x", String.valueOf(xIndex*MapCell.CELL_SIZE)),
            new CommandArgument("y", String.valueOf(yIndex*MapCell.CELL_SIZE)),
            new CommandArgument("type", String.valueOf(gameObjectType.ordinal()))}));
    }
    
   
            
            
}
