/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author David
 */
public class Server {

    public final static int PORT = 27757;

    private ObservableList<Player> playerList;
    private SocketAcceptor acceptor;

    private GameHandler gameHandler;
    private static Server instance;

    public Server() throws IOException {
        instance = this;
        playerList = FXCollections.observableArrayList();

        acceptor = new SocketAcceptor();
        
        gameHandler = new GameHandler();

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

    public void addGame(ServerGame game) {
        gameHandler.addGame(game);
    }
    
    public static Server getInstance() {
        return instance;
    }
}
