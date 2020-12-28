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
        if(round == 0) {
            round = 1;
        }
        int totStrength = getStrengthFromRound(round);
        
        Vector<Spawnable> spawnables = new Vector<>();
        while(totStrength > 0) {
            int randIndex = (int)(Math.random() * mobs.length);
            int amount = (int)(totStrength/strengths[randIndex]);
            if(amount > 5) {
                amount = 5;
            }
            for(int i = 0; i < amount; i++) {
                spawnables.add(new Spawnable(mobs[randIndex], 5/(amount)));
            }
            totStrength -= amount * strengths[randIndex];
        }
        MobWave wave = new MobWave(spawnables);
        
        return wave;
    }
    
    private int getStrengthFromRound(int round) {
        return (int)(4.0d + round + 0.2d * Math.pow((double)round, 2));
    }
    
    private int getStrength(GameObjectType type) {
        switch(type) {
            case M_BLATTLAUS:
                return 1;
            case M_WANDERLAUFER:
                return 10;
            case M_HIRSCHKAEFER:
                return 40;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
