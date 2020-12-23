/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;
import maggdaforestdefense.network.server.serverGameplay.Map;

/**
 *
 * @author David
 */
public class Hirschkaefer extends Bug{
    
   

    public final static double HP = 500, SPEED = 25, DAMAGE = 25, ATTACK_TIME = 2, ARMOR = 5;
    public final static int TOWER_VISION_RANGE = Map.MAP_SIZE;
    public final static int DIRT_DISTANCE_WEIGHT = 1, WATER_DISTANCE_WEIGHT = 2, SAND_DISTANCE_WEIGHT = 1, STONE_DISTANCE_WEIGHT = 5;
    public final static Mob.MovementType MOVEMENT_TYPE = Mob.MovementType.WALK;

    public final static MapDistanceSet MAP_DISTANCES = new MapDistanceSet(WATER_DISTANCE_WEIGHT, SAND_DISTANCE_WEIGHT, DIRT_DISTANCE_WEIGHT, STONE_DISTANCE_WEIGHT);

    public Hirschkaefer(ServerGame serverGame) {
        super(serverGame, HP, SPEED, TOWER_VISION_RANGE, DAMAGE, ATTACK_TIME, MAP_DISTANCES, ARMOR, MOVEMENT_TYPE, GameObjectType.M_HIRSCHKAEFER);
    }
    
  


}
