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
    SPRUCE_1_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadelsturm", 50),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebenskristall", 50);

    private final Image icon;
    private final String name;
    private final int prize;

    private Upgrade(Image i, String n, int p) {
        icon = i;
        name = n;
        prize = p;
    }
    
    public Image getIcon() {
        return icon;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPrize() {
        return prize;
    }
}
