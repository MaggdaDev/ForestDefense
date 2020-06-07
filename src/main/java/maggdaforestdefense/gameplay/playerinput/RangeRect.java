/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.ClientMapCell;

import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class RangeRect extends Rectangle{
    private ClientMap clientMap;
    private ClientMapCell lastCell;
    public RangeRect(ClientMap map) {
        super();
        clientMap = map;
        setOpacity(0.2);
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLUE);
       
    }
    
    public void adjustRange(int range, int xIndex, int yIndex) {
        ClientMapCell newCell = clientMap.getCells()[xIndex][yIndex];
        if((!newCell.equals(lastCell)) && lastCell != null && lastCell.getChildren().contains(this)) {
            lastCell.getChildren().remove(this);
        }
        newCell.getChildren().add(this);
        lastCell = newCell;
        
        double size = (range*2 + 1) * MapCell.CELL_SIZE;
        setWidth(size);
        setHeight(size);
    }
}
