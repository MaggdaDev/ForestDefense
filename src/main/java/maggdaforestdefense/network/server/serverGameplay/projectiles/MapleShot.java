/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;

/**
 *
 * @author DavidPrivat
 */
public class MapleShot extends Projectile{
    public final static double WIDTH = 10;
    public final static double MAX_RADIUS = 100;
    private double EXPANSION = 50;
    private HitBox.DonutHitBox hitBox;
    
    private double xPos, yPos;
    private double currentRadius;
    private ServerGame serverGame;
    
    
    
    public MapleShot(int id, double xPos, double yPos, Tower owner, Tower.CanAttackSet attackSet, ServerGame serverGame) {
        super(id, GameObjectType.P_MAPLE_SHOT, new HitBox.DonutHitBox(WIDTH, xPos, yPos), owner, attackSet);
        hitBox = new HitBox.DonutHitBox(WIDTH, xPos, yPos);
        currentRadius = 0;
        this.xPos = xPos;
        this.yPos = yPos;
        this.serverGame = serverGame;
     }

    @Override
    public void dealDamage(Mob target) {
        
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.P_MAPLE_SHOT.ordinal())),
            new CommandArgument("id", String.valueOf(id)),
            new CommandArgument("radius", String.valueOf(currentRadius))};
    }
    
    public boolean updateFlight(double timeElapsed) {
        currentRadius += timeElapsed * EXPANSION;
        hitBox.setInnerRadius(currentRadius);
        hitBox.updatePos(xPos, yPos);
        return currentRadius < MAX_RADIUS;
    }
    
    

    @Override
    public NetworkCommand update(double timeElapsed) {
        boolean stillExists = updateFlight(timeElapsed);

        collision(serverGame.getMobs());
        if (stillExists) {
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
                new CommandArgument("y", String.valueOf(yPos)),
                new CommandArgument("id", String.valueOf(id)),
            new CommandArgument("radius", String.valueOf(currentRadius))});
        } else {
            return null;
        }
    }
}
