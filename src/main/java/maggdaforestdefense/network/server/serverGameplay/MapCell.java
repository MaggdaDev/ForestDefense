/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.RandomEvent;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class MapCell extends ImageView {

    public static final double CELL_SIZE = 100;

    private CellType cellType;
    private Image image;

    private MapCell topNeighbour, botNeighbour, rightNeighbour, leftNeighbour;
    private MapCell[] neighbours;
    private int xIndex, yIndex;
    private Map map;
    private boolean generated = false;

    public MapCell(Map map, int x, int y) {

        this.map = map;
        xIndex = x;
        yIndex = y;

        setFitWidth(CELL_SIZE);
        setFitHeight(CELL_SIZE);

        initialRandomType();

    }

    public MapCell(CellType type, Map map, int x, int y) {

        setCellType(type);
        this.map = map;
        xIndex = x;
        yIndex = y;

        setFitWidth(CELL_SIZE);
        setFitHeight(CELL_SIZE);
    }

    private final void initialRandomType() {
        Randomizer rand = new Randomizer();
        rand.addEvent(new RandomEvent(CellType.UNDEFINED.ordinal(), 60));
        rand.addEvent(new RandomEvent(CellType.DIRT.ordinal(), 2));
        rand.addEvent(new RandomEvent(CellType.SAND.ordinal(), 1));
        rand.addEvent(new RandomEvent(CellType.STONE.ordinal(), 3));
        rand.addEvent(new RandomEvent(CellType.WATER.ordinal(), 0.5));
        setUpCellTypeFromNumber(rand.throwDice());
    }

    private final void setUpCellTypeFromNumber(int number) {
        setCellType(CellType.values()[number]);

    }

    public void generate() {
        if (!generated) {
            generated = true;
            Randomizer rand = new Randomizer();
            rand.addEvent(new RandomEvent(CellType.DIRT.ordinal(), 1));
            rand.addEvent(new RandomEvent(CellType.SAND.ordinal(), 0.5));
            rand.addEvent(new RandomEvent(CellType.STONE.ordinal(), 0.5));
            rand.addEvent(new RandomEvent(CellType.WATER.ordinal(), 0.1));
            if (cellType == CellType.UNDEFINED) {
                for (MapCell currCell : neighbours) {
                    if (currCell != null && currCell.getCellType() != CellType.UNDEFINED) {
                        switch (currCell.getCellType()) {
                            case STONE:
                                rand.addEvent(new RandomEvent(CellType.STONE.ordinal(), 1));
                                break;
                            case WATER:
                                rand.addEvent(new RandomEvent(CellType.WATER.ordinal(), 20));
                                break;
                            case SAND:
                                rand.addEvent(new RandomEvent(CellType.SAND.ordinal(), 10));
                                break;
                            case DIRT:
                                rand.addEvent(new RandomEvent(CellType.DIRT.ordinal(), 10));
                                break;
                        }

                    }
                }
                 setUpCellTypeFromNumber(rand.throwDice());
            }
           

            for (MapCell currNeighbour : neighbours) {
                if (currNeighbour != null) {
                    if (cellType == CellType.BASE) {
                        currNeighbour.setCellType(cellType.DIRT);
                    }
                    currNeighbour.generate();
                }
            }
        }

    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType type) {
        cellType = type;
        if (type != CellType.UNDEFINED) {
            image = type.getImage();
            setImage(image);
        }
    }

    public void setUpNeightbours() {
        if (0 < xIndex) {
            leftNeighbour = map.getCells()[xIndex - 1][yIndex];
        }
        if (0 < yIndex) {
            topNeighbour = map.getCells()[xIndex][yIndex - 1];
        }
        if (xIndex < map.getCells().length - 1) {
            rightNeighbour = map.getCells()[xIndex + 1][yIndex];
        }
        if (yIndex < map.getCells()[xIndex].length - 1) {
            botNeighbour = map.getCells()[xIndex][yIndex + 1];
        }
        neighbours = new MapCell[]{leftNeighbour, topNeighbour, rightNeighbour, botNeighbour};
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public static enum CellType {
        UNDEFINED(null),
        WATER(GameImage.MAP_CELL_WATER),
        SAND(GameImage.MAP_CELL_SAND),
        DIRT(GameImage.MAP_CELL_DIRT),
        STONE(GameImage.MAP_CELL_STONE),
        BASE(GameImage.MAP_CELL_BASE);

        private final GameImage image;

        CellType(GameImage gameImage) {
            image = gameImage;
        }

        public Image getImage() {
            if (image != null) {
                return image.getImage();
            } else {
                return null;
            }
        }
    }
}
