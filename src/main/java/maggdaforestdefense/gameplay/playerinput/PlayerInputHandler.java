/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.playerinput;

import javafx.scene.input.MouseEvent;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.GameScreen;
import maggdaforestdefense.gameplay.ingamemenus.SideMenu;
import maggdaforestdefense.menues.MenuManager;

/**
 *
 * @author DavidPrivat
 */
public class PlayerInputHandler {

    private SelectionSqare selectionSquare;
    private SelectionClickedSquare selectionClickedSquare;
    private RangeRect rangeRect;
    private ClientMap map;

    private static PlayerInputHandler instance;

    public PlayerInputHandler() {
        instance = this;

    }

    public void setMap(ClientMap map) {
        this.map = map;
        selectionSquare = new SelectionSqare(map);
        selectionClickedSquare = new SelectionClickedSquare(map);
        rangeRect = new RangeRect(map);
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(rangeRect);
                

    }

    public void mapCellClicked(ClientMapCell clickedCell) {
        Game.getInstance().getGameScreen().setNewContentSideMenu(clickedCell.getMenuPane());
        if(clickedCell.isPlanted()) {
            showRange(clickedCell);

        } else {
            rangeRect.setVisible(false);
        }

    }

    public static PlayerInputHandler getInstance() {
        return instance;
    }

    public void showRange(ClientMapCell cell) {
        if (cell.getCurrentTower() != null) {
            rangeRect.adjustRange(cell.getCurrentTower());
            
        }
    }
}
