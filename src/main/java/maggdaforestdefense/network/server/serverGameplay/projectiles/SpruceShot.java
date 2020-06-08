/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.projectiles;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public class SpruceShot extends ConstantFlightProjectile {

    private final static double DEFAULT_SPEED = 1000;
    private final static int DEFAULT_RANGE = Spruce.DEFAULT_RANGE;

 

    public SpruceShot(int id, ServerGame game, double x, double y, Mob target) {
        super(id, GameObjectType.P_SPRUCE_SHOT, DEFAULT_RANGE, target, x, y, DEFAULT_SPEED, game);
        serverGame = game;
        xPos = x;
        yPos = y;

        calculateSpeed(target);

    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.P_SPRUCE_SHOT.ordinal())),
            new CommandArgument("id", String.valueOf(id))};
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        boolean stillExists = updateFlight(timeElapsed);

        if (stillExists) {
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("x", String.valueOf(xPos)),
                new CommandArgument("y", String.valueOf(yPos)),
                new CommandArgument("id", String.valueOf(id))});
        } else {
            return null;
        }
    }
}
