/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.scene.image.Image;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.LanguageFetcher;

/**
 *
 * @author DavidPrivat
 */
public enum Upgrade {

    // SPRUCESPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebenskristall", 50, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_6;})),
    SPRUCE_1_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadel Teilung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_1;})),
    SPRUCE_1_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichten-Monokultur", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_2;})),
    SPRUCE_1_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Rüstungsdurchdringende Nadeln", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_3;})),
    SPRUCE_1_4(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Höher wachsen", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_4;})),
    SPRUCE_1_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadelstärkung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_5;})),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Lebensraub", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_6;})),
    SPRUCE_2_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Aufruestung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_1;})),
    SPRUCE_2_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichten-Wut", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_2;})),
    SPRUCE_2_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Kritische Nadeln", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_3;})),
    SPRUCE_2_4(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Wurzelhieb", 400, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_4;})),
    SPRUCE_2_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Erbarmungslose Fichte", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_5;})),
    SPRUCE_2_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Fichtenfreundschaft", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_6;})),
    SPRUCE_3_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Serienmörder", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_1;})),
    SPRUCE_3_2(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Rasende Fichte", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_2;})),
    SPRUCE_3_3(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Riesenschreck", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_3;})),
    SPRUCE_3_4(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Exekutierer", 1000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_4;})),
    SPRUCE_3_5(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Dominierende Nadeln", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_5;})),
    SPRUCE_3_6(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Aufruhr der Fichten", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_6;}));


    private final Image icon;
    private final String name;
    private final int prize;
    private final LanguageFetcher languageFetcher;

    private Upgrade(Image i, String n, int p, LanguageFetcher descriptionFetcher) {
        icon = i;
        name = n;
        prize = p;
        languageFetcher = descriptionFetcher;
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
    
    public String getDescription() {
        return languageFetcher.fetchLanguage();
    }
    
   
}
