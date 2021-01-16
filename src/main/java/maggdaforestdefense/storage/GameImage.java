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
    TOWER_MAPLE_1("maggdaforestdefense/towers/maple1.png"),
    TOWER_MAPLE_2("maggdaforestdefense/towers/maple2.png"),
    TOWER_MAPLE_3("maggdaforestdefense/towers/maple3.png"),
    TOWER_MAPLE_4("maggdaforestdefense/towers/maple4.png"),
    TOWER_LORBEER_1("maggdaforestdefense/towers/lorbeer1.png"),
    TOWER_LORBEER_2("maggdaforestdefense/towers/lorbeer2.png"),
    TOWER_LORBEER_3("maggdaforestdefense/towers/lorbeer3.png"),
    TOWER_LORBEER_4("maggdaforestdefense/towers/lorbeer4.png"),
    TOWER_OAK_1("maggdaforestdefense/towers/oak1.png"),
    TOWER_OAK_2("maggdaforestdefense/towers/oak2.png"),
    TOWER_OAK_3("maggdaforestdefense/towers/oak3.png"),
    TOWER_OAK_4("maggdaforestdefense/towers/oak4.png"),
    

    // PROJECTILES
    PROJECTILE_SPRUCE_SHOT("maggdaforestdefense/projectiles/spruce_shot.png"),
    PROJECTILE_MAPLE_SHOT("maggdaforestdefense/projectiles/maple_shot.png"),
    PROJECTILE_LORBEER_SHOT("maggdaforestdefense/projectiles/lorbeer_shot.png"),
    // UPGRADE ICONS
    UPGRADE_SPRUCE_01_01("maggdaforestdefense/upgrade_icons/spruce/spruce_01_01.png"),
    UPGRADE_SPRUCE_01_02("maggdaforestdefense/upgrade_icons/spruce/spruce_01_02.png"),
    UPGRADE_SPRUCE_01_03("maggdaforestdefense/upgrade_icons/spruce/spruce_01_03.png"),
    UPGRADE_SPRUCE_01_04("maggdaforestdefense/upgrade_icons/spruce/spruce_01_04.png"),
    UPGRADE_SPRUCE_01_05("maggdaforestdefense/upgrade_icons/spruce/spruce_01_05.png"),
    UPGRADE_SPRUCE_01_06("maggdaforestdefense/upgrade_icons/spruce/spruce_01_06.png"),
    UPGRADE_SPRUCE_02_01("maggdaforestdefense/upgrade_icons/spruce/spruce_02_01.png"),
    UPGRADE_SPRUCE_02_02("maggdaforestdefense/upgrade_icons/spruce/spruce_02_02.png"),
    UPGRADE_SPRUCE_02_03("maggdaforestdefense/upgrade_icons/spruce/spruce_02_03.png"),
    UPGRADE_SPRUCE_02_04("maggdaforestdefense/upgrade_icons/spruce/spruce_02_04.png"),
    UPGRADE_SPRUCE_02_05("maggdaforestdefense/upgrade_icons/spruce/spruce_02_05.png"),
    UPGRADE_SPRUCE_02_06("maggdaforestdefense/upgrade_icons/spruce/spruce_02_06.png"),
    UPGRADE_SPRUCE_03_01("maggdaforestdefense/upgrade_icons/spruce/spruce_03_01.png"),
    UPGRADE_SPRUCE_03_02("maggdaforestdefense/upgrade_icons/spruce/spruce_03_02.png"),
    UPGRADE_SPRUCE_03_03("maggdaforestdefense/upgrade_icons/spruce/spruce_03_03.png"),
    UPGRADE_SPRUCE_03_04("maggdaforestdefense/upgrade_icons/spruce/spruce_03_04.png"),
    UPGRADE_SPRUCE_03_05("maggdaforestdefense/upgrade_icons/spruce/spruce_03_05.png"),
    UPGRADE_SPRUCE_03_06("maggdaforestdefense/upgrade_icons/spruce/spruce_03_06.png"),
    
    UPGRADE_MAPLE_01_01("maggdaforestdefense/upgrade_icons/maple/maple_01_01.png"),
    UPGRADE_MAPLE_01_02("maggdaforestdefense/upgrade_icons/maple/maple_01_02.png"),
    UPGRADE_MAPLE_01_03("maggdaforestdefense/upgrade_icons/maple/maple_01_03.png"),
    UPGRADE_MAPLE_02_01("maggdaforestdefense/upgrade_icons/maple/maple_02_01.png"),
    UPGRADE_MAPLE_02_02("maggdaforestdefense/upgrade_icons/maple/maple_02_02.png"),
    UPGRADE_MAPLE_02_03("maggdaforestdefense/upgrade_icons/maple/maple_02_03.png"),
    UPGRADE_MAPLE_03_01("maggdaforestdefense/upgrade_icons/maple/maple_03_01.png"),
    UPGRADE_MAPLE_03_02("maggdaforestdefense/upgrade_icons/maple/maple_03_02.png"),
    UPGRADE_MAPLE_03_03("maggdaforestdefense/upgrade_icons/maple/maple_03_03.png"),
    
    UPGRADE_LORBEER_01_01("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_01_01.png"),
    UPGRADE_LORBEER_01_02("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_01_02.png"),
    UPGRADE_LORBEER_01_03("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_01_03.png"),
    UPGRADE_LORBEER_01_04("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_01_04.png"),
    UPGRADE_LORBEER_02_01("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_02_01.png"),
    UPGRADE_LORBEER_02_02("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_02_02.png"),
    UPGRADE_LORBEER_02_03("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_02_03.png"),
    UPGRADE_LORBEER_02_04("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_02_04.png"),
    UPGRADE_LORBEER_03_01("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_03_01.png"),
    UPGRADE_LORBEER_03_02("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_03_02.png"),
    UPGRADE_LORBEER_03_03("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_03_03.png"),
    UPGRADE_LORBEER_03_04("maggdaforestdefense/upgrade_icons/lorbeer/lorbeer_03_04.png"),
    
    UPGRADE_OAK_01_01("maggdaforestdefense/upgrade_icons/oak/oak_01_01.png"),
    UPGRADE_OAK_01_02("maggdaforestdefense/upgrade_icons/oak/oak_01_02.png"),
    UPGRADE_OAK_01_03("maggdaforestdefense/upgrade_icons/oak/oak_01_03.png"),
    UPGRADE_OAK_01_04("maggdaforestdefense/upgrade_icons/oak/oak_01_04.png"),
    UPGRADE_OAK_02_01("maggdaforestdefense/upgrade_icons/oak/oak_02_01.png"),
    UPGRADE_OAK_02_02("maggdaforestdefense/upgrade_icons/oak/oak_02_02.png"),
    UPGRADE_OAK_02_03("maggdaforestdefense/upgrade_icons/oak/oak_02_03.png"),
    UPGRADE_OAK_02_04("maggdaforestdefense/upgrade_icons/oak/oak_02_04.png"),
    UPGRADE_OAK_03_01("maggdaforestdefense/upgrade_icons/oak/oak_03_01.png"),
    UPGRADE_OAK_03_02("maggdaforestdefense/upgrade_icons/oak/oak_03_02.png"),
    UPGRADE_OAK_03_03("maggdaforestdefense/upgrade_icons/oak/oak_03_03.png"),
    UPGRADE_OAK_03_04("maggdaforestdefense/upgrade_icons/oak/oak_03_04.png"),
    
    
    // MENU ICONS
    MENUICON_EXPAND("maggdaforestdefense/styles/gameicons/expand.png"),
    MENUICON_LOCK("maggdaforestdefense/styles/gameicons/lock.png"),
    MENUICON_ARROW_RIGHT("maggdaforestdefense/styles/gameicons/arrow_right.png"),
    MENUICON_ARROW_LEFT("maggdaforestdefense/styles/gameicons/arrow_left.png"),
    MENUICON_CHECK_GREEN("maggdaforestdefense/styles/gameicons/green_check.png"),
    MENUICON_NOT_AVAILABLE("maggdaforestdefense/styles/gameicons/not_available.png"),
    
    //ACTIVE ICONS
    ACTIVE_ICON_ATTACK("maggdaforestdefense/active_icons/attack.png"),
    ACTIVE_ICON_SELL("maggdaforestdefense/active_icons/sell.png"),
    ACTIVE_ICON_PRESTIGE("maggdaforestdefense/active_icons/prestige.png"),
    ACTIVE_ICON_TRADE("maggdaforestdefense/active_icons/trade.png"),
    ACTIVE_ICON_TOTAL_REGEN("maggdaforestdefense/active_icons/total_regen.png"),
    //DISPLAY 
    DISPLAY_HEALTH_BOX("maggdaforestdefense/display/health_box.png"),
    DISPLAY_HEALTH_BAR_MOB("maggdaforestdefense/display/health_bar_mob.png"),
    DISPLAY_HEALTH_BAR_TOWER("maggdaforestdefense/display/health_bar_tower.png"),
    COIN_ICON("maggdaforestdefense/display/coin_icon.png"),
    ESSENCE_ICON("maggdaforestdefense/display/essence_icon.png"),
    ESSENCE_BOX("maggdaforestdefense/display/essence_box.png"),
    ESSENCE_BAR("maggdaforestdefense/display/essence_bar.png"),
    ESSENCE_BUTTON("maggdaforestdefense/display/essence_button.png"),
    ESSENCE_ANIMATION_1("maggdaforestdefense/display/essence_animation_1.png"),
    ESSENCE_ANIMATION_2("maggdaforestdefense/display/essence_animation_2.png"),
    ESSENCE_ANIMATION_3("maggdaforestdefense/display/essence_animation_3.png"),
    ESSENCE_ANIMATION_4("maggdaforestdefense/display/essence_animation_4.png"),
    LORBEER_ICON("maggdaforestdefense/display/lorbeer_icon.png");

    private Image image;

    GameImage(String path) {
        try {
            if (!maggdaforestdefense.MaggdaForestDefense.isServer()) {
                image = new Image(GameImage.class.getClassLoader().getResource(path).toString());
            }
        } catch (Exception e) {
            image = null;
            e.printStackTrace();
        }

    }

    public Image getImage() {

        return image;
    }

}
