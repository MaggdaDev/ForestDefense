/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

/**
 *
 * @author DavidPrivat
 */
public class Deutsch extends Language{

    public Deutsch() {
    // Tower names
    SPRUCE_NAME = "Fichte";
    MAPLE_NAME = "Ahorn";
    
    // Tower descriptions
    SPRUCE_DESCRIPTION = "Die Fichte schießt einfache Nadelgeschosse, die 1 Gegner durchdringen können.";
    MAPLE_DESCRIPTION = "Der Ahorn schießt Ringe aus Blättern um sich, die Flächenschaden verursachen.";
        
        
    // Upgrade description
    UPGRADE_DESCRIPTION_SPURCE_1_1 = "Nadel Teilung: Trifft eine Nadel auf einen Gegner, teilt sich die Nadel in weitere Nadeln auf, die sich allerdings nicht weiter aufteilen.";
    UPGRADE_DESCRIPTION_SPURCE_1_2 = "Fichten-Monokultur: Je mehr Fichten auf der Karte sind, desto schneller schießt die Fichte.";
    UPGRADE_DESCRIPTION_SPURCE_1_3 = "Rüstungsdurchdringende Nadeln: Fichte verursacht mehr Schaden an Gegnern mit Rüstung.";
    UPGRADE_DESCRIPTION_SPURCE_1_4 = "Höher Wachsen: Kann fliegende Gegner treffen.";
    UPGRADE_DESCRIPTION_SPURCE_1_5 = "Nadelstärkung: Wenn eine Nadel einen Gegner trifft, fügt sie dem Gegner dahinter mehr Schaden zu.";
    UPGRADE_DESCRIPTION_SPURCE_1_6 = "Lebensraub: Die Fichte regeneriert sich um einen Teil des verursachten Schadens.";
    
    UPGRADE_DESCRIPTION_SPURCE_2_1 = "Aufrüstung: Je mehr Gegner in der Reichweite der Fichte sind, desto mehr Nadeln schießt die Fichte.";
    UPGRADE_DESCRIPTION_SPURCE_2_2 = "Fichten-Wut: Im Laufe einer Runde baut die Fichte für Treffer eine höhere Schussrate auf";
    UPGRADE_DESCRIPTION_SPURCE_2_3 = "Kritische Nadeln: Einige Nadeln treffen kritisch und teilen mehr Schaden aus.";
    UPGRADE_DESCRIPTION_SPURCE_2_4 = "Wurzelhieb: Kann mit den Wurzeln grabende Gegner angreifen.";
    UPGRADE_DESCRIPTION_SPURCE_2_5 = "Erbarmungslose Fichte: Tötet eine Nadel einen Gegner, so kann sie einen weiteren Gegner durchdringen.";
    UPGRADE_DESCRIPTION_SPURCE_2_6 = "Fichten-Freundschaft: Erhöht die Lebensregeneration naher Fichten";
    
    UPGRADE_DESCRIPTION_SPURCE_3_1 = "Serienmörder: Schießt neue Nadeln pro getötetem Gegner.";
    UPGRADE_DESCRIPTION_SPURCE_3_2 = "Rasende Fichte: Getötete Gegner erhöhen die Schussrate permanent.";
    UPGRADE_DESCRIPTION_SPURCE_3_3 = "Riesenschreck: Zusätzlicher Schaden wird abhängig vom verbleibenden Leben des Gegners hinzugefügt.";
    UPGRADE_DESCRIPTION_SPURCE_3_4 = "Fichtenforschung: Die Fichte erkennt Schwachpunkte einger getöteter Gegner und erhöht ihren Schaden gegen diese Gegnergruppe permanent.";
    UPGRADE_DESCRIPTION_SPURCE_3_5 = "Dominierende Nadeln: Je länger die Nadeln fliegen, desto mehr Schaden verursachen sie.";
    UPGRADE_DESCRIPTION_SPURCE_3_6 = "Aufruhr der Fichten: Fällt die Fichte unter 30% Leben, so opfern andere Fichten auf der Karte Leben, um die Fichte zu heilen";
}
    
}
