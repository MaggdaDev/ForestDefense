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
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class SelectionSqare extends Rectangle {

    private ClientMap map;
    private ClientMapCell mapCell;
    
    private static SelectionSqare instance;

    public SelectionSqare(ClientMap map) {
        instance = this;
        this.map = map;
        mapCell = map.getCells()[0][0];

        setWidth(MapCell.CELL_SIZE-4);
        setHeight(MapCell.CELL_SIZE-4);
        setFill(Color.TRANSPARENT);
        setStroke(Color.GRAY);
        setStrokeWidth(4);
    }

    public void updatePosition(MouseEvent e) {
        int xIndex = (int) Math.round(e.getSceneX() / MapCell.CELL_SIZE -1);
        int yIndex = (int) Math.round(e.getSceneY() / MapCell.CELL_SIZE -1);
        if (xIndex >= map.getCells().length) {
            xIndex = map.getCells().length - 1;
        }
        if (yIndex >= map.getCells()[xIndex].length) {
            yIndex = map.getCells()[xIndex].length - 1;
        }
        ClientMapCell newCell = map.getCells()[xIndex][yIndex];
        if(newCell != mapCell) {
            mapCell.removeSelectionSquare();
            mapCell = newCell;
            mapCell.addSelectionSquare();
        }        
    }
    
    public static SelectionSqare getInstance() {
        return instance;
    }
}
