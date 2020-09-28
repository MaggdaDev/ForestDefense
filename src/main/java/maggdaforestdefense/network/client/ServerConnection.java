/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author David
 */
public class ServerConnection {
    public static String SERVER_IP = "vs5.minortom.net";
    
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ServerConnection() {
        if(maggdaforestdefense.MaggdaForestDefense.isDev()) {
            SERVER_IP = "localhost";
        }
        try {
            socket = new Socket(SERVER_IP, Server.PORT);

            Logger.logClient("Socket: " + socket.toString());
            Logger.logClient("Used Port to create Socket: " + socket.getLocalPort());

            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public BufferedReader getInput() {
        return input;
    }
    
    public PrintWriter getOutput() {
        return output;
    }
}
