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
    
    public ClientLorbeer(int id, int xIndex, int yIndex, double health) {
        super(id, GameImage.TOWER_LORBEER_1, GameObjectType.T_LORBEER, UpgradeSet.LORBEER_SET, xIndex, yIndex, Lorbeer.DEFAULT_RANGE, Lorbeer.DEFAULT_HEALTH, Lorbeer.DEFAULT_GROWING_TIME, RANGE_TYPE);
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
        healthBar.update(xPos + 0.5*MapCell.CELL_SIZE, yPos, healthPoints);
        
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
        
        
            super.range = updateCommand.getNumArgument("range");
    }
    
}
