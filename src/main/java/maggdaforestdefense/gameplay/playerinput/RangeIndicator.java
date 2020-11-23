/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.scene.Group;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;

import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;

/**
 *
 * @author DavidPrivat
 */
public class RangeIndicator extends Group{

    private ClientMap clientMap;
    private ClientMapCell lastCell;
    private Tower.RangeType currentRangeType;

    private Rectangle rect;
    private Circle circle;

    public RangeIndicator(ClientMap map) {
        currentRangeType = Tower.RangeType.CIRCLE;
        clientMap = map;

        //rect
        rect = new Rectangle(0, 0, 100, 100);
        rect.setOpacity(0.3);
        rect.setFill(Color.LIGHTGRAY);
        rect.setStroke(Color.BLUE);
        rect.setMouseTransparent(true);
        rect.setVisible(false);

        //circle
        circle = new Circle(0, 0, 100);
        circle.setOpacity(0.3);
        circle.setFill(Color.LIGHTGRAY);
        circle.setStroke(Color.BLUE);
        circle.setMouseTransparent(true);
        circle.setVisible(false);
        
        
        getChildren().addAll(rect,circle);

    }

    public void adjustRange(ClientTower tower) {
        setVisible(true);
        rect.setVisible(false);
        circle.setVisible(false);
        int xIndex = tower.getXIndex();
        int yIndex = tower.getYIndex();
        double range = tower.getRange();
        currentRangeType = tower.getRangeType();
        ClientMapCell newCell = clientMap.getCells()[xIndex][yIndex];

        lastCell = newCell;
        switch (currentRangeType) {
            case SQUARED:

                double size = (range * 2 + 1) * MapCell.CELL_SIZE;
                rect.setWidth(size);
                rect.setHeight(size);
                rect.setLayoutX((xIndex * MapCell.CELL_SIZE - size / 2) + 0.5 * MapCell.CELL_SIZE);
                rect.setLayoutY((yIndex * MapCell.CELL_SIZE - size / 2) + 0.5 * MapCell.CELL_SIZE);
                rect.setViewOrder(-100);
                rect.setVisible(true);
                break;

            case CIRCLE:
                circle.setRadius(range * MapCell.CELL_SIZE);
                circle.setLayoutX(((double)xIndex + 0.5d) * MapCell.CELL_SIZE);
                circle.setLayoutY(((double)yIndex + 0.5d) * MapCell.CELL_SIZE);
                circle.setViewOrder(-101);
                circle.setVisible(true);
                break;

        }
    }


}
