/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;

/**
 *
 * @author David
 */
public class Marienkaefer extends Bug{
    public final static double HP = 125, SPEED = 100, DAMAGE = 10, ATTACK_TIME = 1, ARMOR = 0;
    public final static int TOWER_VISION_RANGE = 3;
    public final static int DIRT_DISTANCE_WEIGHT = 1, WATER_DISTANCE_WEIGHT = 1, SAND_DISTANCE_WEIGHT = 100, STONE_DISTANCE_WEIGHT = 200;
    public final static Mob.MovementType MOVEMENT_TYPE = Mob.MovementType.WALK;

    public final static MapDistanceSet MAP_DISTANCES = new MapDistanceSet(WATER_DISTANCE_WEIGHT, SAND_DISTANCE_WEIGHT, DIRT_DISTANCE_WEIGHT, STONE_DISTANCE_WEIGHT);

    public Marienkaefer(ServerGame serverGame, int waveIndex) {
        super(serverGame, HP * Bug.calculateHpWaveFactor(waveIndex), SPEED, TOWER_VISION_RANGE, DAMAGE, ATTACK_TIME, MAP_DISTANCES, ARMOR, MOVEMENT_TYPE, GameObjectType.M_MARIENKAEFER);
    }
    
     @Override
    public NetworkCommand update(double timeElapsed) {
        if (!updateAlive()) {
            sentDeathToClient = true;
        }
        int oldXIndex = currentXIndex;
        int oldYIndex = currentYIndex;
        
        updateIndexPosition();
        updateMovement(timeElapsed);
        updateDamageTarget(timeElapsed);
        updateEffects(timeElapsed);
        
        switch(serverGame.getMap().getCells()[super.currentXIndex][super.currentYIndex].getCellType()) {
            case WATER:
                if(super.movementType != MovementType.FLY) {
                    super.searchForTowers(serverGame.getMap().getCells()[oldXIndex][oldYIndex].getPathCell());
                }
                super.movementType = MovementType.FLY;
                break;
            default:
                super.movementType = MovementType.WALK;
                break;
        }
        
        return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
            new CommandArgument("id", String.valueOf(id)),
            new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("hp", String.valueOf(healthPoints)),
            new CommandArgument("movement", movementType.ordinal()),
            new CommandArgument("effects", effectSet.toString())});

    }
}
