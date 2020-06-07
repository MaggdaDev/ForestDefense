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
public enum Upgrade {

    // SPRUCE
    SPRUCE_1_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Adlerauge");

    private final Image icon;
    private final String name;

    private Upgrade(Image i, String n) {
        icon = i;
        name = n;
    }
    
    public Image getIcon() {
        return icon;
    }
    
    public String getName() {
        return name;
    }
}
