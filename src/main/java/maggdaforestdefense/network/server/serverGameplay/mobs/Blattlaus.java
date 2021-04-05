/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;

/**
 *
 * @author David
 */
public class Blattlaus extends Bug{
    
    public final static double HP = 50, SPEED = 300, DAMAGE = 5, ATTACK_TIME = 1, ARMOR = 0;
    public final static int TOWER_VISION_RANGE = 0;
    public final static int DIRT_DISTANCE_WEIGHT = 1, WATER_DISTANCE_WEIGHT = 100, SAND_DISTANCE_WEIGHT = 10, STONE_DISTANCE_WEIGHT = 1000;
    public final static MovementType MOVEMENT_TYPE = MovementType.WALK;

    public final static MapDistanceSet MAP_DISTANCES = new MapDistanceSet(WATER_DISTANCE_WEIGHT, SAND_DISTANCE_WEIGHT, DIRT_DISTANCE_WEIGHT, STONE_DISTANCE_WEIGHT);

    public Blattlaus(ServerGame serverGame) {
        super(serverGame, HP, SPEED, TOWER_VISION_RANGE, DAMAGE, ATTACK_TIME, MAP_DISTANCES, ARMOR, MOVEMENT_TYPE, GameObjectType.M_BLATTLAUS);
    }
    
   

}
