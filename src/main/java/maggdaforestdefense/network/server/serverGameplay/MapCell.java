/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class MapCell extends ImageView{
    public static final double CELL_SIZE = 100;
    
    private CellType cellType;
    private Image image;
    
    public MapCell(CellType type) {
        cellType = type;
        image = cellType.getImage();
        setImage(image);
        setFitWidth(CELL_SIZE);
        setFitHeight(CELL_SIZE);

    }
    
    public CellType getCellType() {
        return cellType;
    }
    
    public static enum CellType {
        WATER(GameImage.MAP_CELL_WATER),
        SAND(GameImage.MAP_CELL_SAND),
        DIRT(GameImage.MAP_CELL_DIRT),
        STONE(GameImage.MAP_CELL_STONE);
        
        private final GameImage image;
        
        CellType(GameImage gameImage) {
            image = gameImage;
        }
        
        public Image getImage() {
            return image.getImage();
        }
    }
}
