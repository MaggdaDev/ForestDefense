/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import javafx.scene.image.Image;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientSpruce extends ClientTower {

    public ClientSpruce(int id, int xIndex, int yIndex, double growingTime) {
        super(id, GameImage.TOWER_SPRUCE_1, GameObjectType.T_SPRUCE, UpgradeSet.SPRUCE_SET, xIndex, yIndex, Spruce.DEFAULT_RANGE, Spruce.HEALTH, growingTime);
        setPreserveRatio(true);
        setFitHeight(100);
        
        setLayoutX(xIndex*MapCell.CELL_SIZE);
        setLayoutY(yIndex*MapCell.CELL_SIZE);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        healthPoints = updateCommand.getNumArgument("hp");
        healthBar.update(xPos + 0.5*MapCell.CELL_SIZE, yPos, healthPoints);
        
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
    public void setTier(int tier) {
        tier ++;
        Image image;
        switch(tier) {
            case 4:
                image = GameImage.TOWER_SPRUCE_4.getImage();
                break;
            case 3:
                image = GameImage.TOWER_SPRUCE_3.getImage();
                break;
            case 2:
                image = GameImage.TOWER_SPRUCE_2.getImage();
                break;
            case 1: default:
                image = GameImage.TOWER_SPRUCE_1.getImage();
                break;
        }
        
        setImage(image);
        upgradeMenu.setTreeImage(image);
    }
    
    @Override
    public void onRemove() {
        if(Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().contains(healthBar)) {
            Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().remove(healthBar);
        }
        
    }
    
    
    
    
}
