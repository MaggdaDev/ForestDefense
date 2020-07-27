/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.projectiles.SpruceShot;

/**
 *
 * @author DavidPrivat
 */
public class Spruce extends Tower {

    public final static int DEFAULT_RANGE = 2;              //map cells
    public final static double DEFAULT_SHOOT_TIME = 1;        //per sec
    public final static int DEFAULT_PRIZE = 1;

    //Balancing stats
    private int range = DEFAULT_RANGE;

    double xPos, yPos;
    double shootTimer = 0, shootTime = DEFAULT_SHOOT_TIME;
    
    // UPGRADE CONSTANTS
    public final static double FICHTEN_WUT_MULTIPLIER = 0.999;
    public final static double NADEL_STAERKUNG_MULTIPLIER = 3;
    
    // UPGRADE VARIABLES
    private double monoculturalMultiplier = 1;  // *deltaT => >1
    private int spruceCounter = 0;
    
    private double rasendeFichteMultiplier = 1;
    private int rasendeFichteKillCounter = 0;
    
    private double aufruestungMultiplier = 1;
    private int aufruestungCounter = 0;


    
    public Spruce(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_SPRUCE, DEFAULT_PRIZE, UpgradeSet.SPRUCE_SET);
        xPos = x;
        yPos = y;
        
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{
            new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.T_SPRUCE.ordinal())),
            new CommandArgument("id", String.valueOf(id))
        };
    }

    @Override
    public NetworkCommand update(double timeElapsed) {

        // Shooting
        shootTimer += timeElapsed * monoculturalMultiplier * rasendeFichteMultiplier * aufruestungMultiplier;
        if (shootTimer > shootTime) {
            Mob target = findTarget(range);
            if (target != null) {
                shootTimer = 0;
                shoot(target);
            }
        }
        
        // Upgrades
        performUpgradesOnUpdate();

        return null;
    }

    private void shoot(Mob target) {
        serverGame.addProjectile(new SpruceShot(serverGame.getNextId(), serverGame, getCenterX(), getCenterY(), target, this));
        performUpgradesOnShoot();
    }
    
    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
        
        switch(upgrade) {
            case SPRUCE_1_2:        // Fichtenmonokultur
                onUpdate.add(()->{
                   spruceCounter = 0;
                   serverGame.getGameObjects().forEach((String key, GameObject gameObject) -> {
                       if(gameObject.getGameObjectType() == (GameObjectType.T_SPRUCE)) {
                           spruceCounter++;
                       }
                   });
                   monoculturalMultiplier = Math.sqrt((double)spruceCounter);
                });
                break;
            case SPRUCE_2_1:        // AUFRUESTUNG
                onShoot.add(()->{
                   aufruestungCounter = 0;
                   serverGame.getMobs().forEach((String key, Mob mob)->{
                       if(isInRange(mob, range)) {
                           aufruestungCounter++;
                       }
                   });
                   aufruestungMultiplier = Math.sqrt(0.3 * (int)aufruestungCounter + 1.0d);
                });
                break;
            case SPRUCE_2_2:        // Fichtenwut
                onShoot.add(()->{
                   shootTime *= FICHTEN_WUT_MULTIPLIER;
                });
                break;
                
            case SPRUCE_3_1:        //Serienmörder
                onKill.add(()->{
                    shootTimer = shootTime;
                });
                break;
                
            case SPRUCE_3_2:        //rasende fichte
                onKill.add(()->{
                    rasendeFichteKillCounter++;
                    rasendeFichteMultiplier = Math.sqrt(0.1 * (int)rasendeFichteKillCounter + 1.0d);
                });
                break;
            
        }
    }
    
    
  

}
