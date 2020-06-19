/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

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

    public ClientSpruce(int id, int xIndex, int yIndex) {
        super(id, GameImage.TOWER_SPRUCE_1, GameObjectType.T_SPRUCE, UpgradeSet.SPRUCE_SET, xIndex, yIndex, Spruce.DEFAULT_RANGE);
        setPreserveRatio(true);
        setFitHeight(100);
        
        setLayoutX(xIndex*MapCell.CELL_SIZE);
        setLayoutY(yIndex*MapCell.CELL_SIZE);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        
    }
    
    
    
}
