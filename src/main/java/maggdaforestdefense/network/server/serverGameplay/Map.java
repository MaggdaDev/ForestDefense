/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.util.RandomEvent;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class Map {

    public final static int MAP_SIZE = 25;

    private MapCell[][] cellArray;          //MapCell[x][y]

    //Separators
    public final static String COLLUMN_SEPARATOR = "-", CELL_SEPARATOR = ",";

    private Map() {
        cellArray = new MapCell[MAP_SIZE][MAP_SIZE];
    }

    public static Map generateMap() {
        Map returnMap = new Map();
        boolean finished = false;

        while (!finished) {
            for (int x = 0; x < returnMap.cellArray.length; x++) {
                for (int y = 0; y < returnMap.cellArray[x].length; y++) {

                    Randomizer randomizer = new Randomizer();

                    if (x > 0 && returnMap.cellArray[x - 1][y] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x - 1][y].getCellType().ordinal(), 1));
                    }
                    if (x > 0 && y > 0 && returnMap.cellArray[x - 1][y - 1] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x - 1][y - 1].getCellType().ordinal(), 1));
                    }
                    if (y > 0 && returnMap.cellArray[x][y - 1] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x][y - 1].getCellType().ordinal(), 1));
                    }
                    if (y > 0 && x < returnMap.cellArray.length - 1 && returnMap.cellArray[x + 1][y - 1] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x + 1][y - 1].getCellType().ordinal(), 1));
                    }
                    if (x < returnMap.cellArray.length - 1 && returnMap.cellArray[x + 1][y] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x + 1][y].getCellType().ordinal(), 1));
                    }
                    if (x < returnMap.cellArray.length - 1 && y < returnMap.cellArray.length - 1 && returnMap.cellArray[x + 1][y] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x + 1][y + 1].getCellType().ordinal(), 1));
                    }
                    if (y < returnMap.cellArray.length - 1 && returnMap.cellArray[x][y + 1] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x][y + 1].getCellType().ordinal(), 1));
                    }
                    if (x > 0 && y < returnMap.cellArray.length - 1 && returnMap.cellArray[x - 1][y + 1] != null) {
                        randomizer.addEvent(new RandomEvent(returnMap.cellArray[x - 1][y + 1].getCellType().ordinal(), 1));
                    }

                    int random = randomizer.throwDice();

                    returnMap.cellArray[x][y] = new MapCell(MapCell.CellType.values()[random]);

                }
            }
        }
        return returnMap;
    }

    public static Map generateMap(String mapAsString) {
        Map returnMap = new Map();
        String[] collumnArray = mapAsString.split(COLLUMN_SEPARATOR);
        for (int x = 0; x < collumnArray.length; x++) {
            String[] currCollumn = collumnArray[x].split(CELL_SEPARATOR);
            for (int y = 0; y < currCollumn.length; y++) {
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
