/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.storage.Logger;
import sun.rmi.runtime.Log;

/**
 *
 * @author David
 */
public class ClientCommandHandler extends Thread {

    private BufferedReader input;
    private LinkedBlockingQueue<NetworkCommand> queue;
    private Queue<NetworkCommand> workingQueue;

    private boolean isInGame = false;

    public ClientCommandHandler(BufferedReader in) {
        input = in;
        queue = new LinkedBlockingQueue<>();
        workingQueue = new LinkedList();

    }

    @Override
    public void run() {
        String line = "";
        try {
            while ((line = input.readLine()) != null) {
                if (NetworkCommand.testForKeyWord(line)) {
                    queue.add(NetworkCommand.fromString(line));
                }
                if (!isInGame) {        // IF GAME IS RUNNING: HANDLES COMMANDS WITH 60FPS IN GAMETHREAD; IF NOT IN GAME: HANDLES COMMANDS AS SOON AS THEY ARRIVE IN COMMAND HANDLER THREAD (THIS)
                    handleInput();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleInput() {
        queue.drainTo(workingQueue);
        while (workingQueue.size() != 0) {
            handleCommand(workingQueue.poll());
        }

    }

    private void handleCommand(NetworkCommand command) {
        //Logger.logClient("Command handled: " + command.toString());

        switch (command.getCommandType()) {
            case PERMIT_CONNECTION:
                NetworkManager.getInstance().notifyForAnswer();
                break;
            case SHOW_MAP:
                ClientMapCell[][] cells = Map.stringToClientMapCells(command.getArgument("map"));
                Game.getInstance().generateMap(cells);
                break;
            case NEW_GAME_OBJECT:
                ClientGameObject gameObject = GameObject.generateClientGameObject(command);
                Game.getInstance().addGameObject(gameObject);
                break;
            case UPDATE_GAME_OBJECT:
                Game.getInstance().updateGameObject(command);
                break;
            case PLANT_TREE:
                Game.getInstance().plantTree(command);
                break;
            case REMOVE_GAME_OBJECT:
                Game.getInstance().removeGameObject(command.getArgument("id"));
                break;
        }
    }

    void setInGame(boolean b) {
        isInGame = b;
    }
}
