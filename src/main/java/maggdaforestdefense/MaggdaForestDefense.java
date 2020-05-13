/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import maggdaforestdefense.auth.Afterwards;
import maggdaforestdefense.auth.Credentials;
import maggdaforestdefense.auth.AuthWindow;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.server.Server;
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
        if(ConfigurationManager.getConfig().auth.signedIn) {
            mainApp(primaryStage);
        } else {
            new AuthWindow(new Afterwards() {
                @Override
                public void run() {
                    mainApp(primaryStage);
                }
            }).show();
        }
    }

    public void mainApp(Stage primaryStage) {
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
        primaryStage.setMaximized(false);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
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
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        launch(args);
    }

}
