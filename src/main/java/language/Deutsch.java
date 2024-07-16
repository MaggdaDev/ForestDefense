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
    SPRUCE_DESCRIPTION = "Die Fichte schie�t einfache Nadelgeschosse, die 1 Gegner durchdringen k�nnen.";
    MAPLE_DESCRIPTION = "Der Ahorn schie�t Ringe aus Bl�ttern um sich, die Fl�chenschaden verursachen.";
    LORBEER_DESCRIPTION = "Der Lorbeerbaum verursacht jedes mal, wenn man ihn angreifen laesst, leichten Schaden an Gegnern in seiner Umgebung. Sterben diese davon, so waechst eine Lorbeere, die man Ernten kann und Geld erhaelt.";
    OAK_DESCRIPTION = "Die Eiche greift nicht an, daf�r h�lt sie viel Schaden aus und regeneriert ihre Lebenspunkte langsam.";
        
        
    // Upgrade description
    UPGRADE_DESCRIPTION_SPURCE_1_1 = "Nadelteilung: Trifft eine Nadel auf einen Gegner, teilt sich die Nadel in weitere Nadeln auf, die sich allerdings nicht weiter aufteilen.";
    UPGRADE_DESCRIPTION_SPURCE_1_2 = "Fichten-Monokultur: Je mehr Fichten auf der Karte sind, desto schneller schie�t die Fichte.";
    UPGRADE_DESCRIPTION_SPURCE_1_3 = "R�stungsdurchdringende Nadeln: Fichte verursacht mehr Schaden an Gegnern mit R�stung.";
    UPGRADE_DESCRIPTION_SPURCE_1_4 = "H�her Wachsen: Kann fliegende Gegner treffen.";
    UPGRADE_DESCRIPTION_SPURCE_1_5 = "Nadelst�rkung: Wenn eine Nadel einen Gegner trifft, f�gt sie dem Gegner dahinter mehr Schaden zu.";
    UPGRADE_DESCRIPTION_SPURCE_1_6 = "Lebensraub: Die Fichte regeneriert sich um einen Teil des verursachten Schadens.";
    
    UPGRADE_DESCRIPTION_SPURCE_2_1 = "Aufr�stung: Je mehr Gegner in der Reichweite der Fichte sind, desto mehr Nadeln schie�t die Fichte.";
    UPGRADE_DESCRIPTION_SPURCE_2_2 = "Fichten-Wut: Im Laufe einer Runde baut die Fichte f�r Treffer eine h�here Schussrate auf";
    UPGRADE_DESCRIPTION_SPURCE_2_3 = "Kritische Nadeln: Einige Nadeln treffen kritisch und teilen mehr Schaden aus.";
    UPGRADE_DESCRIPTION_SPURCE_2_4 = "Wurzelhieb: Kann mit den Wurzeln grabende Gegner angreifen.";
    UPGRADE_DESCRIPTION_SPURCE_2_5 = "Erbarmungslose Fichte: T�tet eine Nadel einen Gegner, so kann sie einen weiteren Gegner durchdringen.";
    UPGRADE_DESCRIPTION_SPURCE_2_6 = "Fichten-Freundschaft: Erh�ht die Lebensregeneration naher Fichten";
    
    UPGRADE_DESCRIPTION_SPURCE_3_1 = "Serienm�rder: Schie�t neue Nadeln pro get�tetem Gegner.";
    UPGRADE_DESCRIPTION_SPURCE_3_2 = "Rasende Fichte: Get�tete Gegner erh�hen die Schussrate permanent.";
    UPGRADE_DESCRIPTION_SPURCE_3_3 = "Riesenschreck: Zus�tzlicher Schaden wird abh�ngig vom verbleibenden Leben des Gegners hinzugef�gt.";
    UPGRADE_DESCRIPTION_SPURCE_3_4 = "Fichtenforschung: Die Fichte erkennt Schwachpunkte einger get�teter Gegner und erh�ht ihren Schaden gegen diese Gegnergruppe permanent.";
    UPGRADE_DESCRIPTION_SPURCE_3_5 = "Dominierende Nadeln: Je l�nger die Nadeln fliegen, desto mehr Schaden verursachen sie.";
    UPGRADE_DESCRIPTION_SPURCE_3_6 = "Aufruhr der Fichten: F�llt die Fichte unter 30% Leben, so opfern andere Fichten auf der Karte Leben, um die Fichte zu heilen";
    
    
    UPGRADE_DESCRIPTION_MAPLE_1_1 = "Ausbau: Je weniger Gegner in der Reichweite sind, desto mehr Schaden verursacht der Ahorn.";
    UPGRADE_DESCRIPTION_MAPLE_1_2 = "Bund der Ahorne: Erh�ht die Reichweite anderer Ahorne in der N�he.";
    UPGRADE_DESCRIPTION_MAPLE_1_3 = "Tod den Gierigen: Je schneller die Gegner sind, desto mehr Schaden verursacht der Ahorn.";
    
    UPGRADE_DESCRIPTION_MAPLE_2_1 = "Eskalation: Stehen viele Gegner in der Reichweite des Ahorns, aktiviert dieser Eskalation und schie�t f�r eine beschr�nkte Zeit schneller.";
    UPGRADE_DESCRIPTION_MAPLE_2_2 = "Aufladen: Nachdem der Ahorn eine bestimmte Zeit nicht angegriffen hat, macht der erste Angriff mehr Schaden.";
    UPGRADE_DESCRIPTION_MAPLE_2_3 = "Ersch�pfende Bl�tter: Vom Ahorn getroffene Gegner bekommen mehr Schaden aus allen Quellen.";
    
    UPGRADE_DESCRIPTION_MAPLE_3_1 = "Zerschmetternde Bl�tter: Anstatt Schaden zu verursachen, zerst�rt der Ahorn die entsprechende Menge an R�stungspunkten bei Gegnern permanent.";
    UPGRADE_DESCRIPTION_MAPLE_3_2 = "Rei�ende Bl�tter: Jeder Gegner l�sst die Bl�tter etwas weiter fliegen ";
    UPGRADE_DESCRIPTION_MAPLE_3_3 = "Zerlegende Bl�tter: Pro getroffenem Gegner wird die Abklingzeit f�r den n�chsten Schuss verringert.";
    
    
    UPGRADE_DESCRIPTION_LORBEER_1_1 = "Weitreichende Ernte: Die Reichweite der Lorbeere wird erh�ht.";
    UPGRADE_DESCRIPTION_LORBEER_1_2 = "Ertragreiche Ernte: Jede Lorbeere bringt mehr Geld.";
    UPGRADE_DESCRIPTION_LORBEER_1_3 = "Effizientere Ernte: Die Wartezeit zwischen Attacken wird verringert.";
    UPGRADE_DESCRIPTION_LORBEER_1_4 = "Vorrats-Ernte: Es k�nnen mehr Lorbeeren gleichzeitig am Baum h�ngen.";
            
    UPGRADE_DESCRIPTION_LORBEER_2_1 = "Brutale Ernte: Je mehr Lebenspunkte der Gegner schon verloren hat, desto mehr Schaden wird verursacht.";
    UPGRADE_DESCRIPTION_LORBEER_2_2 = "Ernterausch: Werden viele gegner auf einmal get�tet, so wird die Wartezeit zur n�chsten Attacke halbiert.";
    UPGRADE_DESCRIPTION_LORBEER_2_3 = "Massenproduktion: Je mehr Lorbeeren auf einmal verkauft werden, desto mehr Geld erh�lt man f�r diese.";
    UPGRADE_DESCRIPTION_LORBEER_2_4 = "Wiederverwertung: Vom Lorbeerbaum verletzte, aber nicht get�tete Gegner geben beim Tod mehr Geld.";
            
    UPGRADE_DESCRIPTION_LORBEER_3_1 = "Automatische Ernte: Kann ein Gegner in Reichweite get�tet werden, so wird die Attacke automatisch aktiviert.";
    UPGRADE_DESCRIPTION_LORBEER_3_2 = "Prestige-Ernte: Befindet sich die maximale Anzahl an ungeernteten Lorbeeren am Baum, so k�nnen diese in eine Verbesserung f�r diesen Baum umgewandelt werden, welche den Ertrag geernteter Lorbeeren um 20% erh�ht.";
    UPGRADE_DESCRIPTION_LORBEER_3_3 = "Kopfgeld-Ernte: F�r die Erf�llung bestimmter Auftr�ge wird die Maximalanzahl an Lorbeeren produziert.";
    UPGRADE_DESCRIPTION_LORBEER_3_4 = "Tauschhandel: Befindet sich die maximale Anzahl an Lorbeeren am Baum, so k�nnen diese gegen eine zuf�llige Verbesserung eines Baums eingetauscht werden.";
    
    
    UPGRADE_DESCRIPTION_OAK_1_1 = "Harte Rinde: Die Eiche hat mehr Lebenspunkte.";
    UPGRADE_DESCRIPTION_OAK_1_2 = "Leckere Eicheln: Fliegende Gegner bevorzugen die Eiche vor anderen B�umen, um von den leckeren Eicheln zu kosten.";
    UPGRADE_DESCRIPTION_OAK_1_3 = "Leckere Wurzeln: Grabende Gegner bevorzugen die Eiche vor anderen B�umen, um von den leckeren Wurzeln zu kosten.";
    UPGRADE_DESCRIPTION_OAK_1_4 = "Raue Rinde: Gegner, die die Eiche angreifen, erleiden mehr Schaden.";
            
    UPGRADE_DESCRIPTION_OAK_2_1 = "Auffrischung: Am Ende einer Runde f�llt die Eiche ihre Lebenspunkte wieder auf.";
    UPGRADE_DESCRIPTION_OAK_2_2 = "Frische Quelle: Steht die Eiche am Wasser, regeneriert sie sich um einen Prozentsatz ihrer Leben.";
    UPGRADE_DESCRIPTION_OAK_2_3 = "Soziale Eiche: Die Eiche spendet B�umen in ihrer Umgebung Leben, sollten diese angegriffen werden.";
    UPGRADE_DESCRIPTION_OAK_2_4 = "Verbundene Wurzeln: Die Eiche heilt sich um einen kleinen Teil des Schadens, den andere Eichen erleiden.";
            
    UPGRADE_DESCRIPTION_OAK_3_1 = "Totalregeneration: F�higkeit: f�llt die Lebenspunkte der Eiche komplett auf.";
    UPGRADE_DESCRIPTION_OAK_3_2 = "Eichenwall: Eichen in einer Reihe verbinden sich zu einem Wall; Erlittener Schaden wird dann auf alle Eichen aufgeteilt.";
    UPGRADE_DESCRIPTION_OAK_3_3 = "Spontane Erh�rtung: F�higkeit: Der n�chste Angriff eines Gegners verursacht keinen Schaden.";
    UPGRADE_DESCRIPTION_OAK_3_4 = "Eichelernte: F�r jeden neuen Gegner, der die Eiche angreift, erh�lt diese maximale Lebenspunkte dazu.";

}
    
}
