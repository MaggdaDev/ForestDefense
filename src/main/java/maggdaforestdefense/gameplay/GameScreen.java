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
public class GameScreen extends Group{
    
    private ClientMap map;
    private Group gamePlayGroup;
    
    private double scrolling = 1;
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
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.LEFT) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutX(gamePlayGroup.getLayoutX()+5);
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.UP) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()+5);
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.DOWN) {
            @Override
            public void handle() {
                gamePlayGroup.setLayoutY(gamePlayGroup.getLayoutY()-5);
            }
        });
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e)->{
            scrolling += e.getDeltaY();


            gamePlayGroup.setScaleX(Math.pow(1.001, scrolling));
            gamePlayGroup.setScaleY(Math.pow(1.001, scrolling));
        });

      
        
       
    }
    
    public void generateMap(MapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
    }
    
    public void updateFocus() {
        
    }
}
