package maggdaforestdefense.auth;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.auth.Credentials;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.storage.Logger;

import java.util.Random;

public class AuthWindow {
    private Stage primaryStage;
    private MaggdaForestDefense defense;
    private Stage authStage;
    private Scene authScene;
    private VBox vBox;
    private HBox btnBox;
    private WebView signinView;
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
            Credentials defaultCredentials = new Credentials();
            defaultCredentials.userId = "Anonymous";
            defaultCredentials.userName = "Anonymous #" + new Random().nextInt(100);
            signedIn(defaultCredentials);
        });

        btnBox = new HBox(cancelBtn, anonBtn);
        btnBox.setSpacing(10);
        //btnBox.setAlignment(new Align);

        signinView = new WebView();
        signinView.setPrefHeight(640);
        signinView.getEngine().setUserAgent("Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0");
        signinView.getEngine().load("https://wiki.minortom.net?useskin=example");

        vBox = new VBox(signinView, btnBox);
        vBox.setSpacing(10);

        authScene = new Scene(vBox);
        authStage.setScene(authScene);
    }

    public void show() {
        authStage.setResizable(false);
        authStage.setAlwaysOnTop(true);
        authStage.setHeight(690);
        authStage.setWidth(370);
        authStage.show();
    }

    private void cancel() {
        authStage.hide();
        System.exit(0);
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
