/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maggdaforestdefense.auth.Afterwards;
import maggdaforestdefense.auth.AuthWindow;
import maggdaforestdefense.auth.SwingAuthWindow;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.Server;

import java.util.Objects;
import javafx.scene.image.Image;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import org.panda_lang.pandomium.util.os.PandomiumOS;

/**
 *
 * @author David
 */
public class MaggdaForestDefense extends Application {    
    //Main
    private static MaggdaForestDefense instance;
    public static MaggdaForestDefense getInstance() {
        return instance;
    }

    //Graphics
    private MenuManager menueManager;
    private StackPane root;

    //Networks
    private NetworkManager networkManager;
    
    // Game
    private Game game;

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        if(ConfigurationManager.getConfig().auth.signedIn) {
            mainApp(primaryStage);
        } else {
            if(PandomiumOS.isWindows()&&(System.getProperty("java.version").startsWith("1."))) {
                Logger.logClient("Using pandemonium");
                new SwingAuthWindow(new Afterwards() {
                    @Override
                    public void run() {
                        mainApp(primaryStage);
                    }
                }).show();
            } else {
                Logger.logClient("Using javafx.webview");
                new AuthWindow(new Afterwards() {
                    @Override
                    public void run() {
                        mainApp(primaryStage);
                    }
                }).show();
            }
        }
    }

    public void mainApp(Stage primaryStage) {
        try {
            Server server = new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                exit();
            }
        });

        root = new StackPane();

        Scene scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("maggdaforestdefense/styles/styles.css")).toExternalForm());

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

    public void exit() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //System.setProperty("java.library.path", "natives");
        Logger.logClient("Java version: " + System.getProperty("java.version"));
        //SvgImageLoaderFactory.install();
        launch(args);
    }

}
