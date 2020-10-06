/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network;

import com.google.gson.Gson;
import maggdaforestdefense.util.Exceptions;

/**
 *
 * @author David
 */
public class NetworkCommand {

    public static final CommandArgument[] EMPTY_ARGS = new CommandArgument[]{};
    // PREDEF COMMANDS
    public static final NetworkCommand 

            GAME_CREATED = new NetworkCommand(CommandType.GAME_CREATED, EMPTY_ARGS),
            START_GAME = new NetworkCommand(CommandType.START_GAME, EMPTY_ARGS),
            END_GAME = new NetworkCommand(CommandType.END_GAME, EMPTY_ARGS),
            READY_FOR_NEXT_ROUND = new NetworkCommand(CommandType.READY_FOR_NEXT_ROUND, EMPTY_ARGS),
            LIST_AVAILABLE_GAMES = new NetworkCommand(CommandType.LIST_AVAILABLE_GAMES, EMPTY_ARGS),
            WAIR_FOR_READY_NEXT_WAVE = new NetworkCommand(CommandType.WAIT_FOR_READY_NEXT_WAVE, EMPTY_ARGS),
            WAVE_FINISHED = new NetworkCommand(CommandType.WAVE_FINISHED, EMPTY_ARGS);

    //CONSTANTS
    public final static String KEYWORD = "FD;";

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
        return KEYWORD + new Gson().toJson(this);
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
    
    public CommandArgument[] getAllArguments() {
        return commandArguments;
    }

    public double getNumArgument(String name) {
        return Double.parseDouble(getArgument(name));
    }

    public boolean containsArgument(String name) {
        for (CommandArgument arg : commandArguments) {
            if (arg.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static NetworkCommand fromString(String string) {
        return new Gson().fromJson(string.replace(KEYWORD, ""), NetworkCommand.class);
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
        REQUIRE_CONNECTION, // auth
        CREATE_GAME, // NO ARGS
        LIST_AVAILABLE_GAMES,   // NO ARGS
        START_GAME, // NO ARGS
        ADD_TOWER, // x, y, type
        UPGRADE_BUTTON_CLICKED, //id, tier, type
        READY_FOR_NEXT_ROUND, // NO ARGS
        REQUEST_ESSENCE_TOWER, //id

        //  SERVER TO CLIENT
        PERMIT_CONNECTION, // auth_ok
        SHOW_GAMES,         // list of games
        GAME_CREATED,       // NO ARGS
        INVALID_MESSAGE, // NO ARGS      The message sent by the client is invalid, will disconnect
        HANDLE_EXCEPTION, // name, stack
        SHOW_MAP, // map
        NEW_GAME_OBJECT, //type, id, x, y (maybe more; type specific)
        UPDATE_GAME_OBJECT, //id, args (x,y maybe more, type specific)
        UPDATE_GAME_RESSOURCES, // coins, essence
        PLANT_TREE, //id, type, xIndex, yIndex
        REMOVE_GAME_OBJECT, //id
        UPGRADE_BUY_CONFIRMED, //id, tier, type
        WAVE_FINISHED, // NO ARGS
        TOWER_NEED_ESSENCE, // id
        WAIT_FOR_READY_NEXT_WAVE, // NO ARGS
        NEXT_WAVE, //wave
        DO_ESSENCE_ANIMATION, // id (of tower)
        END_GAME;               // NO ARGS

    }
}
