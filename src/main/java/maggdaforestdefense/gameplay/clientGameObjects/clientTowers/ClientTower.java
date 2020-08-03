/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import javafx.scene.Parent;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.ingamemenus.GrowingWaitingMenu;
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
    protected GrowingWaitingMenu growingMenu;
    protected final UpgradeSet upgradeSet;
    
    protected HealthBar healthBar;
    protected double healthPoints;
    
    protected double growingTime;
    
    protected boolean isMature = false;
    
    protected ClientMapCell mapCell;
    
    public ClientTower(int id, GameImage image, GameObjectType type, UpgradeSet upgrades, int xIndex, int yIndex, int range, double health, double growingTime) {
        super(id, image, type, xIndex * MapCell.CELL_SIZE, yIndex * MapCell.CELL_SIZE);
        mapCell = Game.getInstance().getGameScreen().getMap().getCells()[xIndex][yIndex];
        
          this.upgradeSet = upgrades;
        upgradeMenu = new UpgradeMenu(this);
        growingMenu = new GrowingWaitingMenu(image, upgradeMenu.getTreeName(), growingTime);
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        this.range = range;
        this.growingTime = growingTime;
        
        healthPoints = health;
        healthBar = new HealthBar(healthPoints, GameImage.DISPLAY_HEALTH_BOX, GameImage.DISPLAY_HEALTH_BAR_TOWER);
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(healthBar);
      

    }
    
    protected void updateGrowing(double timeLeft) {
        growingMenu.update(timeLeft);
        if(isMature != true && timeLeft == 0) {
            isMature = true;
            mapCell.notifyTreeMature();
        }
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
    
    public GrowingWaitingMenu getGrowingWaitingMenu() {
        return growingMenu;
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
