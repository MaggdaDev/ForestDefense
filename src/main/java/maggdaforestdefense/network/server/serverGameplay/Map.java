/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

/**
 *
 * @author DavidPrivat
 */
public class Map {
    public final static int MAP_SIZE = 15;
    
    private MapCell[][] cellArray;          //MapCell[x][y]
    
    //Separators
    public final static String COLLUMN_SEPARATOR = "|", CELL_SEPARATOR = ",";
    
    private Map() {
        cellArray = new MapCell[MAP_SIZE][MAP_SIZE];
    }
    
    public static Map generateMap() {
        Map returnMap = new Map();
        
        for(int x = 0; x < returnMap.cellArray.length; x++) {
            for(int y = 0; y < returnMap.cellArray[x].length; y++) {
                int random = (int)(Math.random()*4);
                switch(random) {
                    case 0:
                        returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.STONE);
                        break;
                    case 1:
                        returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.DIRT);
                        break;
                    case 2:
                        returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.SAND);
                        break;
                    case 3:
                        returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.WATER);
                        break;
                        
                }
            }
        }
        return returnMap;
    }
    
    public static Map generateMap(String mapAsString) {
        Map returnMap = new Map();
        String[] collumnArray = mapAsString.split(COLLUMN_SEPARATOR);
        for(int x = 0; x < collumnArray.length; x++) {
            String[] currCollumn = collumnArray[x].split(CELL_SEPARATOR);
            for(int y = 0; y < currCollumn.length; y++) {
                String currCellAsString = currCollumn[y];
                int cellTypeOrdinary = Integer.parseInt(currCellAsString);
                returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.values()[cellTypeOrdinary]);
            }
        }
        return returnMap;
    }
    
    @Override
    public String toString() {
        String retString = "";
        for(MapCell[] yArray: cellArray) {
            for(MapCell currCell: yArray) {
                retString += String.valueOf(currCell.getCellType().ordinal());
                retString += CELL_SEPARATOR;
            }
            retString += COLLUMN_SEPARATOR;
        }
        return retString;
    }
    
    
}
