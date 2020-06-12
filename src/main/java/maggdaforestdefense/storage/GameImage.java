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
    MAP_CELL_BASE("maggdaforestdefense/map_cells/base.png"),
    
    // Mobs
    MOB_BUG_1("maggdaforestdefense/mobs/bug1.png"),
    MOB_BUG_2("maggdaforestdefense/mobs/bug2.png"),
    MOB_BUG_3("maggdaforestdefense/mobs/bug3.png"),
    
    
    // TOWERS
    TOWER_SPRUCE_1("maggdaforestdefense/towers/spruce1.png"),
    
    // PROJECTILES
    PROJECTILE_SPRUCE_SHOT("maggdaforestdefense/projectiles/spruce_shot.png"),
    
    // UPGRADE ICONS
    UPGRADE_SPRUCE_01_01("maggdaforestdefense/upgrade_icons/spruce_01_01.png"),
    
    // MENU ICONS
    MENUICON_EXPAND("maggdaforestdefense/styles/gameicons/expand.png"),
    
    //DISPLAY 
    DISPLAY_HEALTH_BOX("maggdaforestdefense/display/health_box.png"),
    DISPLAY_HEALTH_BAR("maggdaforestdefense/display/health_bar.png"),
    COIN_ICON("maggdaforestdefense/display/coin_icon.png"),
    ESSENCE_ICON("maggdaforestdefense/display/essence_icon.png");

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
