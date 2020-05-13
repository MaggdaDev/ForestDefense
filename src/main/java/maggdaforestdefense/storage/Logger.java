/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

/**
 *
 * @author David
 */
public class Logger {
    public static void logClient(String string) {
        System.out.println("[CLIENT] " + string);
    }
    public static void debugClient(String string) {
        System.out.println("[CLIENT/d] " + string);
    }
    public static void logServer(String string) {
        System.out.println("[SERVER] " + string);
    }
}
