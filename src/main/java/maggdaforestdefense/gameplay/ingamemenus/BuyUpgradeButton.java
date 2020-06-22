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
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class BuyUpgradeButton extends StackPane {

    public static final double SIZE = 100;
    private Upgrade upgrade;
    private ImageView upgradeIcon, lockedIcon, checkIcon;
    private CLICK_STATES clickState;
    private ClientTower tower;
    private PrizeLabel prizeLabel;
    private final int tier, type;

    public BuyUpgradeButton(ClientTower owner, Upgrade upgrade, boolean locked, int tier, int type) {
        this.upgrade = upgrade;
        this.tier = tier;
        this.type = type;
        tower = owner;


        upgradeIcon = new ImageView(upgrade.getIcon());
        upgradeIcon.setFitHeight(SIZE);
        upgradeIcon.setFitWidth(SIZE);

        lockedIcon = new ImageView(GameImage.MENUICON_LOCK.getImage());
        lockedIcon.setFitHeight(SIZE / 2);
        lockedIcon.setPreserveRatio(true);
        
        checkIcon = new ImageView(GameImage.MENUICON_CHECK_GREEN.getImage());
        checkIcon.setPreserveRatio(true);
        checkIcon.setFitWidth(SIZE/2);

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

        getChildren().addAll(vbox, lockedIcon, checkIcon);

        update();


        setOnMousePressed((MouseEvent e) -> {
            if (clickState == CLICK_STATES.USUAL) {

                clickState = CLICK_STATES.CLICKED;
                update();
                
                
               
            }
        });
        
        setOnMouseReleased((MouseEvent e)->{
            if(clickState == CLICK_STATES.CLICKED) {
                NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.UPGRADE_BUTTON_CLICKED, new CommandArgument[]{new CommandArgument("id", owner.getGameObjectId()), new CommandArgument("tier", tier), new CommandArgument("type", type)}));
                
            }
        });

    }

    public void update() {

        switch (clickState) {
            case USUAL:
                lockedIcon.setVisible(false);
                checkIcon.setVisible(false);
                upgradeIcon.setOpacity(1);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case CLICKED:
                lockedIcon.setVisible(false);
                checkIcon.setVisible(false);
                upgradeIcon.setOpacity(1);
                setEffect(new ColorAdjust(0, 0, -0.5, 0));
                break;
            case DISABLE:
                lockedIcon.setVisible(false);
                checkIcon.setVisible(false);
                upgradeIcon.setOpacity(0.5);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case LOCKED:
                lockedIcon.setVisible(true);
                checkIcon.setVisible(false);
                upgradeIcon.setOpacity(0.5);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case BOUGHT:
                checkIcon.setVisible(true);
                lockedIcon.setVisible(false);
                upgradeIcon.setOpacity(0.5);
                setEffect(new ColorAdjust(0, 0, 0, 0));
                break;
            case TIER_ALREADY_BOUGHT:
                checkIcon.setVisible(false);
                lockedIcon.setVisible(false);
                upgradeIcon.setOpacity(0.1);
                setEffect(new ColorAdjust(0, 0, -0.5, 0));
                break;
        }
    }
    
    public CLICK_STATES getClickState() {
        return clickState;
    }

    public void setClickState(CLICK_STATES c) {
        clickState = c;
        update();
    }

    public static enum CLICK_STATES {
        USUAL,
        CLICKED,
        LOCKED,
        DISABLE,
        TIER_ALREADY_BOUGHT,
        BOUGHT;

    }
}
