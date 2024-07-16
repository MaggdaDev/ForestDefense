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
    SPRUCE_1_1(GameImage.UPGRADE_SPRUCE_01_01.getImage(), "Nadel Teilung", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_1;}), GameObjectType.T_SPRUCE),
    SPRUCE_1_2(GameImage.UPGRADE_SPRUCE_01_02.getImage(), "Fichten-Monokultur", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_2;}), GameObjectType.T_SPRUCE),
    SPRUCE_1_3(GameImage.UPGRADE_SPRUCE_01_03.getImage(), "R�stungsdurchdringende Nadeln", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_3;}), GameObjectType.T_SPRUCE),
    SPRUCE_1_4(GameImage.UPGRADE_SPRUCE_01_04.getImage(), "H�her wachsen", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_4;}), GameObjectType.T_SPRUCE),
    SPRUCE_1_5(GameImage.UPGRADE_SPRUCE_01_05.getImage(), "Nadelst�rkung", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_5;}), GameObjectType.T_SPRUCE),
    SPRUCE_1_6(GameImage.UPGRADE_SPRUCE_01_06.getImage(), "Lebensraub", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_1_6;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_1(GameImage.UPGRADE_SPRUCE_02_01.getImage(), "Aufruestung", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_1;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_2(GameImage.UPGRADE_SPRUCE_02_02.getImage(), "Fichten-Wut", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_2;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_3(GameImage.UPGRADE_SPRUCE_02_03.getImage(), "Kritische Nadeln", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_3;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_4(GameImage.UPGRADE_SPRUCE_02_04.getImage(), "Wurzelhieb", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_4;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_5(GameImage.UPGRADE_SPRUCE_02_05.getImage(), "Erbarmungslose Fichte", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_5;}), GameObjectType.T_SPRUCE),
    SPRUCE_2_6(GameImage.UPGRADE_SPRUCE_02_06.getImage(), "Fichtenfreundschaft", 550, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_2_6;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_1(GameImage.UPGRADE_SPRUCE_03_01.getImage(), "Serienm�rder", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_1;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_2(GameImage.UPGRADE_SPRUCE_03_02.getImage(), "Rasende Fichte", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_2;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_3(GameImage.UPGRADE_SPRUCE_03_03.getImage(), "Riesenschreck", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_3;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_4(GameImage.UPGRADE_SPRUCE_03_04.getImage(), "Fichtenforschung", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_4;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_5(GameImage.UPGRADE_SPRUCE_03_05.getImage(), "Dominierende Nadeln", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_5;}), GameObjectType.T_SPRUCE),
    SPRUCE_3_6(GameImage.UPGRADE_SPRUCE_03_06.getImage(), "Aufruhr der Fichten", 1700, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_SPURCE_3_6;}), GameObjectType.T_SPRUCE),
    
    // MAPLE
    MAPLE_1_1(GameImage.UPGRADE_MAPLE_01_01.getImage(), "Ausbau", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_1;}), GameObjectType.T_MAPLE),
    MAPLE_1_2(GameImage.UPGRADE_MAPLE_01_02.getImage(), "Bund der Ahorne", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_2;}), GameObjectType.T_MAPLE),
    MAPLE_1_3(GameImage.UPGRADE_MAPLE_01_03.getImage(), "Tod den Gierigen", 150, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_1_3;}), GameObjectType.T_MAPLE),
    MAPLE_2_1(GameImage.UPGRADE_MAPLE_02_01.getImage(), "Eskalation", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_1;}), GameObjectType.T_MAPLE),
    MAPLE_2_2(GameImage.UPGRADE_MAPLE_02_02.getImage(), "Aufladen", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_2;}), GameObjectType.T_MAPLE),
    MAPLE_2_3(GameImage.UPGRADE_MAPLE_02_03.getImage(), "Ersch�pfende Bl�tter", 500, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_2_3;}), GameObjectType.T_MAPLE),
    MAPLE_3_1(GameImage.UPGRADE_MAPLE_03_01.getImage(), "Zerschmetternde Bl�tter", 1200, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_1;}), GameObjectType.T_MAPLE),
    MAPLE_3_2(GameImage.UPGRADE_MAPLE_03_02.getImage(), "Gnadenlose Bl�tter", 1200, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_2;}), GameObjectType.T_MAPLE),
    MAPLE_3_3(GameImage.UPGRADE_MAPLE_03_03.getImage(), "Zerlegende Bl�tter", 1200, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_MAPLE_3_3;}), GameObjectType.T_MAPLE),
    
    //LORBEER
    
    LORBEER_1_1(GameImage.UPGRADE_LORBEER_01_01.getImage(), "Weitreichende Ernte", 350, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_1_1;}), GameObjectType.T_LORBEER),
    LORBEER_1_2(GameImage.UPGRADE_LORBEER_01_02.getImage(), "Ertragreiche Ernte", 350, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_1_2;}), GameObjectType.T_LORBEER),
    LORBEER_1_3(GameImage.UPGRADE_LORBEER_01_03.getImage(), "Effizientere Ernte", 350, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_1_3;}), GameObjectType.T_LORBEER),
    LORBEER_1_4(GameImage.UPGRADE_LORBEER_01_04.getImage(), "Vorrats-Ernte", 350, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_1_4;}), GameObjectType.T_LORBEER),
    LORBEER_2_1(GameImage.UPGRADE_LORBEER_02_01.getImage(), "Brutale Ernte", 800, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_2_1;}), GameObjectType.T_LORBEER),
    LORBEER_2_2(GameImage.UPGRADE_LORBEER_02_02.getImage(), "Ernterausch", 800, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_2_2;}), GameObjectType.T_LORBEER),
    LORBEER_2_3(GameImage.UPGRADE_LORBEER_02_03.getImage(), "Massenproduktion", 800, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_2_3;}), GameObjectType.T_LORBEER),
    LORBEER_2_4(GameImage.UPGRADE_LORBEER_02_04.getImage(), "Wiederverwertung", 800, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_2_4;}), GameObjectType.T_LORBEER),
    LORBEER_3_1(GameImage.UPGRADE_LORBEER_03_01.getImage(), "Mechanische Ernte", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_3_1;}), GameObjectType.T_LORBEER),
    LORBEER_3_2(GameImage.UPGRADE_LORBEER_03_02.getImage(), "Prestige Ernte", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_3_2;}), GameObjectType.T_LORBEER),
    LORBEER_3_3(GameImage.UPGRADE_LORBEER_03_03.getImage(), "Kopfgeld Ernte", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_3_3;}), GameObjectType.T_LORBEER),
    LORBEER_3_4(GameImage.UPGRADE_LORBEER_03_04.getImage(), "Tauschhandel", 2000, (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_LORBEER_3_4;}), GameObjectType.T_LORBEER),
    
    // OAK
    
    OAK_1_1(GameImage.UPGRADE_OAK_01_01.getImage(), "Harte Rinde", 150 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_1_1;}), GameObjectType.T_OAK),
    OAK_1_2(GameImage.UPGRADE_OAK_01_02.getImage(), "Leckere Eicheln", 150 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_1_2;}), GameObjectType.T_OAK),
    OAK_1_3(GameImage.UPGRADE_OAK_01_03.getImage(), "Leckere Wurzeln", 150 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_1_3;}), GameObjectType.T_OAK),
    OAK_1_4(GameImage.UPGRADE_OAK_01_04.getImage(), "Raue Rinde", 150 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_1_4;}), GameObjectType.T_OAK),
    OAK_2_1(GameImage.UPGRADE_OAK_02_01.getImage(), "Auffrischung", 300 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_2_1;}), GameObjectType.T_OAK),
    OAK_2_2(GameImage.UPGRADE_OAK_02_02.getImage(), "Frische Quelle", 300 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_2_2;}), GameObjectType.T_OAK),
    OAK_2_3(GameImage.UPGRADE_OAK_02_03.getImage(), "Soziale Eiche", 300 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_2_3;}), GameObjectType.T_OAK),
    OAK_2_4(GameImage.UPGRADE_OAK_02_04.getImage(), "Verbundene Wurzeln", 300 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_2_4;}), GameObjectType.T_OAK),
    OAK_3_1(GameImage.UPGRADE_OAK_03_01.getImage(), "Totalregeneration", 800 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_3_1;}), GameObjectType.T_OAK),
    OAK_3_2(GameImage.UPGRADE_OAK_03_02.getImage(), "Eichenwall", 800 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_3_2;}), GameObjectType.T_OAK),
    OAK_3_3(GameImage.UPGRADE_OAK_03_03.getImage(), "Spontane Erh�rtung", 800 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_3_3;}), GameObjectType.T_OAK),
    OAK_3_4(GameImage.UPGRADE_OAK_03_04.getImage(), "Eichelernte", 800 , (() -> {return Game.getInstance().language.UPGRADE_DESCRIPTION_OAK_3_4;}), GameObjectType.T_OAK);


    private final Image icon;
    private final String name;
    private final int prize;
    private final LanguageFetcher languageFetcher;
    private final GameObjectType owner;

    private Upgrade(Image i, String n, int p, LanguageFetcher descriptionFetcher, GameObjectType owner) {
        icon = i;
        name = n;
        prize = p;
        languageFetcher = descriptionFetcher;
        this.owner = owner;
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
    
    public GameObjectType getOwnerType() {
        return owner;
    }
    
   
}
