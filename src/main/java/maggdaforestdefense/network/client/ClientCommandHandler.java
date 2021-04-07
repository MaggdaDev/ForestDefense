/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import java.io.BufferedReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Platform;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.storage.Logger;
import sun.rmi.runtime.Log;

/**
 *
 * @author David
 */
public class ClientCommandHandler {

    private LinkedBlockingQueue<NetworkCommand> queue;
    private LinkedList<NetworkCommand> workingQueue;

    private boolean isInGame = false;
    private int tempCount = 0;

    public ClientCommandHandler() {
        queue = new LinkedBlockingQueue<>();
        workingQueue = new LinkedList();

    }


    public void onMessage(String message) {
        if (NetworkCommand.testForKeyWord(message)) {
            if(isInGame) {
            queue.add(NetworkCommand.fromString(message));
            } else {
                handleCommand(NetworkCommand.fromString(message));
            }
        } else {
            Logger.errClient("Received an invalid message from the server (KeywordNotFound): " + message);
        }
    }

    public void reset() {
        queue.clear();
        workingQueue.clear();
        
    }


    public void handleInput() {
        queue.drainTo(workingQueue);
        while (workingQueue.size() != 0) {
            try {
                handleCommand(workingQueue.removeFirst());
            } catch (Exception e) {
                Logger.errClient("Exception while handling a command", e);
            }
        }
    }

    private void handleCommand(NetworkCommand command) {

        switch (command.getCommandType()) {
            case UPDATE:
                for (CommandArgument arg : command.getAllArguments()) {
                    handleCommand(arg.getInnerCommand());
                }
                break;

            case PERMIT_CONNECTION:
                NetworkManager.getInstance().onReady(Boolean.parseBoolean(command.getArgument("auth_ok")));
                break;
            case LAUNCH_GAME:
                Game.createGame(command.getArgument("name"));
                break;
            case SHOW_GAMES:
                Platform.runLater(() -> {
                    MenuManager.getInstance().showJoinableGames(command);
                });
                break;
            case SHOW_WAITING_PLAYERS:
                Platform.runLater(() -> {
                    MenuManager.getInstance().showWaitingPlayers(command);
                });
                break;
            case START_GAME:
                Platform.runLater(() -> {
                    Game.getInstance().startGame();
                });
                break;
            case SHOW_MAP:
                ClientMapCell[][] cells = Map.stringToClientMapCells(command.getArgument("map"));
                Platform.runLater(() -> {
                    Game.getInstance().generateMap(cells);
                });

                break;
            case NEW_GAME_OBJECT:
                ClientGameObject gameObject = GameObject.generateClientGameObject(command);
                Game.getInstance().addGameObject(gameObject);
                break;
            case UPDATE_GAME_OBJECT:
                Game.getInstance().updateGameObject(command);
                break;
            case UPDATE_GAME_RESSOURCES:
                Game.getInstance().updateRessources(command);
                break;
            case PLANT_TREE:
                Game.getInstance().plantTree(command);
                break;
            case REMOVE_GAME_OBJECT:
                Game.getInstance().removeGameObject(command.getArgument("id"));
                break;
            case UPGRADE_BUY_CONFIRMED:
                Game.getInstance().buyUpgrade(command);
                break;
            case END_GAME:
                Game.getInstance().endGame(command);
                break;
            case NEXT_WAVE:
                Game.getInstance().announceWave(command);
                break;
            case WAIT_FOR_READY_NEXT_WAVE:
                Game.getInstance().readyCheck();
                break;
            case DO_ESSENCE_ANIMATION:
                Game.getInstance().doEssenceAnimtion(command);
                break;
            case WAVE_FINISHED:
                Game.getInstance().waveFinished();
                break;
            case TOWER_NEED_ESSENCE:
                Game.getInstance().towerNeedEssence(command);
                break;
            case UPDATE_READY_CHECK:
                Game.getInstance().updateReadyCheck(command);
                break;
            case PERFORM_ACTIVESKILL_TC:
                Game.getInstance().performActiveSkill(command);
                break;
            case SUGGEST_MUSIC:
                Game.getInstance().suggestMusic(command);
                break;
            case EDIT_TAUSCHHANDEL:
                Game.getInstance().editTauschhandel(command);
                break;
            case NOTIFY_PLAYSPEED_CHANGE:
                Game.getInstance().notifyPlayspeedChange(command);
                break;
        }
    }

    public void setInGame(boolean b) {
        isInGame = b;   
    }

}
