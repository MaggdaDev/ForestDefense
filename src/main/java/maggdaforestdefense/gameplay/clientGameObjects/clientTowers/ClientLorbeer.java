/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import java.util.Vector;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

/**
 *
 * @author DavidPrivat
 */
public class ClientLorbeer extends ClientTower{
    public final static RangeType RANGE_TYPE = Lorbeer.RANGE_TYPE;
    
    private ActiveSkillActivator attackActivator, sellActivator, prestigeActivator;
    
    private int lorbeerAmount = 0, oldLorbeerAmount = 0, maxLorbeerAmount = 1;
    private int coinsPerLorbeer = 0, oldCoinsPerLorbeer = 0;
    private RessourceDisplay potentialGoldDisplay;
    private RessourceDisplay.LorbeerDisplay lorbeerDisplay;
    
    private InformationPopup fullPopup, headHuntFinishedPopup;
    
    private HeadHunt headHunt, oldHeadHunt;
    private HeadHuntBox headHuntBox;
    
    private HBox goldLorbeerHBox;
    
    public ClientLorbeer(int id, int xIndex, int yIndex, double health) {
        super(id, GameImage.TOWER_LORBEER_1, GameObjectType.T_LORBEER, UpgradeSet.LORBEER_SET, xIndex, yIndex, Lorbeer.DEFAULT_RANGE, Lorbeer.DEFAULT_HEALTH, Lorbeer.DEFAULT_GROWING_TIME, RANGE_TYPE);
        
        lorbeerDisplay = new RessourceDisplay.LorbeerDisplay(GameImage.LORBEER_ICON, lorbeerAmount, maxLorbeerAmount);
        potentialGoldDisplay = new RessourceDisplay(GameImage.COIN_ICON, 0);
        
        attackActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_ATTACK);
        attackActivator.setOnUsed((MouseEvent e)->{
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_ATTACK.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });
        
        sellActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_SELL);
        sellActivator.setOnUsed((MouseEvent e)->{
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_SELL.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });
        
        prestigeActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_PRESTIGE);
        prestigeActivator.setOnUsed((MouseEvent e)->{
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.LORBEER_PRESTIGE.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });
        
        goldLorbeerHBox = new HBox(lorbeerDisplay, new Label("="), potentialGoldDisplay);
        upgradeMenu.getTreePane().getChildren().add(goldLorbeerHBox);
        
        fullPopup = new InformationPopup("VOLL!", (xIndex+0.5) * MapCell.CELL_SIZE, (yIndex+0.1) * MapCell.CELL_SIZE);
        headHuntFinishedPopup = new InformationPopup("ABGESCHLOSSEN!", (xIndex+0.5) * MapCell.CELL_SIZE, (yIndex+0.1) * MapCell.CELL_SIZE);
        headHuntFinishedPopup.setVisible(false);
        
        onRemove.add(()->{
            Game.removeGamePlayNode(fullPopup);
            Game.removeGamePlayNode(headHuntFinishedPopup);
        });

    }


    @Override
    public void setTier(int tier) {
        tier ++;
        Image image;
        switch(tier) {
            case 4:
                image = GameImage.TOWER_LORBEER_4.getImage();
                break;
            case 3:
                image = GameImage.TOWER_LORBEER_3.getImage();
                break;
            case 2:
                image = GameImage.TOWER_LORBEER_2.getImage();
                break;
            case 1: default:
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
        
        if(isMature) {
            EffectSet e = EffectSet.fromString(updateCommand.getArgument("effects"));
            handleEffects(e);
            
            attackActivator.updateCooldown(updateCommand.getNumArgument("attackCooldown"));
            
            // LORBEERAMOUNT
            if(updateCommand.containsArgument("lorbeeren") && updateCommand.containsArgument("coinsPerLorbeer")) {
            String[] lorbeeren = updateCommand.getArgument("lorbeeren").split("-");
            lorbeerAmount = Integer.parseInt(lorbeeren[0]);
            maxLorbeerAmount = Integer.parseInt(lorbeeren[1]);
            coinsPerLorbeer = (int)(updateCommand.getNumArgument("coinsPerLorbeer"));
            
            
            
            
            
            if(oldLorbeerAmount > lorbeerAmount) {
                if(oldCoinsPerLorbeer < coinsPerLorbeer) {
                    
                } else {
                    Game.addGamePlayNode(new InformationBubble("+" + ((oldLorbeerAmount - lorbeerAmount) * coinsPerLorbeer), InformationBubble.InformationType.GOLD, getCenterX(), getCenterY()));
                }
            } else if(oldLorbeerAmount < lorbeerAmount) {
                 Game.addGamePlayNode(new InformationBubble("+" + (lorbeerAmount - oldLorbeerAmount), InformationBubble.InformationType.LORBEER, getCenterX(), getCenterY()));
            }
            
            oldLorbeerAmount = lorbeerAmount;
            oldCoinsPerLorbeer = coinsPerLorbeer;
            lorbeerDisplay.setValue(lorbeerAmount);
                lorbeerDisplay.setMaxLorbeer(maxLorbeerAmount);
            potentialGoldDisplay.setValue(lorbeerAmount * coinsPerLorbeer);
            if(lorbeerAmount > 0) {
             sellActivator.setUsable(true);
             } else {
                sellActivator.setUsable(false);
            }
        
            fullPopup.setVisible(lorbeerAmount >= maxLorbeerAmount);
            prestigeActivator.setUsable(lorbeerAmount >= maxLorbeerAmount);
            }
            
        }
        
        if(updateCommand.containsArgument("image")) {
            setImage(GameImage.values()[(int)updateCommand.getNumArgument("image")].getImage());
            updateGrowing(updateCommand.getNumArgument("timeLeft"));
        } else {
            if(!isMature) {
                isMature = true;
                onMatured();
                
            }
        }
        
        if(updateCommand.containsArgument("headhunt")) {
            headHunt = HeadHunt.fromString(updateCommand.getArgument("headhunt"));
            headHuntBox.update(headHunt);
            
        } 
        if(headHunt != null) {
            boolean headHuntFinished = headHunt.isFinished();
            sellActivator.setUsable(headHuntFinished);
            headHuntFinishedPopup.setVisible(headHuntFinished);
            
            if(oldHeadHunt != null && oldHeadHunt.isFinished() && !headHunt.isFinished()) {
                Game.addGamePlayNode(new InformationBubble("+" + oldHeadHunt.getGoldOnFinished(), InformationBubble.InformationType.GOLD, getCenterX(), getCenterY()));
            }
            
            oldHeadHunt = headHunt;
        }
        
        
        
            super.range = updateCommand.getNumArgument("range");
    }
    
    @Override
    public void buyUpgrade(int tier, int type) {
        if(UpgradeSet.LORBEER_SET.getUpgrade(tier, type) == Upgrade.LORBEER_3_2){
            addActiveSkill(prestigeActivator);
        }
        if(UpgradeSet.LORBEER_SET.getUpgrade(tier, type) == Upgrade.LORBEER_3_3) {
            headHuntBox = new HeadHuntBox();
            upgradeMenu.getTreePane().getChildren().remove(goldLorbeerHBox);
            upgradeMenu.getTreePane().getChildren().add(headHuntBox);
        }
        upgradeMenu.buyUpgrade(tier, type);
    }
    
    @Override
    public void performActiveSkill(ActiveSkill skill) {
        switch(skill) {
            case LORBEER_ATTACK:
                shoot();
                break;
        }
    }
    
    public void shoot() {
        new LorbeerShot(xPos + 0.5*MapCell.CELL_SIZE, yPos + 0.5*MapCell.CELL_SIZE, (0.5+range)*(MapCell.CELL_SIZE));
    }
    
    @Override
    public void onMatured() {
        addActiveSkill(attackActivator);
        addActiveSkill(sellActivator);
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
            if(missions.size() != hBox.getChildren().size()) {
                newHunt = true;
            } else {
                for(int i = 0; i < missions.size(); i++) {
                    Lorbeer.HeadHuntGenerator.Mission currentMission = missions.get(i);
                    MissionBox currentMissionBox = (MissionBox)hBox.getChildren().get(i);
                    if(!currentMissionBox.isEqual(currentMission.getGameObjectType(), currentMission.getAmount())) {
                       newHunt = true;
                    }
                }
            }
            
            if(newHunt) {
                hBox.getChildren().clear();
                
                headHunt.getMissions().forEach((Mission mission)->{
                    hBox.getChildren().add(new MissionBox(mission.getGameObjectType(), mission.getAmount()));
                });
            } else {
                for(int i = 0; i < headHunt.getMissions().size(); i++) {
                    ((MissionBox)hBox.getChildren().get(i)).update(headHunt.getMissions().get(i).getAmountReady());
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
