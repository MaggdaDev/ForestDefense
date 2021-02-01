/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import com.google.gson.Gson;
import maggdaforestdefense.auth.AuthCredentials;
import maggdaforestdefense.auth.AuthUser;
import maggdaforestdefense.auth.AuthenticationException;
import maggdaforestdefense.auth.WireCredentials;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Json;
import org.java_websocket.WebSocket;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author David
 */
public class ServerSocketHandler implements Runnable, Stoppable {

    private WebSocket conn;
    //private BufferedReader input;
    private BufferedReader input;
    private PrintWriter output;

    private LinkedBlockingQueue<NetworkCommand> queue;
    private Queue<NetworkCommand> workingList;
    private Player owner;

    private ServerGame game;

    private boolean gameStarted = false;

    public ServerSocketHandler(WebSocket conn) {

        this.conn = conn;

        // Queues
        queue = new LinkedBlockingQueue();
        workingList = new LinkedList();

        Logger.debugServer("New ServerSocketHandler created");
    }

    @Override
    public void run() {
        while (true) {
            update();
        }
    }

    public void handleMessage(WebSocket conn, String message) {
        if (NetworkCommand.testForKeyWord(message)) {
            queue.add(NetworkCommand.fromString(message));
        } else {
            Logger.errServer("Received an invalid command.");
            sendCommand(new NetworkCommand(NetworkCommand.CommandType.INVALID_MESSAGE, NetworkCommand.EMPTY_ARGS));
        }
    }

    public void handleException(WebSocket conn, Exception e, boolean isWs) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sendCommand(new NetworkCommand(NetworkCommand.CommandType.REQUIRE_CONNECTION, new CommandArgument[]{new CommandArgument("name", e.getMessage()), new CommandArgument("stack", sw.toString())}));
    }

    public void update() {
        queue.drainTo(workingList);
        while (workingList.size() != 0) {
            try {
                handleCommand(workingList.poll());
            } catch (Exception e) {
                Logger.errServer("Exception while handling a command", e);
                handleException(conn, e, false);
            }
        }
    }

    private void handleCommand(NetworkCommand command) {
        Logger.logServer("Command handled: " + command);
        switch (command.getCommandType()) {
            case REQUIRE_CONNECTION:
                try {
                    WireCredentials credentials = Json.getGson().fromJson(command.getArgument("auth"), WireCredentials.class);
                    AuthCredentials authCredentials;
                    AuthUser user;

                    if (credentials.getType() == WireCredentials.CredentialType.AnonAuthCredentials) {
                        user = credentials.getAnonAuthCredentials().getAuthUser();
                    } else if (credentials.getType() == WireCredentials.CredentialType.FAuthCredentials) {
                        user = credentials.getfAuthCredentials().getAuthUser();
                    } else {
                        user = null;
                    }
                    if (user == null) {
                        sendCommand(new NetworkCommand(NetworkCommand.CommandType.PERMIT_CONNECTION, new CommandArgument[]{new CommandArgument("auth_ok", Boolean.toString(false))}));
                        conn.close(); // TODO: Find a better way to close a connection
                    } else {
                        sendCommand(new NetworkCommand(NetworkCommand.CommandType.PERMIT_CONNECTION, new CommandArgument[]{new CommandArgument("auth_ok", Boolean.toString(true))}));
                        owner.setAuthUser(user);
                    }
                } catch (AuthenticationException e) {
                    Logger.logServer("Couldn't authenticate a user: " + e.getReason() + " " + e.getMessage());
                    sendCommand(new NetworkCommand(NetworkCommand.CommandType.PERMIT_CONNECTION, new CommandArgument[]{new CommandArgument("auth_ok", Boolean.toString(false))}));
                    conn.close(); // TODO: Find a better way to close a connection
                }
                break;
            case CREATE_GAME:
                createGame(command.getArgument("name"));
                break;
            case LIST_AVAILABLE_GAMES:
                listGames();
                break;
            case REQUEST_JOIN_GAME:
                requestJoinGame(command.getArgument("id"));
                break;

            case REQUEST_START_GAME:
                synchronized (this) {
                    if (!gameStarted) {
                        gameStarted = true;

                        game.sendCommandToAllPlayers(NetworkCommand.START_GAME);
                        game.start();

                    }

                }

                break;

            case ADD_TOWER:
                double xPos = command.getNumArgument("x");
                double yPos = command.getNumArgument("y");
                GameObjectType type = GameObjectType.values()[(int) command.getNumArgument("type")];

                game.addNewTower(xPos, yPos, type);

                break;

            case UPGRADE_BUTTON_CLICKED:
                String id = command.getArgument("id");
                int tier = (int) command.getNumArgument("tier");
                int upgradeType = (int) command.getNumArgument("type");
                game.buyUpgrade(id, tier, upgradeType);
                break;

            case READY_FOR_NEXT_ROUND:
                owner.setReadyForNextRound(true);
                game.updateReadyProgress();
                break;

            case REQUEST_ESSENCE_TOWER:

                game.requestEssence(command.getArgument("id"));
                break;
                
            case PERFORM_ACTIVESKILL_TS:
                game.performActiveSkill(command.getArgument("id"), ActiveSkill.values()[(int)command.getNumArgument("skill")]);
                
                break;
                
            case USE_LORBEER_TRADE:
                game.useLorbeerTrade(command.getArgument("id"), Upgrade.values()[(int)command.getNumArgument("upgrade")]);
                break;
        }

    }

    private void createGame(String name) {
        game = new ServerGame(owner, name);
        Server.getInstance().addGame(game);
    }

    private void listGames() {
        sendCommand(Server.getInstance().getGamesAsCommand());
    }

    public void sendCommand(NetworkCommand command) {
        //Logger.debugServer("Command sent: " + command.toString());
        if(conn.isOpen()) {
            conn.send(command.toString());
        } else {
            Logger.errServer("A command was sent but the client is not connected.");
        }
    }

    @Override
    public void stop() {
        if(game!=null) {
            game.removePlayer(owner);
        }
    }

    public void setOwner(Player o) {
        owner = o;
    }

    private void requestJoinGame(String gameId) {
        game = Server.getInstance().getGameHandler().getGame(gameId);
        game.addPlayer(owner);
    }

}
