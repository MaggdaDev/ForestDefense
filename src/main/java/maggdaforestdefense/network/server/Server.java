/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author David
 */
public class Server {

    public final static int PORT = 26656;

    private ObservableList<Player> playerList;
    private SocketAcceptor acceptor;

    private static Server instance;

    public Server() throws IOException {
        instance = this;
        playerList = FXCollections.observableArrayList();

        acceptor = new SocketAcceptor();

        Thread thread = new Thread(acceptor);
        thread.start();

    }

    public void addNewSocket(Socket socket) {
        ServerSocketHandler addSocketHandler;
        try {
            addSocketHandler = new ServerSocketHandler(socket);
            Player player = new Player(addSocketHandler);
            playerList.add(player);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static Server getInstance() {
        return instance;
    }
}
