/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import java.util.HashMap;
import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

import maggdaforestdefense.network.NetworkCommand;

/**
 *
 * @author David
 */
public class GameHandler {
    private static int currentGameId = 0;
    
    private HashMap<String, ServerGame> games;
    private Vector<CommandArgument> gamesAsArgs;
    
    public GameHandler() {
        games = new HashMap<>();
    }
    
    public void addGame(ServerGame game) {
        games.put(getNextGameId(), game);
    }
    
    public NetworkCommand getGamesAsCommand() {
        gamesAsArgs = new Vector<>();
        games.forEach((String id, ServerGame game)->{
            gamesAsArgs.add(new CommandArgument(id, game.getGameName()));
        });
        return new NetworkCommand(NetworkCommand.CommandType.SHOW_GAMES, (CommandArgument[])gamesAsArgs.toArray());
    }
    
    public final static synchronized String getNextGameId() {
        currentGameId++;
        return String.valueOf(currentGameId);
    }
}
