/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.util.KeyEventHandler;

/**
 *
 * @author David
 */
public class GameScreen extends Group{
    
    private ClientMap map;
    private Group gamePlayGroup;
    public GameScreen() {
        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);
        getChildren().add(gamePlayGroup);
        setManaged(false);
        
        /*
        // Focus controll
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.RIGHT) {
            @Override
            public void handle() {
                gamePlayGroup.setScaleX(gamePlayGroup.getScaleX()+1);
            }
        });
*/
      
        
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnKeyPressed((KeyEvent e)->{
            gamePlayGroup.setScaleX(gamePlayGroup.getScaleX()+1);
        });
    }
    
    public void generateMap(MapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
    }
    
    public void updateFocus() {
        
    }
}
