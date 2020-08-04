/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import java.util.Vector;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.Path;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathCell;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathFinder;
import maggdaforestdefense.util.RandomEvent;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class Map {

    public final static int MAP_SIZE = 25;

    private MapCell[][] cellArray;          //MapCell[x][y]
    
    private Base base;

    //Separators
    public final static String COLLUMN_SEPARATOR = "-", CELL_SEPARATOR = ",";

    private Map() {
        cellArray = new MapCell[MAP_SIZE][MAP_SIZE];
    }
    
    public PathCell[][] toPathCellMap() {
        PathCell[][] retMap = new PathCell[cellArray.length][cellArray[0].length];
        for(int x = 0; x < cellArray.length; x++) {
            for(int y = 0; y < cellArray[x].length; y++) {
                retMap[x][y] = cellArray[x][y].getPathCell();
            }
        }
        return retMap;
    }

    public static Map generateMap(ServerGame serverGame) {
        Map returnMap = new Map();

        for (int x = 0; x < returnMap.cellArray.length; x++) {          // Generate all cells
            for (int y = 0; y < returnMap.cellArray[x].length; y++) {
                returnMap.cellArray[x][y] = new MapCell(returnMap, x, y);
            }
        }
        
        int midX = (int) (returnMap.cellArray.length/2);
        int midY = (int) (returnMap.cellArray[0].length/2);
        returnMap.setBase(new Base(returnMap, midX, midY, serverGame));

        for (int x = 0; x < returnMap.cellArray.length; x++) {           // setUp neighbours
            for (int y = 0; y < returnMap.cellArray[x].length; y++) {
                returnMap.cellArray[x][y].setUpNeightbours();
            }
        }

        

        returnMap.getBase().generate();
        
      

        return returnMap;
    }
    
    protected void setBase(Base base) {
        this.base = base;
        int midX = (int) (cellArray.length/2);
        int midY = (int) (cellArray[0].length/2);
        cellArray[midX][midY] = base;
    }
    
    public Base getBase() {
        return base;
    }

    public static ClientMapCell[][] stringToClientMapCells(String mapAsString) {
        ClientMapCell[][] retCells = new ClientMapCell[MAP_SIZE][MAP_SIZE];
        String[] collumnArray = mapAsString.split(COLLUMN_SEPARATOR);
        for (int x = 0; x < collumnArray.length; x++) {
            String[] currCollumn = collumnArray[x].split(CELL_SEPARATOR);
            for (int y = 0; y < currCollumn.length; y++) {
                String currCellAsString = currCollumn[y];
                int cellTypeOrdinary = Integer.parseInt(currCellAsString);
                retCells[x][y] = new ClientMapCell(MapCell.CellType.values()[cellTypeOrdinary], x, y);
            }
        }
        return retCells;
    }

    @Override
    public String toString() {
        String retString = "";
        for (MapCell[] yArray : cellArray) {
            for (MapCell currCell : yArray) {
                retString += String.valueOf(currCell.getCellType().ordinal());
                retString += CELL_SEPARATOR;
            }
            retString += COLLUMN_SEPARATOR;
        }
        return retString;
    }

    public MapCell[][] getCells() {
        return cellArray;
    }

}
