/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class BuyUpgradeButton extends StackPane {

    public static final double SIZE = 100;
    private Upgrade upgrade;
    private ImageView upgradeIcon, lockedIcon;
    private CLICK_STATES clickState;
    private ClientTower tower;
    private PrizeLabel prizeLabel;

    public BuyUpgradeButton(ClientTower owner, Upgrade upgrade, boolean locked) {
        this.upgrade = upgrade;
        tower = owner;


        upgradeIcon = new ImageView(upgrade.getIcon());
        upgradeIcon.setFitHeight(SIZE);
        upgradeIcon.setFitWidth(SIZE);

        lockedIcon = new ImageView(GameImage.MENUICON_LOCK.getImage());
        lockedIcon.setFitHeight(SIZE / 2);
        lockedIcon.setPreserveRatio(true);

        if (locked) {
            lockedIcon.setVisible(true);
            clickState = CLICK_STATES.LOCKED;
        } else {
            clickState = CLICK_STATES.USUAL;
            lockedIcon.setVisible(false);
        }
        
        prizeLabel = new PrizeLabel(upgrade.getPrize());
        
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        VBox vbox = new VBox(upgradeIcon, prizeLabel);
        vbox.setAlignment(Pos.CENTER);

        getChildren().addAll(vbox, lockedIcon);

        update();

        setOnMousePressed((MouseEvent e) -> {
            if (clickState == CLICK_STATES.USUAL) {
                clickState = CLICK_STATES.CLICKED;
                update();
            }
        });
        
        setOnMouseReleased((MouseEvent e)->{
            if(clickState == CLICK_STATES.CLICKED) {
                clickState = CLICK_STATES.USUAL;
                update();
            }
        });

    }

    public void update() {

        switch (clickState) {
            case USUAL:
                lockedIcon.setVisible(false);
                upgradeIcon.setOpacity(1);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case CLICKED:
                lockedIcon.setVisible(false);
                upgradeIcon.setOpacity(1);
                setEffect(new ColorAdjust(0, 0, -0.5, 0));
                break;
            case DISABLE:
                lockedIcon.setVisible(false);
                upgradeIcon.setOpacity(0.5);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case LOCKED:
                lockedIcon.setVisible(true);
                upgradeIcon.setOpacity(0.5);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
        }
    }

    public void setClickState(CLICK_STATES c) {
        clickState = c;
        update();
    }

    public static enum CLICK_STATES {
        USUAL,
        CLICKED,
        LOCKED,
        DISABLE;

    }
}
