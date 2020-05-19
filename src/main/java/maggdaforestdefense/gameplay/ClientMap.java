/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class ClientMap extends Group{
    private double width, height;
    
    
    public ClientMap(MapCell[][] mapCellArray) {
        setManaged(false);
        width = mapCellArray.length * MapCell.CELL_SIZE;
        height = mapCellArray[0].length * MapCell.CELL_SIZE;
        
        for(int x = 0; x < mapCellArray.length; x++) {
            MapCell[] yArray = mapCellArray[x];
            for(int y = 0; y < yArray.length; y++) {
                MapCell currentCell = yArray[y];
                currentCell.setLayoutX(x*MapCell.CELL_SIZE);
                currentCell.setLayoutY(y*MapCell.CELL_SIZE);
                getChildren().add(currentCell);
            }
        }
    }
    
    public double getHeight() {
        return height;
    }
    
    public double getWidth() {
        return width;
    }
   
}
