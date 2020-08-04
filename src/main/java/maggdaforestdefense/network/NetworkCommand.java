/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network;

import maggdaforestdefense.util.Exceptions;

/**
 *
 * @author David
 */
public class NetworkCommand {

    public static final CommandArgument[] EMPTY_ARGS = new CommandArgument[]{};
    // PREDEF COMMANDS
    public static final NetworkCommand REQUIRE_CONNECTION = new NetworkCommand(CommandType.REQUIRE_CONNECTION, EMPTY_ARGS),
            PERMIT_CONNECTION = new NetworkCommand(CommandType.PERMIT_CONNECTION, EMPTY_ARGS),
            START_GAME = new NetworkCommand(CommandType.START_GAME, EMPTY_ARGS),
            END_GAME = new NetworkCommand(CommandType.END_GAME, EMPTY_ARGS);

    //CONSTANTS
    public final static String KEYWORD = "__", SEPARATOR_1 = ";";

    private CommandType commandType;
    private CommandArgument[] commandArguments;

    public NetworkCommand(CommandType type, CommandArgument[] args) {
        commandType = type;
        commandArguments = args;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        String retString = "";
        retString += KEYWORD + SEPARATOR_1 + commandType.ordinal();

        for (CommandArgument arg : commandArguments) {
            retString += SEPARATOR_1 + arg.toString();
        }

        return retString;
    }

    public String getArgument(String name) {
        for (CommandArgument arg : commandArguments) {
            if (arg.getName().equals(name)) {
                return arg.getValue();
            }
        }
        try {
            throw new Exceptions.ArgumentNotFoundException();
        } catch (Exceptions.ArgumentNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public double getNumArgument(String name) {
        return Double.parseDouble(getArgument(name));
    }
    
    public boolean containsArgument(String name) {
        for(CommandArgument arg: commandArguments) {
            if(arg.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static NetworkCommand fromString(String string) {
        String[] splitted = string.split(SEPARATOR_1);

        int commandTypeIndex = Integer.parseInt(splitted[1]);
        CommandType type = CommandType.values()[commandTypeIndex];

        CommandArgument[] argsArr = new CommandArgument[splitted.length - 2];
        for (int i = 0; i < argsArr.length; i++) {
            argsArr[i] = CommandArgument.fromString(splitted[i + 2]);
        }
        return new NetworkCommand(type, argsArr);

    }
    
    

    public static boolean testForKeyWord(String s) {
        if (s.startsWith(KEYWORD)) {
            return true;
        } else {
            return false;
        }
    }

    public static enum CommandType {

        //  CLIENT TO SERVER
        REQUIRE_CONNECTION, // NO ARGS
        START_GAME, // NO ARGS
        ADD_TOWER, // x, y, type
        UPGRADE_BUTTON_CLICKED, //id, tier, type
        
        //  SERVER TO CLIENT
        PERMIT_CONNECTION, // NO ARGS
        SHOW_MAP, // map
        NEW_GAME_OBJECT, //type, id, x, y (maybe more; type specific)
        UPDATE_GAME_OBJECT, //id, args (x,y maybe more, type specific)
        UPDATE_GAME_RESSOURCES, // coins, essence
        PLANT_TREE,             //id, type, xIndex, yIndex
        REMOVE_GAME_OBJECT,     //id
        UPGRADE_BUY_CONFIRMED,  //id, tier, type
        NEXT_WAVE,              //wave
        END_GAME;               // NO ARGS
        
    }
}
