/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.util.KeyEventHandler;

/**
 *
 * @author David
 */
public class GameScreen extends Group {

    private ClientMap map;
    private Group gamePlayGroup;

    private double scrolling = 0, mapXInset = 0, mapYInset = 0;

    public GameScreen() {
        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);
        getChildren().add(gamePlayGroup);
        setManaged(false);

        //TEST
        /*
        double oldCenterX = getCenterX(-0.5,2, 8);
            double oldCenterY = getCenterY(-1,2, 4);
            
            double newScale = 3;

            double newCenterX = getCenterX(-0.5,newScale, 8);
            double newCenterY = getCenterY(-1,newScale, 4);
            
            double x = (newCenterX-oldCenterX)*newScale;
            double y = (newCenterY-oldCenterY)*newScale;
         */
        // Focus controll
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.RIGHT) {
            @Override
            public void handle() {
                mapXInset -= 5;
                refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.LEFT) {
            @Override
            public void handle() {

                mapXInset += 5;
                refreshGameplayGroupPosition();

            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.UP) {
            @Override
            public void handle() {
                mapYInset += 5;
                refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.DOWN) {
            @Override
            public void handle() {
                mapYInset -= 5;
                refreshGameplayGroupPosition();
            }
        });
        gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), 0, 0));
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {

            scrolling += e.getDeltaY();
            gamePlayGroup.getTransforms().clear();
            gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), maggdaforestdefense.MaggdaForestDefense.getWindowWidth()/2, maggdaforestdefense.MaggdaForestDefense.getWindowHeight()/2));

        });

    }

    private double getScaleFromScroll() {
        return Math.pow(1.001, scrolling);
    }

    private void refreshGameplayGroupPosition() {
        gamePlayGroup.setLayoutX(mapXInset);
        gamePlayGroup.setLayoutY(mapYInset);
    }

    public void generateMap(MapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
    }

    public void updateFocus() {

    }
}
