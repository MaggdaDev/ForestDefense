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
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import maggdaforestdefense.config.Configuration;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import maggdaforestdefense.network.client.UdpReceiver;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;

/**
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

    //UDP STUFF
    private DatagramSocket udpSocket;
    private DatagramPacket udpPacket;
    private byte[] udpByteBuffer;
    public final static int byteBufferLength = 1400;
    private boolean useUDP = true;

    public ServerSocketHandler(WebSocket conn, DatagramSocket udp) throws SocketException {
        this.useUDP = useUDP;
        this.conn = conn;

        // Queues
        queue = new LinkedBlockingQueue();
        workingList = new LinkedList();

        Logger.debugServer("New ServerSocketHandler created");
        useUDP = udp != null;
        if (useUDP) {

            udpSocket = udp;
            udpByteBuffer = new byte[byteBufferLength];
            udpPacket = new DatagramPacket(udpByteBuffer, byteBufferLength, new InetSocketAddress(conn.getRemoteSocketAddress().getHostName(), Server.CLIENT_UDP_PORT));
            Logger.logServer("UDP set up");
        }
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
        //Logger.logServer("Command handled: " + command);
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
                }
/*
=======
                Credentials credentials = new Gson().fromJson(command.getArgument("auth"), Credentials.class);
                boolean anonymous = true;
                String userId = "";
                String userName = "";
                if (credentials.isSignedIn()) {
                    MWUser user = AuthWindow.getUserFromToken(credentials.getAuthToken());
                    anonymous = false;
                    userId = user.getUsername() + "@wiki.minortom.net";
                    userName = user.getUsername();
                } else if (credentials.getUserName().startsWith("Anonymous") && credentials.getMwUser().getUsername().equals("Anonymous")) {
                    Logger.logServer("Anonymous login");
                    anonymous = true;
                    userId = "Anonymous@forestdefense.minortom.net";
                    userName = credentials.getUserName();
                } else {
                    Logger.logServer("User ID null! Neither anonymous nor other login working!  CredentialsUserName: " + credentials.getUserName() + "      mWUser.getUsername(): " + credentials.getMwUser().getUsername());
                    userId = null;
                }
                if (userId == null) {
>>>>>>> udptest
                    sendCommand(new NetworkCommand(NetworkCommand.CommandType.PERMIT_CONNECTION, new CommandArgument[]{new CommandArgument("auth_ok", Boolean.toString(false))}));
                    conn.close(); // TODO: Find a better way to close a connection
                }
*/
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

                    } else {
                        Logger.logServer("START_GAME requested but game already running!");
                    }

                }

                break;

            case REMOVE_WAITING_PLAYER:
                game.removePlayer(owner);
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

            case REQUEST_PLAYSPEED_CHANGE:
                int playspeedId = (int) command.getNumArgument("speedId");
                game.editPlayspeed(playspeedId);
                game.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.NOTIFY_PLAYSPEED_CHANGE, new CommandArgument[]{new CommandArgument("speedId", playspeedId)}));
                break;
            case READY_FOR_NEXT_ROUND:
                synchronized (this) {
                    owner.setReadyForNextRound(true);
                    game.updateReadyProgress();
                }
                break;

            case REQUEST_ESSENCE_TOWER:

                game.requestEssence(command.getArgument("id"));
                break;

            case PERFORM_ACTIVESKILL_TS:
                game.performActiveSkill(command.getArgument("id"), ActiveSkill.values()[(int) command.getNumArgument("skill")]);

                break;

            case USE_LORBEER_TRADE:
                game.useLorbeerTrade(command.getArgument("id"), Upgrade.values()[(int) command.getNumArgument("upgrade")]);
                break;
        }

    }

    private void createGame(String name) {
        gameStarted = false;
        game = new ServerGame(owner, name);
        Server.getInstance().addGame(game);
    }

    private void listGames() {
        sendCommand(Server.getInstance().getGamesAsCommand());
    }

    public void sendCommand(NetworkCommand command) {
        //Logger.debugServer("Command sent: " + command.toString());
        if (conn.isOpen()) {
            conn.send(command.toString());
        } else {
            Logger.errServer("A command was sent but the client is not connected.");
        }
    }

    public void sendCommandUPD(NetworkCommand command) {
        //Logger.debugServer("Command sent: " + command.toString());
        if (useUDP) {
            byte[] commandAsBytes = command.toString().getBytes();
            udpPacket.setData(commandAsBytes);
            udpPacket.setLength(commandAsBytes.length);
            try {
                udpSocket.send(udpPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendCommand(command);
        }

    }

    @Override
    public void stop() {
        if (game != null) {
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
