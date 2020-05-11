package maggdaforestdefense.auth.client;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AuthWindow {
    private Stage authStage;
    private Scene authScene;
    private VBox vBox;
    private HBox btnBox;
    private WebView signinView;
    private Button cancelBtn;

    public AuthWindow() {
        authStage = new Stage();
        authStage.setTitle("Log in");

        cancelBtn = new Button("Cancel");

        btnBox = new HBox(cancelBtn);
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

}
