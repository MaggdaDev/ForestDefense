/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network;

/**
 *
 * @author David
 */
public class NetworkCommand {

    public static final CommandArgument[] EMPTY_ARGS = new CommandArgument[]{};
    // PREDEF COMMANDS
    public static final NetworkCommand REQUIRE_CONNECTION = new NetworkCommand(CommandType.REQUIRE_CONNECTION, EMPTY_ARGS),
            PERMIT_CONNECTION = new NetworkCommand(CommandType.PERMIT_CONNECTION, EMPTY_ARGS),
            START_GAME = new NetworkCommand(CommandType.START_GAME, EMPTY_ARGS);

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
        return "";
    }
    
    public double getNumArgument(String name) {
        return Double.parseDouble(getArgument(name));
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

        //CLIENT TO SERVER
        REQUIRE_CONNECTION,     // NO ARGS
        START_GAME,             // NO ARGS
        ADD_TOWER,              // x, y, type
        // SERVER TO CLIENT
        UPDATE_TEST,            // x and y
        PERMIT_CONNECTION,      // NO ARGS
        SHOW_MAP,               // map
        NEW_GAME_OBJECT,        //type, id, x, y (maybe more; type specific)
        UPDATE_GAME_OBJECT;     //id, args (x,y maybe more, type specific)
    }
}
