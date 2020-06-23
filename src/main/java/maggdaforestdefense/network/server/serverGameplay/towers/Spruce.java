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
    public final static int DEFAULT_PRIZE = 100;

   private Vector<Upgrade> upgrades;

    private int range = DEFAULT_RANGE;

    double xPos, yPos;
    double shootTimer = 0, shootTime = DEFAULT_SHOOT_TIME;

    public Spruce(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_SPRUCE, DEFAULT_PRIZE, UpgradeSet.SPRUCE_SET);
        xPos = x;
        yPos = y;
        upgrades = new Vector<>();
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
        shootTimer += timeElapsed;
        if (shootTimer > shootTime) {
            Mob target = findTarget(range);
            if (target != null) {
                shootTimer = 0;
                shoot(target);
            }
        }

        return null;
    }

    private void shoot(Mob target) {
        serverGame.addProjectile(new SpruceShot(serverGame.getNextId(), serverGame, getCenterX(), getCenterY(), target));
    }
    
    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
    }
  

}
