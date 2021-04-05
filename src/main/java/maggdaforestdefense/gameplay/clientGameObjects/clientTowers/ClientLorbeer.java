/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import com.google.gson.Gson;
import java.util.Vector;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.InformationBubble;
import maggdaforestdefense.gameplay.InformationPopup;
import maggdaforestdefense.gameplay.RessourceDisplay;
import maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles.LorbeerShot;
import maggdaforestdefense.gameplay.playerinput.ActiveSkillActivator;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer.HeadHuntGenerator.HeadHunt;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower.RangeType;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer.HeadHuntGenerator.Mission;
import maggdaforestdefense.gameplay.playerinput.*;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ClientLorbeer extends ClientTower {

    public final static RangeType RANGE_TYPE = Lorbeer.RANGE_TYPE;

    private ActiveSkillActivator attackActivator, sellActivator, prestigeActivator, tradeActivator;

    private int lorbeerAmount = 0, oldLorbeerAmount = 0, maxLorbeerAmount = 1;
    private int coinsPerLorbeer = 0, oldCoinsPerLorbeer = 0;
    private RessourceDisplay potentialGoldDisplay;
    private RessourceDisplay.LorbeerDisplay lorbeerDisplay;

    private InformationPopup fullPopup, headHuntFinishedPopup;

    private HeadHunt headHunt, oldHeadHunt;
    private HeadHuntBox headHuntBox;

    private TradeBox tradeBox;

    private HBox goldLorbeerHBox;

    private Vector<Upgrade> tradeUpgrades;

    public ClientLorbeer(int id, int xIndex, int yIndex, double health) {
        super(id, GameImage.TOWER_LORBEER_1, GameObjectType.T_LORBEER, UpgradeSet.LORBEER_SET, xIndex, yIndex, Lorbeer.DEFAULT_RANGE, Lorbeer.DEFAULT_HEALTH, Lorbeer.DEFAULT_GROWING_TIME, RANGE_TYPE);
        tradeUpgrades = new Vector<>();
        lorbeerDisplay = new RessourceDisplay.LorbeerDisplay(GameImage.LORBEER_ICON, lorbeerAmount, maxLorbeerAmount);
        potentialGoldDisplay = new RessourceDisplay(GameImage.COIN_ICON, 0);

        attackActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_ATTACK);
        attackActivator.setOnUsed((MouseEvent e) -> {
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_ATTACK.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });

        sellActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_SELL);
        sellActivator.setOnUsed((MouseEvent e) -> {
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_SELL.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });

        prestigeActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_PRESTIGE);
        prestigeActivator.setOnUsed((MouseEvent e) -> {
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_PRESTIGE.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });

        tradeActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_TRADE);
        tradeActivator.setOnUsed((MouseEvent e) -> {
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_TRADE.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });

        goldLorbeerHBox = new HBox(lorbeerDisplay, new Label("="), potentialGoldDisplay);
        upgradeMenu.getTreePane().getChildren().add(goldLorbeerHBox);

        fullPopup = new InformationPopup("VOLL!", (xIndex + 0.5) * MapCell.CELL_SIZE, (yIndex + 0.1) * MapCell.CELL_SIZE);
        headHuntFinishedPopup = new InformationPopup("ABGESCHLOSSEN!", (xIndex + 0.5) * MapCell.CELL_SIZE, (yIndex + 0.1) * MapCell.CELL_SIZE);
        headHuntFinishedPopup.setVisible(false);

        onRemove.add(() -> {
            Game.removeGamePlayNode(fullPopup);
            Game.removeGamePlayNode(headHuntFinishedPopup);
            if (tradeUpgrades.size() != 0) {
                Game.getInstance().removeAllLorbeerTradingUpgrades(tradeUpgrades);
            }
        });

    }

    @Override
    public void setTier(int tier) {
        tier++;
        Image image;
        switch (tier) {
            case 4:
                image = GameImage.TOWER_LORBEER_4.getImage();
                break;
            case 3:
                image = GameImage.TOWER_LORBEER_3.getImage();
                break;
            case 2:
                image = GameImage.TOWER_LORBEER_2.getImage();
                break;
            case 1:
            default:
                image = GameImage.TOWER_LORBEER_1.getImage();
                break;
        }

        setImage(image);
        upgradeMenu.setTreeImage(image);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        healthPoints = updateCommand.getNumArgument("hp");
        updateHealth(healthPoints);

        if (isMature) {
            EffectSet e = EffectSet.fromString(updateCommand.getArgument("effects"));
            handleEffects(e);

            attackActivator.updateCooldown(updateCommand.getNumArgument("attackCooldown"));

            // LORBEERAMOUNT
            if (updateCommand.containsArgument("lorbeeren") && updateCommand.containsArgument("coinsPerLorbeer")) {
                String[] lorbeeren = updateCommand.getArgument("lorbeeren").split("-");
                lorbeerAmount = Integer.parseInt(lorbeeren[0]);
                maxLorbeerAmount = Integer.parseInt(lorbeeren[1]);
                coinsPerLorbeer = (int) (updateCommand.getNumArgument("coinsPerLorbeer"));

                if (oldLorbeerAmount > lorbeerAmount) {
                    if (oldCoinsPerLorbeer < coinsPerLorbeer) {

                    } else {
                        Game.addGamePlayNode(new InformationBubble("+" + (int) ((oldLorbeerAmount - lorbeerAmount) * coinsPerLorbeer), InformationBubble.InformationType.GOLD, getCenterX(), getCenterY()));
                    }
                } else if (oldLorbeerAmount < lorbeerAmount) {
                    Game.addGamePlayNode(new InformationBubble("+" + (int) (lorbeerAmount - oldLorbeerAmount), InformationBubble.InformationType.LORBEER, getCenterX(), getCenterY()));
                }

                oldLorbeerAmount = lorbeerAmount;
                oldCoinsPerLorbeer = coinsPerLorbeer;
                lorbeerDisplay.setValue(lorbeerAmount);
                lorbeerDisplay.setMaxLorbeer(maxLorbeerAmount);
                potentialGoldDisplay.setValue(lorbeerAmount * coinsPerLorbeer);
                if (lorbeerAmount > 0) {
                    sellActivator.setUsable(true);
                } else {
                    sellActivator.setUsable(false);
                }

                fullPopup.setVisible(lorbeerAmount >= maxLorbeerAmount);
                prestigeActivator.setUsable(lorbeerAmount >= maxLorbeerAmount);
                tradeActivator.setUsable(lorbeerAmount >= maxLorbeerAmount);
            }

        }

        if (updateCommand.containsArgument("image")) {
            setImage(GameImage.values()[(int) updateCommand.getNumArgument("image")].getImage());
            updateGrowing(updateCommand.getNumArgument("timeLeft"));
        } else {
            if (!isMature) {
                isMature = true;
                onMatured();

            }
        }

        if (updateCommand.containsArgument("headhunt")) {
            headHunt = HeadHunt.fromString(updateCommand.getArgument("headhunt"));
            headHuntBox.update(headHunt);

        }

        if (headHunt != null) {
            boolean headHuntFinished = headHunt.isFinished();
            sellActivator.setUsable(headHuntFinished);
            headHuntFinishedPopup.setVisible(headHuntFinished);

            if (oldHeadHunt != null && oldHeadHunt.isFinished() && !headHunt.isFinished()) {
                Game.addGamePlayNode(new InformationBubble("+" + (int) oldHeadHunt.getGoldOnFinished(), InformationBubble.InformationType.GOLD, getCenterX(), getCenterY()));
            }

            oldHeadHunt = headHunt;
        }

        // tauschhandel
        

        if (updateCommand.containsArgument("range")) {
            double newRange = updateCommand.getNumArgument("range");

            if (newRange != super.range && SelectionClickedSquare.getInstance().isIndexClicked(xIndex, yIndex)) {
                super.range = newRange;
                PlayerInputHandler.getInstance().showRange(mapCell);
            }
        }
    }
    
    public void editTauschhandel(NetworkCommand command) {
        if (command.getNumArgument("mode") == 1) {
            Upgrade upgrade = Upgrade.values()[(int) command.getNumArgument("upgradeId")];
            tradeBox.addTrade(upgrade);
            Game.getInstance().addLorbeerTradingUpgrade(upgrade);
            tradeUpgrades.add(upgrade);
        } else {
            Upgrade upgrade = Upgrade.values()[(int) command.getNumArgument("upgradeId")];
            tradeBox.removeTrade(upgrade);
            tradeUpgrades.remove(upgrade);
            Game.getInstance().removeLorbeerTradingUpgrade(upgrade);
        }
    }

    @Override
    public void buyUpgrade(int tier, int type) {
        if (UpgradeSet.LORBEER_SET.getUpgrade(tier, type) == Upgrade.LORBEER_3_2) {
            addActiveSkill(prestigeActivator);
        }
        if (UpgradeSet.LORBEER_SET.getUpgrade(tier, type) == Upgrade.LORBEER_3_3) {
            headHuntBox = new HeadHuntBox();
            upgradeMenu.getTreePane().getChildren().remove(goldLorbeerHBox);
            upgradeMenu.getTreePane().getChildren().add(headHuntBox);
        }
        if (UpgradeSet.LORBEER_SET.getUpgrade(tier, type) == Upgrade.LORBEER_3_4) {
            addActiveSkill(tradeActivator);
            tradeBox = new TradeBox();
            upgradeMenu.getTreePane().getChildren().add(tradeBox);
        }
        upgradeMenu.buyUpgrade(tier, type);
    }

    @Override
    public void performActiveSkill(ActiveSkill skill) {
        switch (skill) {
            case LORBEER_ATTACK:
                shoot();
                break;
        }
    }

    public void shoot() {
        new LorbeerShot(xPos + 0.5 * MapCell.CELL_SIZE, yPos + 0.5 * MapCell.CELL_SIZE, (0.5 + range) * (MapCell.CELL_SIZE));
    }

    @Override
    public void onMatured() {
        addActiveSkill(attackActivator);
        addActiveSkill(sellActivator);
    }

    private class TradeBox extends ContentBox {

        private Group tradeGroup;
        private Vector<Trade> trades;
        private Label label;

        public TradeBox() {
            tradeGroup = new Group();

            trades = new Vector<>();

            setAlignment(Pos.CENTER);

            label = new Label("Tauschhandel:");
            label.setTooltip(new Tooltip("Folgende Upgrades können einmalig umsonst erworben werden:"));
            getChildren().addAll(label, tradeGroup);
            setFillWidth(false);

        }

        public void addTrade(Upgrade upgrade) {
            Trade trade = new Trade(upgrade);
            trades.add(trade);
            tradeGroup.getChildren().add(trade);
            relayoutTrades();
            Logger.logClient("Box add Trade!");
        }

        public void removeTrade(Upgrade upgrade) {
            for (Trade trade : trades) {
                if (trade.getUpgrade() == upgrade) {
                    trades.remove(trade);
                    if (tradeGroup.getChildren().contains(trade)) {
                        tradeGroup.getChildren().remove(trade);
                        break;
                    }
                }

            }
            relayoutTrades();
        }

        private void relayoutTrades() {
            for (int i = 0; i < trades.size(); i++) {
                trades.get(i).setLayoutX(((upgradeMenu.getTreePane().getWidth() - 4 * Trade.width)) * (i + 1) / (1 + trades.size()));
            }
        }

        private class Trade extends VBox {

            public final static double width = 40;
            private ImageView upgradeIcon, treeIcon;
            private final Upgrade upgrade;

            public Trade(Upgrade upgrade) {
                this.upgrade = upgrade;
                upgradeIcon = new ImageView(upgrade.getIcon());
                treeIcon = new ImageView(upgrade.getOwnerType().getImage());

                upgradeIcon.setPreserveRatio(true);
                upgradeIcon.setFitWidth(width);

                treeIcon.setPreserveRatio(true);
                treeIcon.setFitWidth(width);

                Tooltip description = new Tooltip(upgrade.getDescription());
                Tooltip.install(upgradeIcon, description);

                getChildren().addAll(upgradeIcon, treeIcon);
                setSpacing(2);
                setAlignment(Pos.CENTER);
                setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, new CornerRadii(2), new BorderWidths(1))));

            }

            public Upgrade getUpgrade() {
                return upgrade;
            }

        }
    }

    private static class HeadHuntBox extends ContentBox {

        private HBox hBox;
        private RessourceDisplay bounty;

        public HeadHuntBox() {
            hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            setFillWidth(true);

            bounty = new RessourceDisplay(GameImage.COIN_ICON, 0);
            bounty.setVisible(false);
            getChildren().addAll(hBox, bounty);
            setAlignment(Pos.CENTER);

        }

        public void update(HeadHunt headHunt) {
            boolean newHunt = false;
            bounty.setValue(headHunt.getGoldOnFinished());
            bounty.setVisible(true);
            Vector<Lorbeer.HeadHuntGenerator.Mission> missions = headHunt.getMissions();
            if (missions.size() != hBox.getChildren().size()) {
                newHunt = true;
            } else {
                for (int i = 0; i < missions.size(); i++) {
                    Lorbeer.HeadHuntGenerator.Mission currentMission = missions.get(i);
                    MissionBox currentMissionBox = (MissionBox) hBox.getChildren().get(i);
                    if (!currentMissionBox.isEqual(currentMission.getGameObjectType(), currentMission.getAmount())) {
                        newHunt = true;
                    }
                }
            }

            if (newHunt) {
                hBox.getChildren().clear();

                headHunt.getMissions().forEach((Mission mission) -> {
                    hBox.getChildren().add(new MissionBox(mission.getGameObjectType(), mission.getAmount()));
                });
            } else {
                for (int i = 0; i < headHunt.getMissions().size(); i++) {
                    ((MissionBox) hBox.getChildren().get(i)).update(headHunt.getMissions().get(i).getAmountReady());
                }
            }
        }

        private static class MissionBox extends VBox {

            private ImageView imageView;
            private Label progress;
            private int amount;
            private GameObjectType gameObjectType;

            public MissionBox(GameObjectType type, int amount) {
                this.amount = amount;
                imageView = new ImageView(GameObject.getGameImageFromType(type).getImage());
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(30);
                progress = new Label("0/" + amount);
                gameObjectType = type;
                getChildren().addAll(imageView, progress);
                setAlignment(Pos.CENTER);
            }

            public void update(int newProg) {
                progress.setText(newProg + "/" + amount);
            }

            public boolean isEqual(GameObjectType otherType, int otherAmount) {
                return otherType == gameObjectType && otherAmount == amount;
            }

        }
    }

}
