/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

/**
 *
 * @author David
 */
public class GameHandler {
    private Vector<ServerGame> games;
    
    public GameHandler() {
        games = new Vector<>();
    }
    
    public void addGame(ServerGame game) {
        games.add(game);
    }
}
