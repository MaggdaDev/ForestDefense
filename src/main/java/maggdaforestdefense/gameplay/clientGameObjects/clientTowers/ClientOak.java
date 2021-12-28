/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.InformationBubble;
import maggdaforestdefense.gameplay.playerinput.ActiveSkillActivator;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Oak;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.SimplePermaStack;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ClientOak extends ClientTower{
    private double maxHealth;
    private ActiveSkillActivator totalRegenActivator, spontanErhaertungActivator;
    private SimpleStacksBox eichelernteBox;
    public ClientOak(int id, int xIndex, int yIndex, double growingTime) {
        super(id, GameImage.TOWER_OAK_1, GameObjectType.T_OAK, UpgradeSet.OAK_SET, xIndex, yIndex, Oak.DEFAULT_RANGE, Oak.DEFAULT_HEALTH, growingTime, Oak.RANGE_TYPE);

        totalRegenActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_TOTAL_REGEN);
        totalRegenActivator.setOnUsed((MouseEvent e)->{
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.OAK_TOTALREGEN.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });
        
        spontanErhaertungActivator = new ActiveSkillActivator(GameImage.ACTIVE_ICON_SPONTAN_ERHAERTUNG);
        spontanErhaertungActivator.setOnUsed((MouseEvent e)->{
            NetworkCommand command = new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TS, new CommandArgument[]{new CommandArgument("id", super.id), new CommandArgument("skill", ActiveSkill.OAK_SPONTANERHAERTUNG.ordinal())});
            NetworkManager.getInstance().sendCommand(command);
        });
        
        eichelernteBox = new SimpleStacksBox("Eichelernte: +", 0 , " HP");
    }

    @Override
    public void setTier(int tier) {
        tier ++;
        Image image;
        switch(tier) {
            case 4:
                image = GameImage.TOWER_OAK_4.getImage();
                break;
            case 3:
                image = GameImage.TOWER_OAK_3.getImage();
                break;
            case 2:
                image = GameImage.TOWER_OAK_2.getImage();
                break;
            case 1: default:
                image = GameImage.TOWER_OAK_1.getImage();
                break;
        }
        
        setImage(image);
        upgradeMenu.setTreeImage(image);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        healthPoints = updateCommand.getNumArgument("hp");

        updateHealth(healthPoints);
        
        if(updateCommand.containsArgument("totalRegenCooldown")) {
            totalRegenActivator.updateCooldown(updateCommand.getNumArgument("totalRegenCooldown"));
        }
        
        if(updateCommand.containsArgument("spontanErhaertungCooldown")) {
            spontanErhaertungActivator.updateCooldown(updateCommand.getNumArgument("spontanErhaertungCooldown"));
        }
        
        if(isMature) {
            EffectSet e = EffectSet.fromString(updateCommand.getArgument("effects"));
            handleEffects(e);
                    maxHealth = updateCommand.getNumArgument("maxHealth");
        healthBar.setMaxHealth(maxHealth);
        }
        
        if(updateCommand.containsArgument("image")) {
            setImage(GameImage.values()[(int)updateCommand.getNumArgument("image")].getImage());
            updateGrowing(updateCommand.getNumArgument("timeLeft"));
        } else {
            if(!isMature) {
                isMature = true;
            }
        }
        
        
    
    }
    
    @Override
    public void buyUpgrade(int tier, int type) {
        switch(UpgradeSet.OAK_SET.getUpgrade(tier, type)) {
            case OAK_3_1:
                addActiveSkill(totalRegenActivator);
                break;
            case OAK_3_3:
                addActiveSkill(spontanErhaertungActivator);
                break;
            case OAK_3_4:
                addBoxToMenu(eichelernteBox);
                break;
        }
        upgradeMenu.buyUpgrade(tier, type);
    }
    
    
    @Override
    public void updateSimplePermaStacks(NetworkCommand command) {
        SimplePermaStack type = SimplePermaStack.values()[(int)command.getNumArgument("type")];
        double value = command.getNumArgument("value");
        switch(type) {
            case OAK_EICHELERNTE:
                eichelernteBox.updateLabel(Math.round(value));
                generateSimplePermaStackInformationBubble();
                break;
            default:
                Logger.errClient("Received illegal perma stack upgrade for spruce: " + type.name());
                break;
        }
    }
    
}
