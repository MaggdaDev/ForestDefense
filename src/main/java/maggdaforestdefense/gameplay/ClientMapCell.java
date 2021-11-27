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
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Exceptions;

/**
 *
 * @author DavidPrivat
 */
public class ClientMapCell extends ImageView {

    private MapCell.CellType cellType;
    private boolean isPlanted = false;

    private PlantMenu plantMenu;

    private ClientTower currentTower = null;

    private MENU_STATE menuState = MENU_STATE.PLANT_MENU;
    
    private final int xIndex, yIndex;

    public ClientMapCell(MapCell.CellType type, int xIndex, int yIndex) {
        super(type.getImage());
        cellType = type;
        
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        
        setFitHeight(MapCell.CELL_SIZE);
        setFitWidth(MapCell.CELL_SIZE);
        


        setLayoutX((((double) (xIndex)) + 0.0d) * MapCell.CELL_SIZE);
        setLayoutY((((double) (yIndex)) + 0.0d) * MapCell.CELL_SIZE);

        setOnMouseEntered((MouseEvent e) -> {
            SelectionSqare.getInstance().updatePosition(xIndex, yIndex);
        });

        setOnMouseClicked((MouseEvent e) -> {
            Logger.logClient("KLICK   x:" + xIndex + "              y:" + yIndex);
            PlayerInputHandler.getInstance().mapCellClicked(this);
            SelectionClickedSquare.getInstance().updatePosition(xIndex, yIndex);
        });

        plantMenu = new PlantMenu(type, xIndex, yIndex);
        
        

    }

    public Parent getMenu() {
        switch (menuState) {
            case PLANT_MENU:
                plantMenu.setBuyTreeBox(null);
                plantMenu.updateCoins(Game.getInstance().getCoins());
                return plantMenu;
            case UPGRADE_MENU:
                return currentTower.getUpgradeMenu();
            case GROWING_WAITING_MENU:
                return currentTower.getGrowingWaitingMenu();
            default:
                throw new UnsupportedOperationException();
        }
    }

    public void plantTree(ClientTower tree) {
        isPlanted = true;
        try {
            if (currentTower != null) {
                throw new Exceptions.MultipleTowersOnCellException();
            }
            currentTower = tree;
            menuState = MENU_STATE.GROWING_WAITING_MENU;

            if (SelectionClickedSquare.getInstance().isIndexClicked(xIndex, yIndex)) {
                PlayerInputHandler.getInstance().mapCellClicked(this);
            }

        } catch (Exceptions.MultipleTowersOnCellException e) {
            e.printStackTrace();
        }
    }

    public void removeTree(ClientTower tree) {

            isPlanted = false;
            currentTower = null;
            menuState = MENU_STATE.PLANT_MENU;
            PlayerInputHandler.getInstance().mapCellClicked(this);
        
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

    public void notifyTreeMature() {
        menuState = MENU_STATE.UPGRADE_MENU;
        if (SelectionClickedSquare.getInstance().isIndexClicked(xIndex, yIndex)) {
            PlayerInputHandler.getInstance().mapCellClicked(this);
        }
    }

    public static enum MENU_STATE {
        PLANT_MENU,
        GROWING_WAITING_MENU,
        UPGRADE_MENU;
    }
}
