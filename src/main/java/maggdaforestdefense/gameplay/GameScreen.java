/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.ingamemenus.SideMenu;
import maggdaforestdefense.gameplay.playerinput.PlayerInputHandler;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.KeyEventHandler;

/**
 *
 * @author David
 */
public class GameScreen extends Group {

    private ClientMap map;
    private Group gamePlayGroup;
    private PlayerInputHandler inputHandler;

    private double scrolling = 0, mapXInset = 0, mapYInset = 0;

    private SideMenu sideMenu;

    public GameScreen() {

        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);

        getChildren().add(gamePlayGroup);
        setManaged(false);

        inputHandler = new PlayerInputHandler();

        gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), 0, 0));
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {

            scrolling += e.getDeltaY();
            gamePlayGroup.getTransforms().clear();
            gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), maggdaforestdefense.MaggdaForestDefense.getWindowWidth() / 2, maggdaforestdefense.MaggdaForestDefense.getWindowHeight() / 2));

        });

        setUpInputListeners();
        
        sideMenu = new SideMenu();
        getChildren().add(sideMenu);
        sideMenu.setVisible(false);

    }

    private final void setUpInputListeners() {
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.RIGHT) {
            @Override
            public void handle() {
                mapXInset -= 5;
                refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.LEFT) {
            @Override
            public void handle() {

                mapXInset += 5;
                refreshGameplayGroupPosition();

            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.UP) {
            @Override
            public void handle() {
                mapYInset += 5;
                refreshGameplayGroupPosition();
            }
        });
        Game.getInstance().addKeyListener(new KeyEventHandler(KeyCode.DOWN) {
            @Override
            public void handle() {
                mapYInset -= 5;
                refreshGameplayGroupPosition();
            }
        });
    }

    public void addGameObject(ClientGameObject gameObject) {
        gamePlayGroup.getChildren().add(gameObject);
    }

    private double getScaleFromScroll() {
        return Math.pow(1.001, scrolling);
    }

    private void refreshGameplayGroupPosition() {
        gamePlayGroup.setLayoutX(mapXInset);
        gamePlayGroup.setLayoutY(mapYInset);
    }

    public void generateMap(ClientMapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
        inputHandler.setMap(map);

    }

    public void setNewContentSideMenu(Parent p) {
        sideMenu.setContent(p);
        sideMenu.show();
        sideMenu.setVisible(true);
    }

}
