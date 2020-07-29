/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

/**
 *
 * @author DavidPrivat
 */
public class MapDistanceSet {
    private double waterWeight, sandWeight, dirtWeight, stoneWeight;
    
    public MapDistanceSet(double water, double sand, double dirt, double stone) {
        waterWeight = water;
        sandWeight = sand;
        stoneWeight = stone;
        dirtWeight = dirt;
    }
    
    public double getWater() {
        return waterWeight;
    }
    
    public double getStone() {
        return stoneWeight;
    }
    
    public double getSand() {
        return sandWeight;
    }
    
    public double getDirt() {
        return dirtWeight;
    }
}
