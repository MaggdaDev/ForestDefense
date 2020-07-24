package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.menues.MapEditor;
import maggdaforestdefense.storage.Logger;

import java.util.Arrays;

public class GenerateableMap {
    // Some static stuff
    public static final int CURRENT_VERSION = 1;

    // Vars

    /**
     * The version of the map file. Use only if previous versions are possibly incompatible to the new one.
     */
    private int version;

    private int sizeX;
    private int sizeY;

    private PresetCell[][] presetCells;

    // New

    public GenerateableMap() {
        version = CURRENT_VERSION;
        setSizeX(10);
        setSizeY(10);
        setPresetCells(new PresetCellInterface[10][10]);
    }

    // Getter and Setter

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSizeX() {
        return sizeX;
    }

    public GenerateableMap setSizeX(int sizeX) {
        this.sizeX = sizeX;
        return this;
    }

    public int getSizeY() {
        return sizeY;
    }

    public GenerateableMap setSizeY(int sizeY) {
        this.sizeY = sizeY;
        return this;
    }

    public PresetCellInterface[][] getPresetCells() {
        return presetCells;
    }

    public GenerateableMap setPresetCells(PresetCellInterface[][] presetCellss) {
        this.presetCells = new PresetCell[presetCellss.length][presetCellss[0].length];
        for (int x = 0; x<presetCellss.length; x++) {
            this.presetCells[x] = new PresetCell[presetCellss[x].length];
            for (int y = 0; y<presetCellss[x].length; y++) {
                if(presetCellss[x][y] == null) {
                    this.presetCells[x][y] = null;
                } else {
                    this.presetCells[x][y] = new PresetCell(presetCellss[x][y]);
                }
            }
        }
        return this;
    }

    // Subclasses

    public static class PresetCell implements PresetCellInterface {
        private MapCell.CellType type;
        private int posX, posY;

        public PresetCell(int posX, int posY) {
            type = MapCell.CellType.UNDEFINED;
            this.posX = posX;
            this.posY = posY;
        }

        public PresetCell(PresetCellInterface cell) {
            this.type = cell.getType();
            this.posX = cell.getPosX();
            this.posY = cell.getPosY();
        }

        public MapCell.CellType getType() {
            return type;
        }

        public void setType(MapCell.CellType type) {
            this.type = type;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }
    }

    public interface PresetCellInterface {
        MapCell.CellType type = MapCell.CellType.UNDEFINED;
        int posX = 0, posY = 0;

        MapCell.CellType getType();
        void setType(MapCell.CellType type);
        int getPosX();
        void setPosX(int posX);
        int getPosY();
        void setPosY(int posY);
    }
}
