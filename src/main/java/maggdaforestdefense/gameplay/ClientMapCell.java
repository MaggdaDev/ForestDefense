/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import maggdaforestdefense.gameplay.ingamemenus.PlantMenu;
import maggdaforestdefense.gameplay.ingamemenus.SideMenu;
import maggdaforestdefense.gameplay.playerinput.PlayerInputHandler;
import maggdaforestdefense.gameplay.playerinput.SelectionClickedSquare;
import maggdaforestdefense.gameplay.playerinput.SelectionSqare;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.util.Exceptions;

/**
 *
 * @author DavidPrivat
 */
public class ClientMapCell extends StackPane {

    private ImageView imageView;
    private MapCell.CellType cellType;
    private boolean isSelectionQuare = false, isPlanted = false, isSelectionClickedSquare = false;

    private PlantMenu plantMenu;

    private ClientTower currentTower = null;

    private Pane menuPane;

    public ClientMapCell(MapCell.CellType type, int xIndex, int yIndex) {
        cellType = type;
        imageView = new ImageView(type.getImage());
        imageView.setFitHeight(MapCell.CELL_SIZE);
        imageView.setFitWidth(MapCell.CELL_SIZE);

        getChildren().add(imageView);

        setLayoutX((((double) (xIndex)) + 0.0d) * MapCell.CELL_SIZE);
        setLayoutY((((double) (yIndex)) + 0.0d) * MapCell.CELL_SIZE);

        setOnMouseEntered((MouseEvent e) -> {
            addSelectionSquare();
        });

        setOnMouseExited((MouseEvent e) -> {
            removeSelectionSquare();
        });
        setOnMouseClicked((MouseEvent e) -> {
            PlayerInputHandler.getInstance().mapCellClicked(this);
            SelectionClickedSquare.getInstance().addToMapCell(this);
        });

        plantMenu = new PlantMenu(type, xIndex, yIndex);

        menuPane = new Pane(plantMenu);
    }

    public void addSelectionSquare() {
        if (!isSelectionQuare) {
            isSelectionQuare = true;
            getChildren().add(SelectionSqare.getInstance());
        }
    }

    public void addSelectionClickedSquare() {
        isSelectionClickedSquare = true;
        if(!getChildren().contains(SelectionClickedSquare.getInstance())) {
            getChildren().add(SelectionClickedSquare.getInstance());
        }
    }
    
    public void removeSelectionClickedSquare() {
        isSelectionClickedSquare = false;
        if(getChildren().contains(SelectionClickedSquare.getInstance())) {
            getChildren().remove(SelectionClickedSquare.getInstance());
        }
    }

    public void removeSelectionSquare() {
        if (isSelectionQuare) {
            isSelectionQuare = false;
            getChildren().remove(SelectionSqare.getInstance());
        }
    }

    public Parent getMenuPane() {
        return menuPane;

    }

    public void plantTree(ClientTower tree) {
        isPlanted = true;
        try {
            if (currentTower != null) {
                throw new Exceptions.MultipleTowersOnCellException();
            }
            getChildren().add(tree);
            currentTower = tree;
            menuPane.getChildren().clear();
            menuPane.getChildren().add(tree.getUpgradeMenu());
            
            if(isSelectionClickedSquare) {
                PlayerInputHandler.getInstance().mapCellClicked(this);
            }

        } catch (Exceptions.MultipleTowersOnCellException e) {
            e.printStackTrace();
        }
    }

    public ClientTower getCurrentTower() {
        return currentTower;
    }

    public boolean isPlanted() {
        if (isPlanted && currentTower != null) {
            return true;
        }
        return false;

    }
}