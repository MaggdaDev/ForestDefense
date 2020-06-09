/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import javafx.scene.image.Image;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientMob extends ClientGameObject{
    
    protected HealthBar healthBar;
    public ClientMob(int id, GameImage image, GameObjectType type, double x, double y, double maxHealth) {
        super(id, image, type, x, y);
        
        healthBar = new HealthBar(maxHealth);
        
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(healthBar);
    }
    
    protected void updateHealth(double h) {
        healthBar.update(xPos + getFitWidth()/2, yPos, h);
    }
    
    public HealthBar getHealthBar() {
        return healthBar;
    }
    
    @Override
    public void onRemove() {
        if(Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().contains(healthBar)) {
            Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().remove(healthBar);
        }
        
    }
    
    
}
