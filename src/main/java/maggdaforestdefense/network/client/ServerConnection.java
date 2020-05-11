/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class ServerConnection {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ServerConnection() {
        try {
            socket = new Socket("localhost", Server.PORT);

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
