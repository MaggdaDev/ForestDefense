/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author David
 */
public class WaveAnnouncer extends InformationOverlay {

    private Font font = new Font(80);
    private Label waveLabel;

    public WaveAnnouncer() {
        waveLabel = new Label();
        waveLabel.setText("Survive as long as possible!\nGood luck with wave 1!");
        waveLabel.setTextAlignment(TextAlignment.CENTER);
        waveLabel.setFont(font);
        waveLabel.setTextFill(Color.LIGHTGRAY);
        getChildren().add(waveLabel);
    }

    public void nextWave(int waveNumber) {
        if (waveNumber > 1) {
            waveLabel.setText("WAVE " + waveNumber + " INCOMING!");
        }
        super.startAnimation(2);
    }
}
