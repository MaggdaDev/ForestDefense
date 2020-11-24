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
public class SelectionClickedSquare extends Rectangle {


    private static SelectionClickedSquare instance;
    private int currentXInd, currentYInd;
    

    public SelectionClickedSquare() {
        instance = this;


        setWidth(MapCell.CELL_SIZE);
        setHeight(MapCell.CELL_SIZE);
        setFill(Color.TRANSPARENT);
        setStroke(Color.web("484848"));
        setStrokeWidth(6);
        
        setViewOrder(ViewOrder.MAP_CONTROLL);
        setVisible(false);
        setMouseTransparent(true);
    }

    public static SelectionClickedSquare getInstance() {
        return instance;
    }

   public void updatePosition(int xIndex, int yIndex) {
        setVisible(true);
        currentXInd = xIndex;
        currentYInd = yIndex;
        
        setLayoutX(xIndex * MapCell.CELL_SIZE);
        setLayoutY(yIndex * MapCell.CELL_SIZE);

    }
   
   public boolean isIndexClicked(int xIndex, int yIndex) {
        return (xIndex == currentXInd && yIndex == currentYInd);
    }
    
}
