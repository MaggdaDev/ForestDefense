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
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.Logger;

import java.util.Objects;
import javafx.beans.value.ChangeListener;

/**
 * Main class.
 *
 * @author David, MinorTom
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
    private Scene scene;
    private Stage primStage;

    //Networks
    private NetworkManager networkManager;

    private static boolean isServer, isDev;
    
    private static Server server;

    /**
     * Starts the program/GUI
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primStage = primaryStage;
        if (ConfigurationManager.getConfig().getAuth().isSignedIn() && ConfigurationManager.getConfig().getAuth().refresh()) {
            mainApp(primaryStage);
        } else {
            AuthWindow.authenticate(new Afterwards() {
                @Override
                public void run() {
                    mainApp(primaryStage);
                }
            });
        }
    }

    /**
     * Opens the main window
     *
     * @param primaryStage
     */
    public void mainApp(Stage primaryStage) {
        if (isDev) {
            try {
                server = new Server();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                exit();
            }
        });

        root = new StackPane();

        scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("maggdaforestdefense/styles/styles.css")).toExternalForm());

        primaryStage.setTitle(
                "MaggdaForestDefense");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
        // Main

        // Networks
        networkManager = NetworkManager.getInstance();

        // Graphics
        menueManager = new MenuManager(root);
        menueManager.start();

        // Game
    }

    public void exit() {
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.logClient("AJAJJAJJAJJAJAJAJJAJAJAJJA");
        Logger.logClient(args[0]);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //System.setProperty("java.library.path", "natives");
        Logger.logClient("Java version: " + System.getProperty("java.version"));
        //SvgImageLoaderFactory.install();

        isServer = false;
        isDev = false;
        for (String arg : args) {
            if (arg.equals("--server")) {
                isServer = true;
                Logger.logClient("Server mode!");
            } else if (arg.equals("--dev")) {
                isDev = true;
                Logger.logClient("Development mode!");
            }
        }

        if (isServer) {
            try {
            server = new Server();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            launch(args);
        }
    }

    public void addOnSceneResize(ChangeListener<? super Number> l) {
        scene.widthProperty().addListener(l);
        scene.heightProperty().addListener(l);
    }

    public Scene getScene() {
        return scene;
    }

    public static double getWindowWidth() {
        return instance.primStage.getWidth();
    }

    public static double getWindowHeight() {
        return instance.primStage.getHeight();
    }
    
    public static boolean isServer() {
        return isServer;
    }

}
