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
import maggdaforestdefense.storage.Logger;

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

    private double dragStartMouseX, dragStartMouseY, dragStartLayoutX, dragStartLayoutY;
    private boolean mousePressed = false;

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
        Game.getInstance().getGameScreen().setNewContentSideMenu(clickedCell.getMenu());
        if (clickedCell.isPlanted()) {
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

    public void setMousePressed(boolean b, MouseEvent e) {
        if (mousePressed == false && b == true) {
            dragStartMouseX = e.getX();
            dragStartMouseY = e.getY();
            dragStartLayoutX = Game.getInstance().getGameScreen().getGamePlayGroup().getLayoutX();
            dragStartLayoutY = Game.getInstance().getGameScreen().getGamePlayGroup().getLayoutY();
            Logger.logClient("New Start");
        }
        mousePressed = b;

    }

    public void mouseMoved(MouseEvent e) {
        if (mousePressed) {
            Logger.logClient("moved");
            Game.getInstance().getGameScreen().getGamePlayGroup().setLayoutX(dragStartLayoutX + e.getX() - dragStartMouseX);
            Game.getInstance().getGameScreen().getGamePlayGroup().setLayoutY(dragStartLayoutY + e.getY() - dragStartMouseY);
        }
    }

}
