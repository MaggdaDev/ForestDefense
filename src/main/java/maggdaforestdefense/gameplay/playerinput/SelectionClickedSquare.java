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

    public static SelectionClickedSquare getInstance() {
        return instance;
    }

    public void addToMapCell(ClientMapCell c) {
        mapCell.removeSelectionClickedSquare();
        mapCell = c;
        mapCell.addSelectionClickedSquare();
    }
}
