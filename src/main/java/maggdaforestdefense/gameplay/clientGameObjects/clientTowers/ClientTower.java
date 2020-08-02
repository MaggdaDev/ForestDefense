/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.ingamemenus.UpgradeMenu;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientTower extends ClientGameObject{
    protected int xIndex, yIndex, range;
    protected UpgradeMenu upgradeMenu;
    protected final UpgradeSet upgradeSet;
    
    protected HealthBar healthBar;
    protected double healthPoints;
    
    public ClientTower(int id, GameImage image, GameObjectType type, UpgradeSet upgrades, int xIndex, int yIndex, int range, double health) {
        super(id, image, type, xIndex * MapCell.CELL_SIZE, yIndex * MapCell.CELL_SIZE);
          this.upgradeSet = upgrades;
        upgradeMenu = new UpgradeMenu(this);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.range = range;
        
        healthPoints = health;
        healthBar = new HealthBar(healthPoints, GameImage.DISPLAY_HEALTH_BOX, GameImage.DISPLAY_HEALTH_BAR_TOWER);
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(healthBar);
      

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
    
    public UpgradeSet getUpgradeSet() {
        return upgradeSet;
    }
    
    @Override
    public void onRemove(){
   
    }

    public void buyUpgrade(int tier, int type) {
        upgradeMenu.buyUpgrade(tier,type);
    }
    
    public abstract void setTier(int tier);
}
