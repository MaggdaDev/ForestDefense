/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;

/**
 *
 * @author David
 */
public class Herkuleskaefer extends Bug{
    
   

    public final static double HP = 50, SPEED = 100, DAMAGE = 15, ATTACK_TIME = 2, ARMOR = 20;
    public final static int TOWER_VISION_RANGE = 3;
    public final static int DIRT_DISTANCE_WEIGHT = 1, WATER_DISTANCE_WEIGHT = 5, SAND_DISTANCE_WEIGHT = 2, STONE_DISTANCE_WEIGHT = 5;
    public final static Mob.MovementType MOVEMENT_TYPE = Mob.MovementType.WALK;

    public final static MapDistanceSet MAP_DISTANCES = new MapDistanceSet(WATER_DISTANCE_WEIGHT, SAND_DISTANCE_WEIGHT, DIRT_DISTANCE_WEIGHT, STONE_DISTANCE_WEIGHT);

    public Herkuleskaefer(ServerGame serverGame) {
        super(serverGame, HP, SPEED, TOWER_VISION_RANGE, DAMAGE, ATTACK_TIME, MAP_DISTANCES, ARMOR, MOVEMENT_TYPE, GameObjectType.M_HERKULESKAEFER);
    }


}
