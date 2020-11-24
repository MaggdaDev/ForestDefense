/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class SelectionSqare extends Rectangle {


    private static SelectionSqare instance;

    public SelectionSqare() {
        instance = this;
        setWidth(MapCell.CELL_SIZE);
        setHeight(MapCell.CELL_SIZE);
        setFill(Color.TRANSPARENT);
        setStroke(Color.GRAY);
        setStrokeWidth(4);
        
        setViewOrder(ViewOrder.MAP_CONTROLL);
        
        setVisible(false);
        setMouseTransparent(true);
    }

    public void updatePosition(int xIndex, int yIndex) {
        setVisible(true);
        
        setLayoutX(xIndex * MapCell.CELL_SIZE);
        setLayoutY(yIndex * MapCell.CELL_SIZE);

    }
    


    public static SelectionSqare getInstance() {
        return instance;
    }
}
