package maggdaforestdefense;

import javafx.scene.text.Font;
import maggdaforestdefense.storage.Logger;

public class Main {

    public static void main(String[] args) {
        Logger.logClient(Font.getFamilies().toString());
        MaggdaForestDefense.main(args);
    }
}