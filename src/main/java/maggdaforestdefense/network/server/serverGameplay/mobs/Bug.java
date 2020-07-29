/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBug;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;

/**
 *
 * @author DavidPrivat
 */
public abstract class Bug extends Mob {


    public final static double HIT_BOX_RADIUS = (ClientBug.width + ClientBug.height) / 4;

    

    public Bug(ServerGame game, double hp, double speed, int towerVisionRange, double damage, double attackTime, MapDistanceSet distanceSet) {
        super(game, GameObjectType.M_BUG, hp, speed, new HitBox.CircularHitBox(HIT_BOX_RADIUS, 0, 0), towerVisionRange, damage, attackTime, distanceSet);
        findStartPos();
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.M_BUG.ordinal())),
            new CommandArgument("hp", healthPoints),
            new CommandArgument("id", String.valueOf(id))};
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        if (updateAlive()) {
            updateIndexPosition();
            updateMovement(timeElapsed);
            updateDamageTarget(timeElapsed);
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
                new CommandArgument("id", String.valueOf(id)),
                new CommandArgument("x", String.valueOf(xPos)),
                new CommandArgument("y", String.valueOf(yPos)),
                new CommandArgument("hp", String.valueOf(healthPoints))});
        } else {
            return null;
        }

    }

}
