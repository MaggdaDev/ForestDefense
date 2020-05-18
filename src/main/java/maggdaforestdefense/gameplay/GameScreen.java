/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author David
 */
public class GameScreen extends Group{
    
    private ClientMap map;
    public GameScreen() {
        setManaged(false);
    }
    
    public void generateMap(MapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        getChildren().add(map);
    }
}
