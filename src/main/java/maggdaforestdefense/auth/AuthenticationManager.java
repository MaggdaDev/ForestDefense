package maggdaforestdefense.auth;

import com.google.api.client.auth.oauth2.Credential;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.menues.MenuManager;
import maggdaforestdefense.storage.Logger;

public class AuthenticationManager {

    private static AuthenticationManager instance;
    public static AuthenticationManager getInstance() {
        if(instance==null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    private AuthUser user;
    private AuthCredentials credentials;

    public AuthenticationManager() {

    }

    public AuthUser getUser() {
        if(user==null) {
            try {
                user = getCredentials().getAuthUser();
            } catch (AuthenticationException e) {
                handleException(e);
                credentials = new AnonAuthCredentials();
                try {
                    user = getCredentials().getAuthUser();
                } catch (AuthenticationException e1) {
                    Logger.errClient("That shouldn't happen (AuthenticationManager)", e1);
                }
            }
        }
        return user;
    }

    public AuthCredentials getCredentials() {
        if(credentials==null) {
            Configuration configuration = ConfigurationManager.getConfig();
            if(configuration.isAnon()) {
                credentials = new AnonAuthCredentials();
            } else {
                try {
                    credentials = IenokihpkgAuthHelper.signIn();
                } catch (AuthenticationException e) {
                    handleException(e);
                    credentials = new AnonAuthCredentials();
                }
            }
        }
        return credentials;
    }

    public void signIn() {
        try {
            credentials = IenokihpkgAuthHelper.signIn();
            user = null;
            Configuration configuration = ConfigurationManager.getConfig();
            configuration.setAnon(false);
            ConfigurationManager.setConfig(configuration);
            Platform.runLater(() -> MenuManager.getInstance().updateUserInfo());
        } catch (AuthenticationException e) {
            handleException(e);
        }
    }

    public void signOut() {
        try {
            IenokihpkgAuthHelper.signOut();
            credentials = null;
            user = null;
            Configuration configuration = ConfigurationManager.getConfig();
            configuration.setAnon(true);
            ConfigurationManager.setConfig(configuration);
            Platform.runLater(() -> MenuManager.getInstance().updateUserInfo());
        } catch (AuthenticationException e) {
            handleException(e);
        }
    }

    private void handleException(AuthenticationException e) {
        Logger.errClient("Authentication Exception in AuthenticationManager", e);
        Configuration configuration = ConfigurationManager.getConfig();
        configuration.setAnon(true);
        ConfigurationManager.setConfig(configuration);
        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, "Couldn't authenticate you:\n" + e.getReason(), ButtonType.OK).show());
    }
}
