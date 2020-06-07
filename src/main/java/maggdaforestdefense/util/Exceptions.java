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
public class Exceptions {
    public static class GameObjectNotCompatibleException extends Exception {};
    
    public static class MultipleTowersOnCellException extends Exception {};
    
    public static class ArgumentNotFoundException extends Exception {};
}
