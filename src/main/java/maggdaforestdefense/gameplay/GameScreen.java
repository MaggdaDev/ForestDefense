/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.util.KeyEventHandler;

/**
 *
 * @author David
 */
public class GameScreen extends Group {

    private ClientMap map;
    private Group gamePlayGroup;

    private double scrolling = 1, mapXInset = 0, mapYInset = 0;


    public GameScreen() {
        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);
        getChildren().add(gamePlayGroup);
        setManaged(false);

        // Focus controll
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.RIGHT) {
            @Override
            public void handle() {
                mapXInset += 5;
                refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.LEFT) {
            @Override
            public void handle() {
                
                mapXInset -= 5;
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
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {
            double oldCenterX = getCenterX(mapXInset,Math.pow(1.001, scrolling));
            double oldCenterY = getCenterY(mapYInset,Math.pow(1.001, scrolling));
            scrolling += e.getDeltaY();
            double newScale = Math.pow(1.001, scrolling);
            
            

            gamePlayGroup.setScaleX(newScale);
            gamePlayGroup.setScaleY(newScale);

            double newCenterX = getCenterX(mapXInset,newScale);
            double newCenterY = getCenterY(mapYInset,newScale);
            
            mapXInset -= (newCenterX-oldCenterX)*newScale;
            mapYInset -= (newCenterY-oldCenterY)*newScale;
            
            refreshGameplayGroupPosition();
            
            //scrollX *= 0.9;
            //scrollY *= 0.9;

            //double scaleRatio = Math.pow(1.001, scrolling) / Math.pow(1.001, oldScroll);
            //gamePlayGroup.setLayoutX(gamePlayGroup.getLayoutX()/scaleRatio);//+0.5*(maggdaforestdefense.MaggdaForestDefense.getWindowWidth()*(scaleRatio-1)));
            //gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()/scaleRatio);//+0.5*(maggdaforestdefense.MaggdaForestDefense.getHeight()*(scaleRatio-1)));

        });
        
     

    }
    
    private double getCenterX(double xInset, double scale) {
        return xInset/scale + 0.5*maggdaforestdefense.MaggdaForestDefense.getWindowWidth()/scale;
    }
    
    private double getCenterY(double yInset, double scale) {
        return yInset/scale + 0.5*maggdaforestdefense.MaggdaForestDefense.getWindowHeight()/scale;
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
