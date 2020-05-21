/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

import javafx.scene.image.Image;

/**
 *
 * @author David
 */
public enum GameImage {

    // Map Cells
    MAP_CELL_SAND("maggdaforestdefense/map_cells/sand.png"),
    MAP_CELL_WATER("maggdaforestdefense/map_cells/water.png"),
    MAP_CELL_STONE("maggdaforestdefense/map_cells/stone.png"),
    MAP_CELL_DIRT("maggdaforestdefense/map_cells/dirt.png"),
    MAP_CELL_BASE("maggdaforestdefense/map_cells/base.png");

    private Image image;
    

    GameImage(String path) {
        try {
            image = new Image(GameImage.class.getClassLoader().getResource(path).toString());
        } catch(Exception e) {
            image = null;
            e.printStackTrace();
        }
                
    }
    public Image getImage() {

        return image;
    }
}
