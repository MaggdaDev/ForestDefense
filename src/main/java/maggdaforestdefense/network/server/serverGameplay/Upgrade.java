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
    SPRUCE_1_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Schnellfeuer", 50),
    SPRUCE_1_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Schwerere Nadeln", 50),
    SPRUCE_1_4(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Adlerauge", 50),
    SPRUCE_1_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Metallnadeln", 50),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebenskristall", 50),
    SPRUCE_2_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadel Teilung", 150),
    SPRUCE_2_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichten-Monokultur", 150),
    SPRUCE_2_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Rüstungsdurchdringende Nadeln", 150),
    SPRUCE_2_4(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Höher wachsen", 150),
    SPRUCE_2_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadelstärkung", 150),
    SPRUCE_2_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Lebensraub", 150),
    SPRUCE_3_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Aufruestung", 400),
    SPRUCE_3_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichten-Wut", 400),
    SPRUCE_3_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Kritische Nadeln", 400),
    SPRUCE_3_4(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Wurzelhieb", 400),
    SPRUCE_3_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Erbarmungslose Fichte", 400),
    SPRUCE_3_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichtenfreundschaft", 400),
    SPRUCE_4_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Serienmörder", 1000),
    SPRUCE_4_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Rasende Fichte", 1000),
    SPRUCE_4_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Riesenschreck", 1000),
    SPRUCE_4_4(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Exekutierer", 1000),
    SPRUCE_4_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Dominierende Nadeln", 1000),
    SPRUCE_4_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Aufruhr der Fichten", 1000);


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
