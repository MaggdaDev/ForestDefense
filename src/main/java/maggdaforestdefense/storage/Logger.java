/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

import java.io.PrintWriter;
import java.io.StringWriter;

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
    public static void errClient(String string) {
        System.err.println("[CLIENT/e] " + string);
    }
    public static void errClient(String string, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errClient(string + "\n" + sw.toString());
    }
    public static void logServer(String string) {
        System.out.println("[SERVER] " + string);
    }
    public static void debugServer(String string) {
        System.out.println("[SERVER/d] " + string);
    }
    public static void errServer(String string) {
        System.err.println("[SERVER/e] " + string);
    }
    public static void errServer(String string, Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errServer(string + "\n" + sw.toString());
    }
}
