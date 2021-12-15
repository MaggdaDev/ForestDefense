/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maggdaforestdefense.auth.AuthenticationManager;
import maggdaforestdefense.config.Version;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.NetworkCommand.CommandType;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.Server;
import maggdaforestdefense.sound.SoundEngine;
import maggdaforestdefense.storage.Logger;

import java.util.Objects;
import maggdaforestdefense.network.server.serverGameplay.ServerSoundsPicker.Sound;


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
    private AuthenticationManager authenticationManager;

    private static boolean isServer, isDev;

    private static Server server;

    private static Game game;
    
    private static SoundEngine soundEngine;
    
    private static String customIp = null;

    /**
     * Starts the program/GUI
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        instance = this;
        primStage = primaryStage;

        mainApp(primaryStage);
        
        soundEngine = new SoundEngine();
        soundEngine.playSound(Sound.MENU_INTRO);
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
        NetworkManager.setCustomIP(customIp);
        networkManager = NetworkManager.getInstance();
        
        authenticationManager = AuthenticationManager.getInstance();

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
            } else if (arg.startsWith("--ip")) {
                customIp = arg.split("=")[1];
                Logger.logClient("Using custom IP: " + customIp);
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

    public static double getHeightFact() {
        return instance.scene.getHeight() / DEBUG_HEIGHT;

    }

    public static double getWidthFact() {
        return instance.scene.getWidth() / DEBUG_WIDTH;
    }

    public static double getSizeFact() {
        if (getHeightFact() < getWidthFact()) {
            return getHeightFact();
        } else {
            return getWidthFact();
        }
    }
    
    public static void bindToSizeFact(DoubleProperty prop, double normalWidth) {
        
        prop.bind(Bindings.createDoubleBinding(()->(normalWidth * getSizeFact()), instance.scene.heightProperty(), instance.scene.widthProperty()));
    }

    
    public static void bindToWidth(DoubleProperty prop, double normalWidth) {
        
        prop.bind(Bindings.createDoubleBinding(()->(normalWidth * getWidthFact()), instance.scene.widthProperty()));
    }
    
    public static void bindToHeight(DoubleProperty prop, double normalHeight) {
        prop.bind(Bindings.createDoubleBinding(()->(normalHeight * getHeightFact()), instance.scene.heightProperty()));
    }
    
    public static void bindPadding(ObjectProperty<Insets> padding, double insets) {
        padding.bind(Bindings.createObjectBinding(()->new Insets(insets * getSizeFact()), instance.scene.widthProperty(), instance.scene.heightProperty()));
    }
    
    public static void bindBorder(ObjectProperty<Border> border, Paint color, BorderStrokeStyle style, double cornerRadius, double borderWidth) {
        border.bind(Bindings.createObjectBinding(()->new Border(new BorderStroke(color, style, new CornerRadii(cornerRadius*getSizeFact()), new BorderWidths(borderWidth*getSizeFact()))), instance.scene.widthProperty(), instance.scene.heightProperty()));
    }
    
    public static void bindBorder(ObjectProperty<Border> border, Paint color, BorderStrokeStyle style, double c1, double c2, double c3, double c4, double borderWidth) {
        border.bind(Bindings.createObjectBinding(()->new Border(new BorderStroke(color, style, new CornerRadii(c1*getSizeFact(),c2*getSizeFact(),c3*getSizeFact(),c4*getSizeFact(),false), new BorderWidths(borderWidth*getSizeFact()))), instance.scene.widthProperty(), instance.scene.heightProperty()));
    }
    
    public static void bindBackground(ObjectProperty<Background> background, Paint color, double c1, double c2, double c3, double c4, double inset) {
        background.bind(Bindings.createObjectBinding(()->(new Background(new BackgroundFill(color, new CornerRadii(c1*getSizeFact(),c2*getSizeFact(),c3*getSizeFact(),c4*getSizeFact(),false), new Insets(inset*getSizeFact())))) , instance.scene.widthProperty(), instance.scene.heightProperty()));
    }
    
    public static DoubleBinding screenWidthMidProperty() {
        return instance.scene.widthProperty().divide(2);
    }
    
    public static DoubleBinding screenHeightMidProperty() {
        return instance.scene.heightProperty().divide(2);
    }
    
    public static ReadOnlyDoubleProperty screenHeightProperty() {
        return instance.scene.heightProperty();
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
