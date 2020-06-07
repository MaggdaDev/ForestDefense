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
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class Spruce extends Tower{
    public final static int DEFAULT_RANGE = 2;
    
    
    public final static UpgradeSet upgradeSet = new UpgradeSet(new Upgrade[][]{
    new Upgrade[]{
        Upgrade.SPRUCE_1_1
    }});
    
    
    private int range = DEFAULT_RANGE;
    
    double xPos, yPos;
    public Spruce(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_SPRUCE);
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
