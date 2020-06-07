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
import maggdaforestdefense.gameplay.ingamemenus.SideMenu;
import maggdaforestdefense.menues.MenuManager;

/**
 *
 * @author DavidPrivat
 */
public class PlayerInputHandler {

    private SelectionSqare selectionSquare;
    private SelectionClickedSquare selectionClickedSquare;
    private ClientMap map;


    private static PlayerInputHandler instance;

    public PlayerInputHandler() {
        instance = this;

    }

    public void setMap(ClientMap map) {
        this.map = map;
        selectionSquare = new SelectionSqare(map);
        selectionClickedSquare = new SelectionClickedSquare(map);

       
    }

    public void mapCellClicked(ClientMapCell clickedCell) {
        Game.getInstance().getGameScreen().setNewContentSideMenu(clickedCell.getMenuPane());

    }

    public static PlayerInputHandler getInstance() {
        return instance;
    }
}
