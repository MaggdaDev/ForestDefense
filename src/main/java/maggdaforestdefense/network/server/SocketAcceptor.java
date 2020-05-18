/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.storage.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author David
 */
public class SocketAcceptor implements Runnable, Stoppable {

    private ServerSocket serverSocket;
    private boolean isOpen;
    public SocketAcceptor() throws IOException {
        serverSocket = new ServerSocket(Server.PORT);
        Logger.logServer("ServerSocket generated: "+ serverSocket.toString());
        Logger.logServer("ServerSocket localport: " + serverSocket.getLocalPort());
        isOpen = false;
    }

    @Override
    public void run() {
        isOpen = true;
        while (isOpen) {
            try {
                Socket newSocket = serverSocket.accept();
                Logger.logServer("New Socket accepted!");
                Logger.logServer("Socket: " + newSocket.toString());
                Logger.logServer("Socket port: " + newSocket.getPort());
                Server.getInstance().addNewSocket(newSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        isOpen = false;
        try {  
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
