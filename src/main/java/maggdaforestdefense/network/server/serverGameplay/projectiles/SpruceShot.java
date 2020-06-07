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
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;

/**
 *
 * @author DavidPrivat
 */
public class SpruceShot extends GameObject{
    
    private Spruce owner;
    private double xPos, yPos, xSpd, ySpd, totalSpeed;
    
    public SpruceShot(int id, double x, double y, double xSpeed, double ySpeed) {
        super(id, GameObjectType.P_SPRUCE_SHOT);
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
