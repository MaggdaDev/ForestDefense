/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import maggdaforestdefense.gameplay.playerinput.SelectionSqare;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class ClientMapCell extends StackPane{
    private ImageView imageView;
    private MapCell.CellType cellType;
    private boolean isSelectionQuare = false;
    public ClientMapCell(MapCell.CellType type, double xIndex, double yIndex) {
        cellType = type;
        imageView = new ImageView(type.getImage());
        imageView.setFitHeight(MapCell.CELL_SIZE);
        imageView.setFitWidth(MapCell.CELL_SIZE);
        
        getChildren().add(imageView);
        
        setLayoutX((((double)(xIndex)) + 0.0d) * MapCell.CELL_SIZE);
        setLayoutY((((double)(yIndex)) + 0.0d) * MapCell.CELL_SIZE);
        
        setOnMouseEntered((MouseEvent e)->{
            addSelectionSquare();
        });
        
        setOnMouseExited((MouseEvent e)->{
            removeSelectionSquare();
        });
    }
    
    public void addSelectionSquare() {
        if(!isSelectionQuare) {
            isSelectionQuare = true;
            getChildren().add(SelectionSqare.getInstance());
        }
    }
    
    public void removeSelectionSquare() {
        if(isSelectionQuare) {
            isSelectionQuare = false;
            getChildren().remove(SelectionSqare.getInstance());
        }
    }
}
