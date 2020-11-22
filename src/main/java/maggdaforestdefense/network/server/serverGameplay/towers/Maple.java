/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.projectiles.MapleShot;
import maggdaforestdefense.network.server.serverGameplay.projectiles.SpruceShot;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */

public class Maple extends Tower{
    
    //BALANCING
    public final static int DEFAULT_PRIZE = 1;
    public final static double DEFAULT_HEALTH = 100;
    public final static double DEFAULT_REGEN = 1;
    public final static int DEFAULT_RANGE = 2;
    public final static double GROWING_TIME = 3;
    public final static boolean CAN_ATTACK_DIGGING = false, CAN_ATTACK_WALKING = true, CAN_ATTACK_FLYING = false;


    private double shootTime = 2, shootTimer = 0;
    public Maple(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_MAPLE, DEFAULT_PRIZE, UpgradeSet.SPRUCE_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(CAN_ATTACK_DIGGING, CAN_ATTACK_WALKING, CAN_ATTACK_FLYING), GROWING_TIME);
    }
    @Override
    public void addUpgrade(Upgrade upgrade) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        if (!checkAlive()) {
            return null;
        }

        if (!isMature) {
            GameImage currImage = updateGrowing(timeElapsed);
            isMature = growingAnimation.isFinished();
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
                new CommandArgument("id", id),
                new CommandArgument("hp", healthPoints),
                new CommandArgument("image", currImage.ordinal()),
                new CommandArgument("timeLeft", growingAnimation.getTimeLeft())
            });
        } else {

            // Regen
            updateRegen(timeElapsed);

            // Shooting
            shootTimer += timeElapsed;
            
            if(shootTimer > shootTime) {
                shootTimer = 0;
                shoot();
            }
            
            // Health
            // Upgrades
            performUpgradesOnUpdate();

            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("hp", healthPoints)});
        }
    }
    
    private void shoot() {
        serverGame.addProjectile(new MapleShot(serverGame.getNextId(), getCenterX(), getCenterY(), this, canAttackSet, serverGame));
        performUpgradesOnShoot();
    }
    
}

