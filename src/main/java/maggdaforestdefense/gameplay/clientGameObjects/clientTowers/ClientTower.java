/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.ingamemenus.UpgradeMenu;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientTower extends ClientGameObject{
    protected int xIndex, yIndex, range;
    protected UpgradeMenu upgradeMenu;
    
    public ClientTower(int id, GameImage image, GameObjectType type, int xIndex, int yIndex, int range) {
        super(id, image, type, xIndex * MapCell.CELL_SIZE, yIndex * MapCell.CELL_SIZE);
        upgradeMenu = new UpgradeMenu(this);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.range = range;
    }
    
    public int getXIndex() {
        return xIndex;
    }
    
    public int getYIndex() {
        return yIndex;
    }
    
    public UpgradeMenu getUpgradeMenu() {
        return upgradeMenu;
    }
    
    public int getRange() {
        return range;
    }
}
