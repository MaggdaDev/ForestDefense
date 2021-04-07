/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.spawning;

import java.util.Vector;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class WaveGenerator {
    
    private int[] strengths;
    private GameObjectType[] mobs;
    public WaveGenerator() {
        
        mobs =  GameObject.getMobs();
        strengths = new int[mobs.length];
        for(int i = 0; i < mobs.length; i++) {
            strengths[i] = getStrength(mobs[i]);
        }
    }
    
    public MobWave generateWave(int round) {

        int totStrength = getStrengthFromRound(round);
        
        Vector<Spawnable> spawnables = new Vector<>();
        while(totStrength > 0) {

            int randIndex = (int)(Math.random() * mobs.length);
            if(round <= 5) {
                randIndex = 0;
            }
            int amount = (int)(totStrength/strengths[randIndex]);
            if(amount > 10) {
                amount = 5;
            }
            for(int i = 0; i < amount; i++) {
                spawnables.add(new Spawnable(mobs[randIndex], Math.pow((double)getStrength(mobs[randIndex]), 0.3)));
            }
            totStrength -= amount * strengths[randIndex];
        }
        MobWave wave = new MobWave(spawnables);
        
        return wave;
    }
    
    private int getStrengthFromRound(int round) {
        return (int)(3.0d + (double)round + 0.1d * Math.pow((double)round, 2.0d));
    }
    
    private int getStrength(GameObjectType type) {
        switch(type) {
            case M_BLATTLAUS:
                return 1;
            case M_WANDERLAUFER:
                return 8;
            case M_HIRSCHKAEFER:
                return 25;
            case M_MARIENKAEFER:
                return 5;
            case M_BORKENKAEFER:
                return 1o;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
