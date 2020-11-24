/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.Node;
import javafx.scene.text.Font;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientSpruce;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.gameplay.ingamemenus.EssenceMenu;
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

    public final static double DEFAULT_FONT = 30;
    private DoubleProperty fontSize = new SimpleDoubleProperty(DEFAULT_FONT);

    private ClientMap map;
    private Group gamePlayGroup;
    private TopOverlay topOverlay;
    private PlayerInputHandler inputHandler;

    private double scrolling = 0, mapXInset = 0, mapYInset = 0;

    private SideMenu rightSideMenu;
    private EssenceMenu essenceMenu;

    private GameOverOverlay gameOverOverlay;
    private WaveAnnouncer waveAnnouncer;
    private ReadyCheckOverlay readyCheckOverlay;

    public GameScreen() {

        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);

        topOverlay = new TopOverlay(0, 0);
        gameOverOverlay = new GameOverOverlay();
        waveAnnouncer = new WaveAnnouncer();
        readyCheckOverlay = new ReadyCheckOverlay();

        rightSideMenu = new SideMenu(true);
        rightSideMenu.setVisible(false);

        essenceMenu = new EssenceMenu();
        essenceMenu.setVisible(true);

        getChildren().addAll(gamePlayGroup, rightSideMenu, essenceMenu, topOverlay, gameOverOverlay, waveAnnouncer, readyCheckOverlay);
        gamePlayGroup.setViewOrder(3);
        rightSideMenu.setViewOrder(2);
        essenceMenu.setViewOrder(2);
        topOverlay.setViewOrder(1);

        setManaged(false);

        inputHandler = new PlayerInputHandler();

        gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), 0, 0));
        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {

            scrolling += e.getDeltaY();
            gamePlayGroup.getTransforms().clear();
            gamePlayGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), maggdaforestdefense.MaggdaForestDefense.getWindowWidth() / 2, maggdaforestdefense.MaggdaForestDefense.getWindowHeight() / 2));

        });

        setUpInputListeners();

        setCursor(Cursor.OPEN_HAND);
        setOnMousePressed((MouseEvent e) -> {
            PlayerInputHandler.getInstance().setMousePressed(true, e);
        });
        setOnMouseReleased((MouseEvent e) -> {
            PlayerInputHandler.getInstance().setMousePressed(false, e);
            setCursor(Cursor.OPEN_HAND);
        });
        setOnMouseDragged((MouseEvent e) -> {
            PlayerInputHandler.getInstance().mouseMoved(e);
            setCursor(Cursor.CLOSED_HAND);
        });

        MaggdaForestDefense.getInstance().addOnSceneResize((a, b, c) -> {
            updateFont();
        });

        styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", fontSize));

    }

    public void updateFont() {
        fontSize.set(DEFAULT_FONT * maggdaforestdefense.MaggdaForestDefense.getInstance().getSizeFact());
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

    public void announceWave(int wave) {
        waveAnnouncer.nextWave(wave);
    }

    public void doEssenceAnimtionTo(ClientTower tower) {
        gamePlayGroup.getChildren().add(new EssenceAnimation(map.getBaseXIndex(), map.getBaseYIndex(), tower));
        tower.hideEssenceButton();
    }

    public void addGameObject(ClientGameObject gameObject) {
        gamePlayGroup.getChildren().add(gameObject);

    }

    void removeGameObject(ClientGameObject remove) {
        if (gamePlayGroup.getChildren().contains(remove)) {
            gamePlayGroup.getChildren().remove(remove);
        }
        if (remove instanceof ClientTower) {
            removeTower((ClientTower) remove);
        }
        remove.onRemove();
    }

    private double getScaleFromScroll() {
        return Math.pow(1.001, scrolling);
    }

    private void refreshGameplayGroupPosition() {
        gamePlayGroup.setLayoutX(mapXInset);
        gamePlayGroup.setLayoutY(mapYInset);
    }

    public void showGameOverOverlay() {

        gameOverOverlay.startAnimation();
    }

    public void showReadyCheck() {
        readyCheckOverlay.startAnimation();
    }

    public void hideReadyCheck() {
        readyCheckOverlay.back();
    }

    public void generateMap(ClientMapCell[][] mapCellArray) {
        map = new ClientMap(mapCellArray);
        gamePlayGroup.getChildren().add(map);
        inputHandler.setMap(map);

    }

    public void setNewContentSideMenu(Parent p) {
        rightSideMenu.setContent(p);
        rightSideMenu.show();
        rightSideMenu.setVisible(true);
    }

    public void addTower(ClientTower tree) {
        ClientMapCell cell = map.getCells()[tree.getXIndex()][tree.getYIndex()];
        cell.plantTree(tree);
        gamePlayGroup.getChildren().add(tree);
    }

    public void removeTower(ClientTower tree) {
        ClientMapCell cell = map.getCells()[tree.getXIndex()][tree.getYIndex()];
        cell.removeTree(tree);
        if (gamePlayGroup.getChildren().contains(tree)) {
            gamePlayGroup.getChildren().remove(tree);
        }
    }

    public Group getGamePlayGroup() {
        return gamePlayGroup;
    }

    public TopOverlay getTopOverlay() {
        return topOverlay;
    }

    public EssenceMenu getEssenceMenu() {
        return essenceMenu;
    }

    public SideMenu getSideMenu() {
        return rightSideMenu;
    }

    public ClientMap getMap() {
        return map;
    }

    public void safeRemoveGameplayNode(Node node) {
        if (gamePlayGroup.getChildren().contains(node)) {
            gamePlayGroup.getChildren().remove(node);
        }
    }

    public void updateReadyCheck(double progress) {
        readyCheckOverlay.updateProgress(progress);
    }

}
