/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class NetworkManager {

    private ServerConnection serverConnection;

    private ClientCommandHandler commandHandler;
    private static NetworkManager instance;

    private NetworkManager() {

    }

    public void update() {

    }

    public void connect() {
        synchronized (this) {
            serverConnection = new ServerConnection();
            sendCommand(NetworkCommand.REQUIRE_CONNECTION);
            commandHandler = new ClientCommandHandler(serverConnection.getInput());
            commandHandler.start();
            try {
                wait();     // FOR ANSWER
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(NetworkCommand command) {
        Logger.logClient("Command sent: " + command.toString());
        serverConnection.getOutput().println(command.toString());
    }

    public void notifyForAnswer() {
        synchronized (this) {
            notify();
        }
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
}
