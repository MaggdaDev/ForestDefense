/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

/**
 *
 * @author David
 */
public class GameScreen extends Group{
    public GameScreen(Circle gameCircle) {
        setManaged(false);
        getChildren().add(gameCircle);
    }
}
