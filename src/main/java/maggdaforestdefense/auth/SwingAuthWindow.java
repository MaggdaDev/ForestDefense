package maggdaforestdefense.auth;

import com.google.gson.Gson;
import org.panda_lang.pandomium.Pandomium;
import org.panda_lang.pandomium.settings.PandomiumSettings;
import org.panda_lang.pandomium.wrapper.PandomiumBrowser;
import org.panda_lang.pandomium.wrapper.PandomiumClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import static maggdaforestdefense.auth.AuthWindow.ANON_TOKEN;
import static maggdaforestdefense.auth.AuthWindow.AUTH_URL;
import static maggdaforestdefense.auth.AuthWindow.CLIENT_ID;
import static maggdaforestdefense.auth.AuthWindow.PROFILE_URL;
import static maggdaforestdefense.auth.AuthWindow.REDIR_URL;
import static maggdaforestdefense.auth.AuthWindow.TOKEN_URL;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.storage.Logger;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefJSDialogCallback;
import org.cef.handler.CefJSDialogHandler;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.BoolRef;

public class SwingAuthWindow {
    private Afterwards afterwards;

    private JFrame frame;
    private JPanel btnPanel;
    private JButton cancelBtn;
    private JButton anonBtn;
    private Pandomium pandomium;
    private PandomiumClient client;
    private PandomiumBrowser browser;

    /**
     * Creates an authentication window using {@link Pandomium} and Swing.
     * @param afterwards What to execute when the user is signed in.
     * @author MinorTom
     * */
    public SwingAuthWindow(Afterwards afterwards) {
        this.afterwards = afterwards;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Sign in");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(375, 695);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });

        btnPanel = new JPanel();
        
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancel();
            }
        });
        btnPanel.add(cancelBtn);
        anonBtn = new JButton("Anonymous");
        anonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        anonSignIn();
                    }
                });
            }
        });
        btnPanel.add(anonBtn);
        frame.getContentPane().add(BorderLayout.SOUTH, btnPanel);

        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder()
                .argument("--disable-gpu")
                .argument("--disable-software-rasterizer")
                .build();

        pandomium = new Pandomium(settings);
        pandomium.initialize();

        client = pandomium.createClient();
        client.getCefClient().addJSDialogHandler(new CefJSDialogHandler() {
            @Override
            public boolean onJSDialog(CefBrowser browser, String origin_url, JSDialogType dialog_type,
            String message_text, String default_prompt_text, CefJSDialogCallback callback,
            BoolRef suppress_message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        int index = (message_text).indexOf("code=");
                        if (index >= 0) {
                            String code = (message_text).substring(index + 5);
                            codeSignIn(code);
                        }
                        index = (message_text).indexOf("error=unauthorized_client");
                        if (index >= 0) {
                            new Alert(Alert.AlertType.WARNING, "You did not authorize the access", ButtonType.OK).showAndWait();
                            cancel();
                        }

                    }
                });
                return true;
            }
            
            @Override
            public boolean onBeforeUnloadDialog(CefBrowser browser, String message_text, boolean is_reload,
            CefJSDialogCallback callback) {
                return false;
            }
            
            @Override
            public void onResetDialogState(CefBrowser browser) {
                
            }
            
            @Override
            public void onDialogClosed(CefBrowser browser) {
                
            }
        });
        /*client.getCefClient().addResourceHandler(new CefResourceHandler() {
            @Override
            public boolean onBeforeBrowse(CefBrowser browser,
                                        CefRequest request,
                                        boolean is_redirect) {
                return false;
            }

            @Override
            public boolean onBeforeResourceLoad(CefBrowser browser, CefRequest request) {
                return false;
            }

            @Override
            public CefResourceHandler getResourceHandler(CefBrowser browser,
                                                       CefRequest request) {
                return null;
            }



            @Override

            public void onResourceRedirect(CefBrowser browser,
                                         String old_url,
                                         StringRef new_url) {
            }

            @Override
            public boolean getAuthCredentials(CefBrowser browser,
                                            boolean isProxy,
                                            String host,
                                            int port,
                                            String realm,
                                            String scheme,
                                            CefAuthCallback callback) {
                return false;
            }

            @Override
            public boolean onQuotaRequest(CefBrowser browser,
                                        String origin_url,
                                        long new_size,
                                        CefQuotaCallback callback) {
                return false;
            }

            @Override
            public void onProtocolExecution(CefBrowser browser,
                                          String url,
                                          BoolRef allow_os_execution) {
            }

            @Override
            public boolean onCertificateError(ErrorCode cert_error,
                                            String request_url,
                                            CefAllowCertificateErrorCallback callback) {
                return false;
            }

            @Override
            public boolean onBeforePluginLoad(CefBrowser browser,
                                            String url,
                                            String policyUrl,
                                            CefWebPluginInfo info) {
                return false;
            }

            @Override
            public void onPluginCrashed(CefBrowser browser,
                                      String pluginPath) {
            }

            @Override
            public void onRenderProcessTerminated(CefBrowser browser,
                                                TerminationStatus status) {
            }
        });*/
        browser = client.loadURL(AuthWindow.AUTH_URL);

        frame.getContentPane().add(BorderLayout.CENTER, browser.toAWTComponent());
    }

    /**
     * Shows the previously created auth. window.
     */
    public void show() {
        frame.setVisible(true);
    }

    /**
     * Used to cancel the authentication (e.g. when the button "cancel" is pressed).
     */
    private void cancel() {
        frame.dispose();
        System.exit(0);
    }

    /**
     * Used to sign the user in anonymously.
     */
    private void anonSignIn() {
        Credentials defaultCredentials = new Credentials();
        defaultCredentials.userId = "Anonymous";
        defaultCredentials.userName = "Anonymous #" + new Random().nextInt(100);
        defaultCredentials.authToken = ANON_TOKEN;
        defaultCredentials.mwUser = MWUser.anonymous();
        signedIn(defaultCredentials);
    }

    /**
     * Used to validate a code got from signing the user in.
     * @param code The code from authenticating the user, according to OAuth2.
     */
    private void codeSignIn(String code) {
        Logger.debugClient("Code " + code);
        TokenRes token = AuthWindow.getAccessToken(code);
        MWUser u = AuthWindow.getUserFromToken(token.getAccess_token());
        if(u==null) {
            new Alert(Alert.AlertType.ERROR, "Uh oh. Something went wrong.", ButtonType.OK).show();
        } else if (!u.isConfirmed_email()) {
            new Alert(Alert.AlertType.WARNING, "Please add a verified e-mail-address to your account to continue.", ButtonType.OK).show();
            AuthWindow.openBrowser(AuthWindow.SETTINGS_URL);
            frame.getContentPane().remove(browser.toAWTComponent());
            browser = client.loadURL(AuthWindow.AUTH_URL);
            frame.getContentPane().add(BorderLayout.CENTER, browser.toAWTComponent());
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

    /**
     * Called by {@link SwingAuthWindow#anonSignIn()} and {@link SwingAuthWindow#codeSignIn(String)}.
     * @param credentials The credentials used to continue.
     */
    private void signedIn(Credentials credentials) {
        frame.dispose();
        Logger.logClient("Signed in with credentials " + new Gson().toJson(credentials));
        Configuration c = ConfigurationManager.getConfig();
        c.auth = credentials;
        if(!ConfigurationManager.setConfig(c)) {
            new Alert(Alert.AlertType.WARNING, "Configuration could not be saved.", ButtonType.OK).showAndWait();
        }
        afterwards.run();
    }
}
