/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.scene.image.Image;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public enum GameObjectType {
    //MOBS
    M_SCHWIMMKAEFER(GameImage.MOB_BLATTLAUS_1),
    M_WASSERLAEUFER(GameImage.MOB_BLATTLAUS_1),
    M_HIRSCHKAEFER(GameImage.MOB_HIRSCHKAEFER_1),
    M_BORKENKAEFER(GameImage.MOB_BORKENKAEFER_1),
    M_WANDERLAUFER(GameImage.MOB_LAUFKAEFER_1),
    M_BLATTLAUS(GameImage.MOB_BLATTLAUS_1),
    M_MARIENKAEFER(GameImage.MOB_MARIENKAEFER_1),
    M_BOSS_CATERPILLAR(GameImage.MOB_BOSS_CATERPILLAR_1),
    M_BOSS_CATERPILLAR_SEGMENT(GameImage.MOB_BOSS_CATERPILLAR_2),
    
    //TOWERS
    T_SPRUCE(GameImage.TOWER_SPRUCE_1),
    T_MAPLE(GameImage.TOWER_MAPLE_1),
    T_LORBEER(GameImage.TOWER_LORBEER_1),
    T_OAK(GameImage.TOWER_OAK_1),
    //PROJECTILES
    P_SPRUCE_SHOT(GameImage.PROJECTILE_SPRUCE_SHOT),
    P_MAPLE_SHOT(GameImage.PROJECTILE_MAPLE_SHOT);
    
    private final GameImage gameImage;
    GameObjectType(GameImage gameImage) {
        this.gameImage = gameImage;
    }
    
    public Image getImage() {
        return gameImage.getImage();
    }
    
    public GameImage getGameImage() {
        return gameImage;
    }
}
