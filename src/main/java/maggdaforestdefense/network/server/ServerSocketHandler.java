/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server;

import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;

/**
 *
 * @author David
 */
public class ServerSocketHandler implements Runnable, Stoppable {

    private Socket socket;
    //private BufferedReader input;
    private BufferedReader input;
    private PrintWriter output;

    private boolean running;
    
    private LinkedBlockingQueue<NetworkCommand> queue;
    private Queue<NetworkCommand> workingList;
    private Player owner;
    
    private ServerGame game;

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
                    update();
                
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
        //Logger.logServer("Command handled: " + command);
        switch(command.getCommandType()) {
            case REQUIRE_CONNECTION:
                sendCommand(NetworkCommand.PERMIT_CONNECTION);
                break;
            case START_GAME:
                game = new ServerGame(owner);
                Server.getInstance().addGame(game);
                game.start();
                break;
            case ADD_TOWER:
                double xPos = command.getNumArgument("x");
                double yPos = command.getNumArgument("y");
                GameObjectType type = GameObjectType.values()[(int)command.getNumArgument("type")];
                
                game.addNewTower(xPos, yPos, type);
                
                break;
                
            case UPGRADE_BUTTON_CLICKED:
                String id = command.getArgument("id");
                int tier = (int)command.getNumArgument("tier");
                int upgradeType = (int)command.getNumArgument("type");
                game.buyUpgrade(id, tier, upgradeType);
                break;
                
            case READY_FOR_NEXT_ROUND:
                owner.setReadyForNextRound(true);
                break;
                
            case REQUEST_ESSENCE_TOWER:
                
                game.requestEssence(command.getArgument("id"));
                break;
        }
        
    }
    
    public void sendCommand(NetworkCommand command) {
        //Logger.logServer("Command sent: " + command.toString());
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
    
    public void setOwner(Player o) {
        owner = o;
    }
}
