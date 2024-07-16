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
public class Deutsch extends Language{  // HALLE

    public Deutsch() {
    // Tower names
    SPRUCE_NAME = "Fichte";
    MAPLE_NAME = "Ahorn";
    LORBEER_NAME = "Lorbeerbaum";
    OAK_NAME = "Eiche";
    
    // Tower descriptions
    SPRUCE_DESCRIPTION = "Die Fichte schießt einfache Nadelgeschosse, die 1 Gegner durchdringen können.";
    MAPLE_DESCRIPTION = "Der Ahorn schießt Ringe aus Blättern um sich, die Flächenschaden verursachen.";
    LORBEER_DESCRIPTION = "Der Lorbeerbaum verursacht jedes mal, wenn man ihn angreifen laesst, leichten Schaden an Gegnern in seiner Umgebung. Sterben diese davon, so waechst eine Lorbeere, die man Ernten kann und Geld erhaelt.";
    OAK_DESCRIPTION = "Die Eiche greift nicht an, dafür hält sie viel Schaden aus und regeneriert ihre Lebenspunkte langsam.";
        
        
    // Upgrade description
    UPGRADE_DESCRIPTION_SPURCE_1_1 = "Nadelteilung: Trifft eine Nadel auf einen Gegner, teilt sich die Nadel in weitere Nadeln auf, die sich allerdings nicht weiter aufteilen.";
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
    
    
    UPGRADE_DESCRIPTION_MAPLE_1_1 = "Ausbau: Je weniger Gegner in der Reichweite sind, desto mehr Schaden verursacht der Ahorn.";
    UPGRADE_DESCRIPTION_MAPLE_1_2 = "Bund der Ahorne: Erhöht die Reichweite anderer Ahorne in der Nähe.";
    UPGRADE_DESCRIPTION_MAPLE_1_3 = "Tod den Gierigen: Je schneller die Gegner sind, desto mehr Schaden verursacht der Ahorn.";
    
    UPGRADE_DESCRIPTION_MAPLE_2_1 = "Eskalation: Stehen viele Gegner in der Reichweite des Ahorns, aktiviert dieser Eskalation und schießt für eine beschränkte Zeit schneller.";
    UPGRADE_DESCRIPTION_MAPLE_2_2 = "Aufladen: Nachdem der Ahorn eine bestimmte Zeit nicht angegriffen hat, macht der erste Angriff mehr Schaden.";
    UPGRADE_DESCRIPTION_MAPLE_2_3 = "Erschöpfende Blätter: Vom Ahorn getroffene Gegner bekommen mehr Schaden aus allen Quellen.";
    
    UPGRADE_DESCRIPTION_MAPLE_3_1 = "Zerschmetternde Blätter: Anstatt Schaden zu verursachen, zerstört der Ahorn die entsprechende Menge an Rüstungspunkten bei Gegnern permanent.";
    UPGRADE_DESCRIPTION_MAPLE_3_2 = "Reißende Blätter: Jeder Gegner lässt die Blätter etwas weiter fliegen ";
    UPGRADE_DESCRIPTION_MAPLE_3_3 = "Zerlegende Blätter: Pro getroffenem Gegner wird die Abklingzeit für den nächsten Schuss verringert.";
    
    
    UPGRADE_DESCRIPTION_LORBEER_1_1 = "Weitreichende Ernte: Die Reichweite der Lorbeere wird erhöht.";
    UPGRADE_DESCRIPTION_LORBEER_1_2 = "Ertragreiche Ernte: Jede Lorbeere bringt mehr Geld.";
    UPGRADE_DESCRIPTION_LORBEER_1_3 = "Effizientere Ernte: Die Wartezeit zwischen Attacken wird verringert.";
    UPGRADE_DESCRIPTION_LORBEER_1_4 = "Vorrats-Ernte: Es können mehr Lorbeeren gleichzeitig am Baum hängen.";
            
    UPGRADE_DESCRIPTION_LORBEER_2_1 = "Brutale Ernte: Je mehr Lebenspunkte der Gegner schon verloren hat, desto mehr Schaden wird verursacht.";
    UPGRADE_DESCRIPTION_LORBEER_2_2 = "Ernterausch: Werden viele gegner auf einmal getötet, so wird die Wartezeit zur nächsten Attacke halbiert.";
    UPGRADE_DESCRIPTION_LORBEER_2_3 = "Massenproduktion: Je mehr Lorbeeren auf einmal verkauft werden, desto mehr Geld erhält man für diese.";
    UPGRADE_DESCRIPTION_LORBEER_2_4 = "Wiederverwertung: Vom Lorbeerbaum verletzte, aber nicht getötete Gegner geben beim Tod mehr Geld.";
            
    UPGRADE_DESCRIPTION_LORBEER_3_1 = "Automatische Ernte: Kann ein Gegner in Reichweite getötet werden, so wird die Attacke automatisch aktiviert.";
    UPGRADE_DESCRIPTION_LORBEER_3_2 = "Prestige-Ernte: Befindet sich die maximale Anzahl an ungeernteten Lorbeeren am Baum, so können diese in eine Verbesserung für diesen Baum umgewandelt werden, welche den Ertrag geernteter Lorbeeren um 20% erhöht.";
    UPGRADE_DESCRIPTION_LORBEER_3_3 = "Kopfgeld-Ernte: Für die Erfüllung bestimmter Aufträge wird die Maximalanzahl an Lorbeeren produziert.";
    UPGRADE_DESCRIPTION_LORBEER_3_4 = "Tauschhandel: Befindet sich die maximale Anzahl an Lorbeeren am Baum, so können diese gegen eine zufällige Verbesserung eines Baums eingetauscht werden.";
    
    
    UPGRADE_DESCRIPTION_OAK_1_1 = "Harte Rinde: Die Eiche hat mehr Lebenspunkte.";
    UPGRADE_DESCRIPTION_OAK_1_2 = "Leckere Eicheln: Fliegende Gegner bevorzugen die Eiche vor anderen Bäumen, um von den leckeren Eicheln zu kosten.";
    UPGRADE_DESCRIPTION_OAK_1_3 = "Leckere Wurzeln: Grabende Gegner bevorzugen die Eiche vor anderen Bäumen, um von den leckeren Wurzeln zu kosten.";
    UPGRADE_DESCRIPTION_OAK_1_4 = "Raue Rinde: Gegner, die die Eiche angreifen, erleiden mehr Schaden.";
            
    UPGRADE_DESCRIPTION_OAK_2_1 = "Auffrischung: Am Ende einer Runde füllt die Eiche ihre Lebenspunkte wieder auf.";
    UPGRADE_DESCRIPTION_OAK_2_2 = "Frische Quelle: Steht die Eiche am Wasser, regeneriert sie sich um einen Prozentsatz ihrer Leben.";
    UPGRADE_DESCRIPTION_OAK_2_3 = "Soziale Eiche: Die Eiche spendet Bäumen in ihrer Umgebung Leben, sollten diese angegriffen werden.";
    UPGRADE_DESCRIPTION_OAK_2_4 = "Verbundene Wurzeln: Die Eiche heilt sich um einen kleinen Teil des Schadens, den andere Eichen erleiden.";
            
    UPGRADE_DESCRIPTION_OAK_3_1 = "Totalregeneration: Fähigkeit: füllt die Lebenspunkte der Eiche komplett auf.";
    UPGRADE_DESCRIPTION_OAK_3_2 = "Eichenwall: Eichen in einer Reihe verbinden sich zu einem Wall; Erlittener Schaden wird dann auf alle Eichen aufgeteilt.";
    UPGRADE_DESCRIPTION_OAK_3_3 = "Spontane Erhärtung: Fähigkeit: Der nächste Angriff eines Gegners verursacht keinen Schaden.";
    UPGRADE_DESCRIPTION_OAK_3_4 = "Eichelernte: Für jeden neuen Gegner, der die Eiche angreift, erhält diese maximale Lebenspunkte dazu.";

}
    
}
