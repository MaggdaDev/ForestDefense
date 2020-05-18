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

    private double scrolling = 1;

    // DAAN
    private double scrollX = 0, scrollY = 0;

    public GameScreen() {
        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);
        getChildren().add(gamePlayGroup);
        setManaged(false);

        // Focus controll
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.RIGHT) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutX(gamePlayGroup.getLayoutX()-5);
                scrollX += 5;
                //refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.LEFT) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutX(gamePlayGroup.getLayoutX()+5);
                scrollX -= 5;
                //refreshGameplayGroupPosition();

            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.UP) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()+5);
                scrollY += 5;
                //refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.DOWN) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()-5);
                scrollY -= 5;
                //refreshGameplayGroupPosition();
            }
        });
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {
            double centerX = getGameplayGroupCenterX(Math.pow(1.001, scrolling));
            double centerY = getGameplayGroupCenterY(Math.pow(1.001, scrolling));
            scrolling += e.getDeltaY();
            
            

            gamePlayGroup.setScaleX(Math.pow(1.001, scrolling));
            gamePlayGroup.setScaleY(Math.pow(1.001, scrolling));

            setGameplayGroupMid(centerX, centerY, Math.pow(1.001, scrolling));
            //scrollX *= 0.9;
            //scrollY *= 0.9;

            //double scaleRatio = Math.pow(1.001, scrolling) / Math.pow(1.001, oldScroll);
            //gamePlayGroup.setLayoutX(gamePlayGroup.getLayoutX()/scaleRatio);//+0.5*(maggdaforestdefense.MaggdaForestDefense.getWindowWidth()*(scaleRatio-1)));
            //gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()/scaleRatio);//+0.5*(maggdaforestdefense.MaggdaForestDefense.getHeight()*(scaleRatio-1)));

        });
        
     

    }
    
    private void setGameplayGroupMid(double centerX, double centerY, double scale) {
        double x = -(centerX - maggdaforestdefense.MaggdaForestDefense.getWindowWidth()*0.5*scale);
        double y = -(centerY - maggdaforestdefense.MaggdaForestDefense.getWindowHeight()*0.5*scale);
        gamePlayGroup.setLayoutX(x);
        gamePlayGroup.setLayoutY(y);
    }
    
    private double getGameplayGroupCenterX(double scale) {
        return maggdaforestdefense.MaggdaForestDefense.getWindowWidth()/(2*scale) - gamePlayGroup.getLayoutX();
    }
    
    private double getGameplayGroupCenterY(double scale) {
        return maggdaforestdefense.MaggdaForestDefense.getWindowHeight()/(2*scale) - gamePlayGroup.getLayoutY();
    }

    private void refreshGameplayGroupPosition() {
        gamePlayGroup.setLayoutX(scrollX);
        gamePlayGroup.setLayoutY(scrollY);
    }

    public void generateMap(MapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
    }

    public void updateFocus() {

    }
}
