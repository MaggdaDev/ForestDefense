/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import java.util.Vector;

/**
 *
 * @author DavidPrivat
 */
public class Randomizer {

    private final Vector<RandomEvent> events;

    public Randomizer() {
        events = new Vector();
    }

    public void addEvent(RandomEvent e) {
        events.add(e);
    }

    public int throwDice() {
        double probsum = 0;
        for (RandomEvent event : events) {
            probsum += event.getProbability();
        }
        double currProb = 0;
        double random = Math.random() * probsum;
        for (RandomEvent event : events) {
            if (currProb < random && random < currProb + event.getProbability()) {
                return event.getNumber();
            } else {
                currProb += event.getProbability();
            }
        }
        return 0;
    }
}
