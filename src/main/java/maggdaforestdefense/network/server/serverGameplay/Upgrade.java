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
    SPRUCE_1_2(GameImage.UPGRADE_SPRUCE_01_02.getImage(), "Fichten-Monokultur", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_2;})),
    SPRUCE_1_3(GameImage.UPGRADE_SPRUCE_01_03.getImage(), "R�stungsdurchdringende Nadeln", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_3;})),
    SPRUCE_1_4(GameImage.UPGRADE_SPRUCE_01_04.getImage(), "H�her wachsen", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_4;})),
    SPRUCE_1_5(GameImage.UPGRADE_SPRUCE_01_05.getImage(), "Nadelst�rkung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_5;})),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebensraub", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_6;})),
    SPRUCE_2_1(GameImage.UPGRADE_SPRUCE_02_01.getImage(), "Aufruestung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_1;})),
    SPRUCE_2_2(GameImage.UPGRADE_SPRUCE_02_02.getImage(), "Fichten-Wut", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_2;})),
    SPRUCE_2_3(GameImage.UPGRADE_SPRUCE_02_03.getImage(), "Kritische Nadeln", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_3;})),
    SPRUCE_2_4(GameImage.UPGRADE_SPRUCE_02_04.getImage(), "Wurzelhieb", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_4;})),
    SPRUCE_2_5(GameImage.UPGRADE_SPRUCE_02_05.getImage(), "Erbarmungslose Fichte", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_5;})),
    SPRUCE_2_6(GameImage.UPGRADE_SPRUCE_02_06.getImage(), "Fichtenfreundschaft", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_6;})),
    SPRUCE_3_1(GameImage.UPGRADE_SPRUCE_03_01.getImage(), "Serienm�rder", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_1;})),
    SPRUCE_3_2(GameImage.UPGRADE_SPRUCE_03_02.getImage(), "Rasende Fichte", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_2;})),
    SPRUCE_3_3(GameImage.UPGRADE_SPRUCE_03_03.getImage(), "Riesenschreck", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_3;})),
    SPRUCE_3_4(GameImage.UPGRADE_SPRUCE_03_04.getImage(), "Fichtenforschung", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_4;})),
    SPRUCE_3_5(GameImage.UPGRADE_SPRUCE_03_05.getImage(), "Dominierende Nadeln", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_5;})),
    SPRUCE_3_6(GameImage.UPGRADE_SPRUCE_03_06.getImage(), "Aufruhr der Fichten", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_6;})),
    
    // MAPLE
    MAPLE_1_1(GameImage.UPGRADE_MAPLE_01_01.getImage(), "Ausbau", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_1;})),
    MAPLE_1_2(GameImage.UPGRADE_MAPLE_01_02.getImage(), "Bund der Ahorne", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_2;})),
    MAPLE_1_3(GameImage.UPGRADE_MAPLE_01_03.getImage(), "Tod den Gierigen", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_3;})),
    MAPLE_2_1(GameImage.UPGRADE_MAPLE_02_01.getImage(), "Eskalation", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_1;})),
    MAPLE_2_2(GameImage.UPGRADE_MAPLE_02_02.getImage(), "Aufladen", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_2;})),
    MAPLE_2_3(GameImage.UPGRADE_MAPLE_02_03.getImage(), "Ersch�pfende Bl�tter", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_3;})),
    MAPLE_3_1(GameImage.UPGRADE_MAPLE_03_01.getImage(), "Zerschmetternde Bl�tter", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_1;})),
    MAPLE_3_2(GameImage.UPGRADE_MAPLE_03_02.getImage(), "Gnadenlose Bl�tter", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_2;})),
    MAPLE_3_3(GameImage.UPGRADE_MAPLE_03_03.getImage(), "Zerlegende Bl�tter", 1, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_3;}));


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
