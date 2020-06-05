/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.scene.image.Image;

/**
 *
 * @author DavidPrivat
 */
public class Upgrade {
    private final String upgradeName;
    private final Image upgradeIcon;
    public Upgrade(String name, Image icon) {
        upgradeIcon = icon;
        upgradeName = name;
    }
}
