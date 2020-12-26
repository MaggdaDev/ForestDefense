/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Font;
import javafx.util.Duration;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.menues.MenuManager;

/**
 *
 * @author David
 */
public class GameOverOverlay extends InformationOverlay {

    private Font font = new Font("Arial Black", 120);

    private Label gameOverLabel;
    private Button backToMenuBtn;

    public GameOverOverlay() {
        gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.setTextFill(Color.RED);

        backToMenuBtn = new Button("Back to menu");
        backToMenuBtn.setOnAction((ActionEvent e) -> {
            MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
        });

        getChildren().addAll(gameOverLabel, backToMenuBtn);

    }

}
