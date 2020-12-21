/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

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
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Lorbeer;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower.RangeType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientLorbeer extends ClientTower{
    public final static RangeType RANGE_TYPE = Lorbeer.RANGE_TYPE;
    
    private ActiveSkillActivator attackActivator, sellActivator;
    
    private int lorbeerAmount = 0, oldLorbeerAmount = 0, maxLorbeerAmount = 1;
    private int coinsPerLorbeer = 0;
    private RessourceDisplay lorbeerDisplay, potentialGoldDisplay;
    
    private InformationPopup fullPopup;
    
    public ClientLorbeer(int id, int xIndex, int yIndex, double health) {
        super(id, GameImage.TOWER_LORBEER_1, GameObjectType.T_LORBEER, UpgradeSet.LORBEER_SET, xIndex, yIndex, Lorbeer.DEFAULT_RANGE, Lorbeer.DEFAULT_HEALTH, Lorbeer.DEFAULT_GROWING_TIME, RANGE_TYPE);
        
        lorbeerDisplay = new RessourceDisplay(GameImage.LORBEER_ICON, 0);
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
        
        upgradeMenu.getTreePane().getChildren().add(new HBox(lorbeerDisplay, new Label("="), potentialGoldDisplay));
        
        fullPopup = new InformationPopup("VOLL!", (xIndex+0.5) * MapCell.CELL_SIZE, (yIndex+0.1) * MapCell.CELL_SIZE);
        
        onRemove.add(()->{
            Game.removeGamePlayNode(fullPopup);
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
            
            
            // LORBEERAMOUNT
            String[] lorbeeren = updateCommand.getArgument("lorbeeren").split("-");
            lorbeerAmount = Integer.parseInt(lorbeeren[0]);
            maxLorbeerAmount = Integer.parseInt(lorbeeren[1]);
            coinsPerLorbeer = (int)(updateCommand.getNumArgument("coinsPerLorbeer"));
            
            attackActivator.updateCooldown(updateCommand.getNumArgument("attackCooldown"));
            
            
            if(oldLorbeerAmount > lorbeerAmount) {
                Game.addGamePlayNode(new InformationBubble("+" + ((oldLorbeerAmount - lorbeerAmount) * coinsPerLorbeer), InformationBubble.InformationType.GOLD, getCenterX(), getCenterY()));
            } else if(oldLorbeerAmount < lorbeerAmount) {
                 Game.addGamePlayNode(new InformationBubble("+" + (lorbeerAmount - oldLorbeerAmount), InformationBubble.InformationType.LORBEER, getCenterX(), getCenterY()));
            }
            
            oldLorbeerAmount = lorbeerAmount;
            
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
        
        lorbeerDisplay.setValue(lorbeerAmount);
        potentialGoldDisplay.setValue(lorbeerAmount * coinsPerLorbeer);
        if(lorbeerAmount > 0) {
            sellActivator.setUsable(true);
        } else {
            sellActivator.setUsable(false);
        }
        
        fullPopup.setVisible(lorbeerAmount >= maxLorbeerAmount);
        
        
            super.range = updateCommand.getNumArgument("range");
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

}
