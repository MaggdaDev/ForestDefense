/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ClientMap extends Group {

    private double width, height;
    private ClientMapCell[][] cells;

    public ClientMap(ClientMapCell[][] mapCellArray) {
        cells = mapCellArray;
        setManaged(false);
        width = mapCellArray.length * MapCell.CELL_SIZE;
        height = mapCellArray[0].length * MapCell.CELL_SIZE;

        for (int x = 0; x < mapCellArray.length; x++) {
            ClientMapCell[] yArray = mapCellArray[x];
            for (int y = 0; y < yArray.length; y++) {
                ClientMapCell currentCell = yArray[y];

                getChildren().add(currentCell);
            }
        }
    }

    public ClientMapCell[][] getCells() {
        return cells;
    }
    
    public int getBaseXIndex() {

        return (int)(cells.length / 2);
    }
    
    public int getBaseYIndex() {
        return (int)(cells[0].length /2);
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

}
