package maggdaforestdefense.auth;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
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
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.storage.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AuthWindow {
    public static final String CLIENT_ID = "a421b7d9de61f3385de9d7ffca3a4503";
    public static final String AUTH_URL = "https://wiki.minortom.net/rest.php/oauth2/authorize?useskin=example&approval_pass=1&redirect_uri=https%3A%2F%2Fforestdefense.minortom.net%2Foauth2%2Fcallback&client_id=" + CLIENT_ID + "&response_type=code";
    public static final String TOKEN_URL = "https://wiki.minortom.net/rest.php/oauth2/access_token";
    public static final String PROFILE_URL = "https://wiki.minortom.net/rest.php/oauth2/resource/profile";
    public static final String REDIR_URL = "https://forestdefense.minortom.net/oauth2/callback";
    public static final String SETTINGS_URL = "https://wiki.minortom.net/wiki/Special:Preferences";

    public static final String ANON_TOKEN = "Anonymous";

    private Afterwards afterwards;
    private Stage authStage;
    private Scene authScene;
    private VBox vBox;
    private HBox btnBox;
    private WebView signinView;
    private SwingNode browserNode;
    private Button cancelBtn;
    private Button anonBtn;

    private CookieManager cookieManager = new java.net.CookieManager();

    public AuthWindow(Afterwards afterwards) {
        this.afterwards = afterwards;

        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

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

            signinView = new WebView();
            signinView.setPrefHeight(640);
            com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
                    (webView, message, lineNumber, sourceId) ->
                            Logger.debugClient("WebView Console: [" + sourceId + ":" + lineNumber + "] " + message)
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
                    cookieManager.getCookieStore().removeAll();
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
        defaultCredentials.authToken = ANON_TOKEN;
        defaultCredentials.mwUser = MWUser.anonymous();
        signedIn(defaultCredentials);
    }

    private void codeSignIn(String code) {
        Logger.debugClient("Code " + code);
        TokenRes token = getAccessToken(code);
        MWUser u = getUserFromToken(token.getAccess_token());
        if(u==null) {
            new Alert(Alert.AlertType.ERROR, "Uh oh. Something went wrong.", ButtonType.OK).show();
        } else if (!u.isConfirmed_email()) {
            new Alert(Alert.AlertType.WARNING, "Please add a verified e-mail-address to your account to continue.", ButtonType.OK).show();
            cookieManager.getCookieStore().removeAll();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    /*try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(SETTINGS_URL));
                        }
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }*/
                    signinView.getEngine().load(AUTH_URL);
                }
            });
        } else {
            Credentials c = new Credentials();
            c.signedIn = true;
            c.userName = u.getUsername();
            c.userId = u.getUsername();
            c.authToken = token.getAccess_token();
            c.mwUser = u;
            signedIn(c);
        }
    }

    private void signedIn(Credentials credentials) {
        authStage.hide();
        Logger.logClient("Signed in with credentials " + new Gson().toJson(credentials));
        Configuration c = ConfigurationManager.getConfig();
        c.auth = credentials;
        if(!ConfigurationManager.setConfig(c)) {
            new Alert(Alert.AlertType.WARNING, "Configuration could not be saved.", ButtonType.OK).showAndWait();
        }
        afterwards.run();
    }

    public String refreshToken(String oldToken) {
        return oldToken;
    }

    public MWUser getUserFromToken(String token) {
        if(token.equals(ANON_TOKEN)) {
            return MWUser.anonymous();
        } else {
            URLConnection connection = null;
            try {
                connection = new URL(PROFILE_URL).openConnection();
                connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
                connection.setRequestProperty("Authorization", "Bearer " + token);
                InputStream response = connection.getInputStream();
                StringBuilder res = new StringBuilder();
                Reader reader = new BufferedReader(new InputStreamReader(response, Charset.forName(StandardCharsets.UTF_8.name())));
                    int c = 0;
                    while ((c = reader.read()) != -1) {
                        res.append((char) c);
                    }
                Logger.debugClient("Response for userfromtoken: " + ((HttpURLConnection) connection).getResponseCode() + " " + res.toString());
                if (((HttpURLConnection) connection).getResponseCode() == 200) {
                    return new Gson().fromJson(res.toString(), MWUser.class);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public TokenRes getAccessToken(String code) {
        try {
            //URLConnection connection = new URL(TOKEN_URL + "?" + "grant_type=authorization_code&code="+code).openConnection();
            URLConnection connection = new URL(TOKEN_URL).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.name());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + StandardCharsets.UTF_8.name());
            OutputStream output = connection.getOutputStream();
            output.write(("grant_type=authorization_code&client_id=" + CLIENT_ID + "&redirect_uri=" + REDIR_URL + "&code="+code).getBytes(StandardCharsets.UTF_8.name()));
            //output.write("".getBytes(StandardCharsets.UTF_8.name()));
            InputStream response = connection.getInputStream();
            StringBuilder res = new StringBuilder();
            Reader reader = new BufferedReader(new InputStreamReader(response, Charset.forName(StandardCharsets.UTF_8.name())));
            int c = 0;
            while ((c = reader.read()) != -1) {
                res.append((char) c);
            }
            Logger.debugClient("Response for getaccesstoken: " + ((HttpURLConnection) connection).getResponseCode() + " " + res.toString());
            if (((HttpURLConnection) connection).getResponseCode() == 200) {
                return new Gson().fromJson(res.toString(), TokenRes.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
