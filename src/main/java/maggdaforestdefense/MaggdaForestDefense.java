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
import maggdaforestdefense.config.Version;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.storage.Logger;

import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.NetworkCommand.CommandType;
import maggdaforestdefense.sound.SoundEngine;

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
    public final static double DEBUG_WIDTH = 2560, DEBUG_HEIGHT = 1380;

    //Networks
    private NetworkManager networkManager;

    private static boolean isServer, isDev;

    private static Server server;

    private static Game game;
    
    private static SoundEngine soundEngine;

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
        
        soundEngine = new SoundEngine();
        soundEngine.playSound(SoundEngine.Sound.MENUMUSIK);
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

        DoubleProperty fontSize = new SimpleDoubleProperty();
        fontSize.bind(Bindings.createDoubleBinding(()->(Math.sqrt(scene.getWidth() * scene.getHeight()) / 100.0d), scene.heightProperty(), scene.widthProperty()));
        
        root.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
        
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("maggdaforestdefense/styles/styles.css")).toExternalForm());

        primaryStage.setTitle(
                "Forest Defense");
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

    public static void main(String[] args) {
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        //System.setProperty("java.library.path", "natives");
        Logger.logClient("Java version: " + System.getProperty("java.version"));
        Logger.logClient("Game version: " + Version.getVersion());
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            launch(args);
        }
    }

    public void joinGame(String id) {
        networkManager.sendCommand(new NetworkCommand(CommandType.REQUEST_JOIN_GAME, new CommandArgument[]{new CommandArgument("id", id)}));
    }

    public double getHeightFact() {
        return 1;

    }

    public double getWidthFact() {
        return 1;
    }

    public double getSizeFact() {
        if (getHeightFact() < getWidthFact()) {
            return getHeightFact();
        } else {
            return getWidthFact();
        }
    }
    
    public static void bindToWidth(DoubleProperty prop, double normalWidth) {
        
        prop.bind(Bindings.createDoubleBinding(()->(normalWidth * instance.scene.getWidth()/DEBUG_WIDTH), instance.scene.widthProperty()));
    }
    
    public static void bindToHeight(DoubleProperty prop, double normalHeight) {
        prop.bind(Bindings.createDoubleBinding(()->(normalHeight * instance.scene.getHeight()/DEBUG_HEIGHT), instance.scene.heightProperty()));
    }
    
    public static SoundEngine getSoundEngine() {
        return soundEngine;
    }

    public void addOnSceneResize(ChangeListener<? super Number> l) {
        scene.widthProperty().addListener(l);
        scene.heightProperty().addListener(l);
    }

    public Scene getScene() {
        return scene;
    }

    public static double getWindowWidth() {
        return instance.scene.getWidth();
    }

    public static double getWindowHeight() {
        return instance.scene.getHeight();
    }

    public static boolean isServer() {
        return isServer;
    }

    public static boolean isDev() {
        return isDev;
    }

}
