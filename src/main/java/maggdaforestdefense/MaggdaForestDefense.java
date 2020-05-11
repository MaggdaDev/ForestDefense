/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.ImageLoader;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.network.client.NetworkManager;

/**
 *
 * @author David
 */
public class MaggdaForestDefense extends Application {    
    //Main


    //Graphics
    private MenuManager menueManager;
    private StackPane root;

    //Networks
    private NetworkManager networkManager;
    
    // Game
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        try {
            Server server = new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }

        root = new StackPane();

        Scene scene = new Scene(root);

        primaryStage.setTitle(
                "MaggdaForestDefense");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        // Main
 

        // Networks
        networkManager = NetworkManager.getInstance();

        // Graphics
        menueManager = new MenuManager(root);
        menueManager.start();
        
        // Game
        game = new Game();
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
