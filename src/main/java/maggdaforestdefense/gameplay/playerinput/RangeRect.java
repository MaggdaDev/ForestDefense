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
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;

import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class RangeRect extends Rectangle {

    private ClientMap clientMap;
    private ClientMapCell lastCell;

    public RangeRect(ClientMap map) {
        super(0,0,100,100);
        clientMap = map;
        setOpacity(0.3);
        setFill(Color.LIGHTGRAY);
        setStroke(Color.BLUE);
        setMouseTransparent(true);
        setVisible(false);

    }

    public void adjustRange(ClientTower tower) {
        setVisible(true);
        int xIndex = tower.getXIndex();
        int yIndex = tower.getYIndex();
        int range = tower.getRange();
        ClientMapCell newCell = clientMap.getCells()[xIndex][yIndex];


        lastCell = newCell;

        double size = (range * 2 + 1) * MapCell.CELL_SIZE;
        setWidth(size);
        setHeight(size);
        setLayoutX((xIndex*MapCell.CELL_SIZE-size/2) + 0.5 * MapCell.CELL_SIZE);
        setLayoutY((yIndex*MapCell.CELL_SIZE-size/2) + 0.5 * MapCell.CELL_SIZE);
        setViewOrder(-100);
    }


    public void remove() {
        if (lastCell.getChildren().contains(this)) {
            lastCell.getChildren().add(this);
        }
    }
}
