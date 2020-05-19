/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

/**
 *
 * @author DavidPrivat
 */
public abstract class RandomEvent {
    private final double prob;
    
    public RandomEvent(double prob) {
        this.prob = prob;
    }
    
    public abstract void handle();
    
    public double getProbability() {
        return prob;
    }
}
