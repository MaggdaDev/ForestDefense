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

/**
 *
 * @author DavidPrivat
 */
public class Bug extends Mob {

    public final static double DEFAULT_HP = 20;
    public final static double HIT_BOX_RADIUS = (ClientBug.width + ClientBug.height) / 4;

    public final static double DEFAULT_SPEED = 200;
    
    public final static int TOWER_VISION_RANGE = 5;
    
    public final static double DEFAULT_DAMAGE = 10, DEFAULT_DAMAGE_TIME = 0.25;

    public Bug(ServerGame game) {
        super(game, GameObjectType.M_BUG, DEFAULT_HP, DEFAULT_SPEED, new HitBox.CircularHitBox(HIT_BOX_RADIUS, 0, 0), TOWER_VISION_RANGE, DEFAULT_DAMAGE, DEFAULT_DAMAGE_TIME);
        findStartPos();
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.M_BUG.ordinal())),
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
