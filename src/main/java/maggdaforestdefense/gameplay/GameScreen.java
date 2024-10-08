/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
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

    private ClientMap map;
    private BorderPane overlayPaneOuter, overlayPaneInner;
    private Group gamePlayGroup;
    private TopOverlay topOverlay;
    private PlayerInputHandler inputHandler;

    private double scale = 1, mapXInset = 0, mapYInset = 0;

    private SideMenu rightSideMenu;
    private EssenceMenu essenceMenu;

    private GameOverOverlay gameOverOverlay;
    private WaveAnnouncer waveAnnouncer;
    private ReadyCheckOverlay readyCheckOverlay;

    public GameScreen() {
        setManaged(false);

        gamePlayGroup = new Group();
        gamePlayGroup.setManaged(false);

        overlayPaneOuter = new BorderPane();
        overlayPaneInner = new BorderPane();

        topOverlay = new TopOverlay(0, 0);
        gameOverOverlay = new GameOverOverlay();
        waveAnnouncer = new WaveAnnouncer();
        readyCheckOverlay = new ReadyCheckOverlay();

        rightSideMenu = new SideMenu(true);
        rightSideMenu.setVisible(false);

        essenceMenu = new EssenceMenu();
        essenceMenu.setVisible(true);

        overlayPaneOuter.setLeft(essenceMenu);
        overlayPaneOuter.setRight(rightSideMenu);
        overlayPaneOuter.setCenter(overlayPaneInner);
        overlayPaneInner.setTop(topOverlay);

        overlayPaneInner.setBottom(readyCheckOverlay);
        overlayPaneOuter.setPickOnBounds(false);
        overlayPaneInner.setPickOnBounds(false);

        setManaged(false);
        setLayoutX(0);
        setLayoutY(0);

        overlayPaneOuter.prefHeightProperty().bind(maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().heightProperty());
        overlayPaneOuter.prefWidthProperty().bind(maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().widthProperty());

        getChildren().addAll(gamePlayGroup, overlayPaneOuter, gameOverOverlay, readyCheckOverlay, waveAnnouncer);
        inputHandler = new PlayerInputHandler();

        maggdaforestdefense.MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {
            handleScroll(e);
        });

        setUpInputListeners();

        setCursor(Cursor.OPEN_HAND);
        setOnMousePressed((MouseEvent e) -> {
            PlayerInputHandler.getInstance().setMousePressed(true, e);
            setCursor(Cursor.CLOSED_HAND);
        });
        setOnMouseReleased((MouseEvent e) -> {
            PlayerInputHandler.getInstance().setMousePressed(false, e);
            setCursor(Cursor.OPEN_HAND);
        });
        setOnMouseDragged((MouseEvent e) -> {
            PlayerInputHandler.getInstance().mouseMoved(e);

        });
        setOnKeyPressed((KeyEvent e) -> {
            if (e.getCode().equals(KeyCode.Z)) {
                mapXInset = 0;
                mapYInset = 0;
                refreshGameplayGroupPosition();
            }
        });
     

    }

    public void setXInset(double x) {
        mapXInset = x;
        refreshGameplayGroupPosition();
    }

    public void setYInset(double y) {
        mapYInset = y;
        refreshGameplayGroupPosition();
    }

    public double getXInset() {
        return mapXInset;
    }

    public double getYInset() {
        return mapYInset;
    }

    private void handleScroll(ScrollEvent e) {
        double x = e.getSceneX();
        double y = e.getSceneY();
        double oldScale = gamePlayGroup.getScaleX();
        scale = oldScale * Math.pow(1.001, e.getDeltaY());
        if (scale > 2) {
            scale = 2;
        } else if (scale < 0.5) {
            scale = 0.5;
        }
        gamePlayGroup.setScaleX(scale);
        gamePlayGroup.setScaleY(scale);

        double f = (scale / oldScale) - 1;
        Bounds bounds = gamePlayGroup.localToScene(gamePlayGroup.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        mapXInset -= f * dx;
        mapYInset -= f * dy;
        refreshGameplayGroupPosition();
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

    private void refreshGameplayGroupPosition() {

        gamePlayGroup.setTranslateX(mapXInset);
        gamePlayGroup.setTranslateY(mapYInset);
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
        rightSideMenu.setContent(p);;
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
