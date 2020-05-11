/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class ServerSocketHandler implements Runnable, Stoppable {

    private Socket socket;
    //private BufferedReader input;
    private BufferedReader input;
    private PrintWriter output;

    private boolean running, isInGame=false;
    
    private LinkedBlockingQueue<NetworkCommand> queue;
    private Queue<NetworkCommand> workingList;

    public ServerSocketHandler(Socket socket) throws IOException {
        
        this.socket = socket;
        // input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        
        // Queues
        queue = new LinkedBlockingQueue();
        workingList = new LinkedList();

        System.out.println("[SERVER] New ServerSocketHandler generated");
        
        
    }

    @Override
    public void run() {
        running = true;
        String inputLine = "";
        try {
            System.out.println("[SERVER] Now reading");
            while (running && (inputLine = input.readLine()) != null) {
                    Logger.logServer("Line read: " + inputLine);

                try {
                    if(NetworkCommand.testForKeyWord(inputLine)) {
                        queue.add(NetworkCommand.fromString(inputLine));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(!isInGame) {
                    update();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void update() {
        queue.drainTo(workingList);
        while(workingList.size() != 0) {
            handleCommand(workingList.poll());
        }
    }
    
    private void handleCommand(NetworkCommand command) {
        Logger.logServer("Command handled: " + command);
        switch(command.getCommandType()) {
            case REQUIRE_CONNECTION:
                sendCommand(NetworkCommand.PERMIT_CONNECTION);
                break;
        }
        
    }
    
    public void sendCommand(NetworkCommand command) {
        Logger.logServer("Command sent: " + command.toString());
        output.println(command.toString());
    }

    @Override
    public void stop() {
        running = false;
        try {
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
