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
    SPRUCE_1_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadel Teilung", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_1;})),
    SPRUCE_1_2(GameImage.UPGRADE_SPRUCE_01_02.getImage(), "Fichten-Monokultur", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_2;})),
    SPRUCE_1_3(GameImage.UPGRADE_SPRUCE_01_03.getImage(), "Rüstungsdurchdringende Nadeln", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_3;})),
    SPRUCE_1_4(GameImage.UPGRADE_SPRUCE_01_04.getImage(), "Höher wachsen", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_4;})),
    SPRUCE_1_5(GameImage.UPGRADE_SPRUCE_01_05.getImage(), "Nadelstärkung", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_5;})),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebensraub", 100, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_6;})),
    SPRUCE_2_1(GameImage.UPGRADE_SPRUCE_02_01.getImage(), "Aufruestung", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_1;})),
    SPRUCE_2_2(GameImage.UPGRADE_SPRUCE_02_02.getImage(), "Fichten-Wut", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_2;})),
    SPRUCE_2_3(GameImage.UPGRADE_SPRUCE_02_03.getImage(), "Kritische Nadeln", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_3;})),
    SPRUCE_2_4(GameImage.UPGRADE_SPRUCE_02_04.getImage(), "Wurzelhieb", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_4;})),
    SPRUCE_2_5(GameImage.UPGRADE_SPRUCE_02_05.getImage(), "Erbarmungslose Fichte", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_5;})),
    SPRUCE_2_6(GameImage.UPGRADE_SPRUCE_02_06.getImage(), "Fichtenfreundschaft", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_6;})),
    SPRUCE_3_1(GameImage.UPGRADE_SPRUCE_03_01.getImage(), "Serienmörder", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_1;})),
    SPRUCE_3_2(GameImage.UPGRADE_SPRUCE_03_02.getImage(), "Rasende Fichte", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_2;})),
    SPRUCE_3_3(GameImage.UPGRADE_SPRUCE_03_03.getImage(), "Riesenschreck", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_3;})),
    SPRUCE_3_4(GameImage.UPGRADE_SPRUCE_03_04.getImage(), "Fichtenforschung", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_4;})),
    SPRUCE_3_5(GameImage.UPGRADE_SPRUCE_03_05.getImage(), "Dominierende Nadeln", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_5;})),
    SPRUCE_3_6(GameImage.UPGRADE_SPRUCE_03_06.getImage(), "Aufruhr der Fichten", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_6;})),
    
    // MAPLE
    MAPLE_1_1(GameImage.UPGRADE_MAPLE_01_01.getImage(), "Ausbau", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_1;})),
    MAPLE_1_2(GameImage.UPGRADE_MAPLE_01_02.getImage(), "Bund der Ahorne", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_2;})),
    MAPLE_1_3(GameImage.UPGRADE_MAPLE_01_03.getImage(), "Tod den Gierigen", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_3;})),
    MAPLE_2_1(GameImage.UPGRADE_MAPLE_02_01.getImage(), "Eskalation", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_1;})),
    MAPLE_2_2(GameImage.UPGRADE_MAPLE_02_02.getImage(), "Aufladen", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_2;})),
    MAPLE_2_3(GameImage.UPGRADE_MAPLE_02_03.getImage(), "Erschöpfende Blätter", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_3;})),
    MAPLE_3_1(GameImage.UPGRADE_MAPLE_03_01.getImage(), "Zerschmetternde Blätter", 1500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_1;})),
    MAPLE_3_2(GameImage.UPGRADE_MAPLE_03_02.getImage(), "Gnadenlose Blätter", 1500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_2;})),
    MAPLE_3_3(GameImage.UPGRADE_MAPLE_03_03.getImage(), "Zerlegende Blätter", 1500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_3;}));


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
