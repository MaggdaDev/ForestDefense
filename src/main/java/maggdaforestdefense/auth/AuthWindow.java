package maggdaforestdefense.auth;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.storage.Logger;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.util.Random;

public class AuthWindow {
    public static final String CLIENT_ID = "a421b7d9de61f3385de9d7ffca3a4503";
    public static final String AUTH_URL = "https://wiki.minortom.net/rest.php/oauth2/authorize?useskin=example&approval_pass=1&redirect_uri=https%3A%2F%2Fforestdefense.minortom.net%2Foauth2%2Fcallback&client_id=" + CLIENT_ID + "&response_type=code";
    public static final String TOKEN_URL = "https://wiki.minortom.net/rest.php/oauth2/access_token";
    public static final String REDIR_URL = "https://forestdefense.minortom.net/oauth2/callback";

    public static final boolean USE_PANDOMIUM = false;

    private Stage primaryStage;
    private MaggdaForestDefense defense;
    private Stage authStage;
    private Scene authScene;
    private VBox vBox;
    private HBox btnBox;
    private WebView signinView;
    private Pandomium pandomium;
    private PandomiumClient pandomiumClient;
    private PandomiumBrowser pandomiumBrowser;
    private SwingNode browserNode;
    private Button cancelBtn;
    private Button anonBtn;

    public AuthWindow(MaggdaForestDefense defense, Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.defense = defense;
        authStage = new Stage();
        authStage.setTitle("Log in");

        authStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                cancel();
            }
        });

        cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> cancel());
        anonBtn = new Button("Sign in anonymously");
        anonBtn.setOnAction(event -> {
            anonSignIn();
        });

        btnBox = new HBox(cancelBtn, anonBtn);
        btnBox.setSpacing(10);
        //btnBox.setAlignment(new Align);

        if(!USE_PANDOMIUM) {
            signinView = new WebView();
            signinView.setPrefHeight(640);
            com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
                    (webView, message, lineNumber, sourceId) ->
                            Logger.logClient("WebView Console: [" + sourceId + ":" + lineNumber + "] " + message)
            );
            signinView.getEngine().setUserAgent("Mozilla/5.0 (Java; ForestDefense x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
            signinView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
                String location = (String)newValue;
                int index = location.indexOf("code=");
                if (index >= 0) {
                    String code = location.substring(index + 5);
                    codeSignIn(code);
                }
            });
            signinView.getEngine().locationProperty().addListener((observable, oldValue, newValue) -> {
                String location = (String)newValue;
                int index = location.indexOf("error=unauthorized_client");
                if (index >= 0) {
                    new Alert(Alert.AlertType.WARNING, "You did not authorize the access", ButtonType.OK).show();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            signinView.getEngine().load(AUTH_URL);
                        }
                    });
                }
            });

            //signinView.getEngine().load("https://wiki.minortom.net?useskin=example");
            signinView.getEngine().load(AUTH_URL);

            vBox = new VBox(signinView, btnBox);
        } else {
            PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder()
                    //.proxy("localhost", 20) // blank page
                    .build();

            Logger.logClient(settings.getNatives().getNativeDirectory());
            pandomium = new Pandomium(settings);
            //Alert fetchingData = new Alert(Alert.AlertType.INFORMATION, "Fetching some additional data, this might take a while", ButtonType.OK);
            //fetchingData.showAndWait();
            //if(fetchingData.showAndWait().orElse(ButtonType.CANCEL)==ButtonType.CANCEL) {
            //    anonSignIn();
            //    return;
            //} else {
                System.setProperty("java.library.path", System.getProperty("java.library.path") + ":" + settings.getNatives().getNativeDirectory());
                Logger.logClient(System.getProperty("java.library.path"));
                pandomium.initialize();


                pandomiumClient = pandomium.createClient();
                pandomiumBrowser = pandomiumClient.loadURL("https://wiki.minortom.net?useskin=example");

                browserNode = new SwingNode();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        browserNode.setContent((JComponent) pandomiumBrowser.toAWTComponent());
                    }
                });

            //}

            vBox = new VBox(browserNode, btnBox);
        }
        vBox.setSpacing(10);

        authScene = new Scene(vBox);
        authStage.setScene(authScene);
    }

    public void show() {
        authStage.setResizable(false);
        //authStage.setAlwaysOnTop(true);
        authStage.setHeight(690);
        authStage.setWidth(370);
        //authStage.setIconified(true);
        authStage.show();
    }

    private void cancel() {
        authStage.hide();
        System.exit(0);
    }

    private void anonSignIn() {
        Credentials defaultCredentials = new Credentials();
        defaultCredentials.userId = "Anonymous";
        defaultCredentials.userName = "Anonymous #" + new Random().nextInt(100);
        defaultCredentials.authToken = "Anonymous";
        signedIn(defaultCredentials);
    }

    private void codeSignIn(String code) {
        Logger.logClient("Code " + code);
    }

    private void signedIn(Credentials credentials) {
        authStage.hide();
        Logger.logClient("Signed in with credentials " + new Gson().toJson(credentials));
        Configuration c = ConfigurationManager.getConfig();
        c.auth = credentials;
        if(!ConfigurationManager.setConfig(c)) {
            new Alert(Alert.AlertType.WARNING, "Configuration could not be saved.", ButtonType.OK).showAndWait();
        }
        defense.mainApp(primaryStage, credentials);
    }
}
