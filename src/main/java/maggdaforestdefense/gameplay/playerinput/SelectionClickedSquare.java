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
public class SelectionClickedSquare extends Rectangle {

    private ClientMap map;
    private ClientMapCell mapCell;

    private static SelectionClickedSquare instance;

    public SelectionClickedSquare(ClientMap map) {
        instance = this;
        this.map = map;
        mapCell = map.getCells()[0][0];

        setWidth(MapCell.CELL_SIZE - 6);
        setHeight(MapCell.CELL_SIZE - 6);
        setFill(Color.TRANSPARENT);
        setStroke(Color.web("484848"));
        setStrokeWidth(6);
    }

    public void updatePosition(MouseEvent e) {
        int xIndex = (int) Math.round(e.getSceneX() / MapCell.CELL_SIZE - 1);
        int yIndex = (int) Math.round(e.getSceneY() / MapCell.CELL_SIZE - 1);
        if (xIndex >= map.getCells().length) {
            xIndex = map.getCells().length - 1;
        }
        if (yIndex >= map.getCells()[xIndex].length) {
            yIndex = map.getCells()[xIndex].length - 1;
        }
        ClientMapCell newCell = map.getCells()[xIndex][yIndex];
        if (newCell != mapCell) {
            mapCell.removeSelectionSquare();
            mapCell = newCell;
            mapCell.addSelectionSquare();
        }
    }

    public static SelectionClickedSquare getInstance() {
        return instance;
    }

    public void addToMapCell(ClientMapCell c) {
        if (mapCell.getChildren().contains(this)) {
            mapCell.getChildren().remove(this);
        }
        mapCell = c;
        mapCell.getChildren().add(this);
    }
}
