/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class ClientSpruce extends ClientTower {

    public ClientSpruce(int id, double xPos, double yPos) {
        super(id, GameImage.TOWER_SPRUCE_1, GameObjectType.T_SPRUCE);
        setPreserveRatio(true);
        setFitHeight(100);
        
        setLayoutX(xPos);
        setLayoutY(yPos);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        
    }
}
