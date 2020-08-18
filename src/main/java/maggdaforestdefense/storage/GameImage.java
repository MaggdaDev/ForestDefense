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
    
    MOB_SCHWIMMKAEFER_1("maggdaforestdefense/mobs/kaefer1.png"),
    MOB_SCHWIMMKAEFER_2("maggdaforestdefense/mobs/kaefer2.png"),
    MOB_SCHWIMMKAEFER_3("maggdaforestdefense/mobs/kaefer3.png"),
    MOB_SCHWIMMKAEFER_4("maggdaforestdefense/mobs/kaefer4.png"),
    MOB_SCHWIMMKAEFER_5("maggdaforestdefense/mobs/kaefer5.png"),
    MOB_SCHWIMMKAEFER_6("maggdaforestdefense/mobs/kaefer6.png"),
    MOB_SCHWIMMKAEFER_7("maggdaforestdefense/mobs/kaefer7.png"),
    
    MOB_HIRSCHKAEFER_1("maggdaforestdefense/mobs/hirschkaefer1.png"),
    MOB_HIRSCHKAEFER_2("maggdaforestdefense/mobs/hirschkaefer2.png"),
    MOB_HIRSCHKAEFER_3("maggdaforestdefense/mobs/hirschkaefer3.png"),
    
    MOB_LAUFKAEFER_1("maggdaforestdefense/mobs/laufkaefer1.png"),
    MOB_LAUFKAEFER_2("maggdaforestdefense/mobs/laufkaefer2.png"),
    MOB_LAUFKAEFER_3("maggdaforestdefense/mobs/laufkaefer3.png"),
    
    MOB_BLATTLAUS_1("maggdaforestdefense/mobs/blattlaus1.png"),
    MOB_BLATTLAUS_2("maggdaforestdefense/mobs/blattlaus2.png"),
    MOB_BLATTLAUS_3("maggdaforestdefense/mobs/blattlaus3.png"),

    
    
    // TOWERS
    TOWERGROWING_ANIMATION_1("maggdaforestdefense/towers/treeGrowing1.png"),
    TOWERGROWING_ANIMATION_2("maggdaforestdefense/towers/treeGrowing2.png"),
    TOWERGROWING_ANIMATION_3("maggdaforestdefense/towers/treeGrowing3.png"),
    TOWERGROWING_ANIMATION_4("maggdaforestdefense/towers/treeGrowing4.png"),
    TOWERGROWING_ANIMATION_5("maggdaforestdefense/towers/treeGrowing5.png"),
    TOWERGROWING_ANIMATION_6("maggdaforestdefense/towers/treeGrowing6.png"),
    TOWERGROWING_ANIMATION_7("maggdaforestdefense/towers/treeGrowing7.png"),
    TOWERGROWING_ANIMATION_8("maggdaforestdefense/towers/treeGrowing8.png"),
    
    TOWER_SPRUCE_1("maggdaforestdefense/towers/spruce1.png"),
    TOWER_SPRUCE_2("maggdaforestdefense/towers/spruce2.png"),
    TOWER_SPRUCE_3("maggdaforestdefense/towers/spruce3.png"),
    TOWER_SPRUCE_4("maggdaforestdefense/towers/spruce4.png"),
    
    // PROJECTILES
    PROJECTILE_SPRUCE_SHOT("maggdaforestdefense/projectiles/spruce_shot.png"),
    
    // UPGRADE ICONS
    UPGRADE_SPRUCE_01_01("maggdaforestdefense/upgrade_icons/spruce_01_01.png"),
    UPGRADE_SPRUCE_01_06("maggdaforestdefense/upgrade_icons/spruce_01_06.png"),
    
    // MENU ICONS
    MENUICON_EXPAND("maggdaforestdefense/styles/gameicons/expand.png"),
    MENUICON_LOCK("maggdaforestdefense/styles/gameicons/lock.png"),
    MENUICON_ARROW_RIGHT("maggdaforestdefense/styles/gameicons/arrow_right.png"),
    MENUICON_ARROW_LEFT("maggdaforestdefense/styles/gameicons/arrow_left.png"),
    MENUICON_CHECK_GREEN("maggdaforestdefense/styles/gameicons/green_check.png"),
    MENUICON_NOT_AVAILABLE("maggdaforestdefense/styles/gameicons/not_available.png"),
    
    //DISPLAY 
    DISPLAY_HEALTH_BOX("maggdaforestdefense/display/health_box.png"),
    DISPLAY_HEALTH_BAR_MOB("maggdaforestdefense/display/health_bar_mob.png"),
    DISPLAY_HEALTH_BAR_TOWER("maggdaforestdefense/display/health_bar_tower.png"),
    COIN_ICON("maggdaforestdefense/display/coin_icon.png"),
    ESSENCE_ICON("maggdaforestdefense/display/essence_icon.png"),
    
    ESSENCE_ANIMATION_1("maggdaforestdefense/display/essence_animation_1.png"),
    ESSENCE_ANIMATION_2("maggdaforestdefense/display/essence_animation_2.png"),
    ESSENCE_ANIMATION_3("maggdaforestdefense/display/essence_animation_3.png"),
    ESSENCE_ANIMATION_4("maggdaforestdefense/display/essence_animation_4.png");

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
