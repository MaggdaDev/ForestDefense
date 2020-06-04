/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;


import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

/**
 *
 * @author DavidPrivat
 */
public class Spruce extends Tower{
    
    double xPos, yPos;
    public Spruce(ServerGame game, double x, double y) {
        super(game);
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
        return null;
    }
    
}
