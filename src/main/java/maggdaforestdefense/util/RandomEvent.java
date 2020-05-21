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
public class RandomEvent {
    private final double prob;
    private final int num;
 
    
    public RandomEvent(int number, double prob) {
        this.prob = prob;
        num = number;
    }
    
    public int getNumber() {
        return num;
    }
    
    
    public double getProbability() {
        return prob;
    }
}
