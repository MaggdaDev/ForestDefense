/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import javafx.scene.image.Image;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Oak;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientOak extends ClientTower{
    
    public ClientOak(int id, int xIndex, int yIndex, double growingTime) {
        super(id, GameImage.TOWER_OAK_1, GameObjectType.T_OAK, UpgradeSet.OAK_SET, xIndex, yIndex, Oak.DEFAULT_RANGE, Oak.DEFAULT_HEALTH, growingTime, Oak.RANGE_TYPE);


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
        
        if(isMature) {
            EffectSet e = EffectSet.fromString(updateCommand.getArgument("effects"));
            handleEffects(e);
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
    
}
