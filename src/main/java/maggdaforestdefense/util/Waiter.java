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
public class Waiter {

    public static void waitUntil(WaitCondition condition) {
        while (!condition.isCondition()) {
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static interface WaitCondition {

        public boolean isCondition();
    }
}
