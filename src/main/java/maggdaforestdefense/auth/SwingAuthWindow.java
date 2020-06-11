package maggdaforestdefense.auth;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import maggdaforestdefense.config.Configuration;
import maggdaforestdefense.config.ConfigurationManager;
import maggdaforestdefense.storage.Logger;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefAuthCallback;
import org.cef.callback.CefJSDialogCallback;
import org.cef.callback.CefRequestCallback;
import org.cef.handler.CefJSDialogHandler;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefResourceHandler;
import org.cef.misc.BoolRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.cef.network.CefURLRequest;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static maggdaforestdefense.auth.AuthWindow.ANON_TOKEN;

public class SwingAuthWindow {
    private Afterwards afterwards;

    private JFrame frame;
    private JPanel btnPanel;
    private JButton cancelBtn;
    private JButton anonBtn;
    private JButton reloadBtn;
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
        reloadBtn = new JButton("Reload");
        reloadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //frame.getContentPane().remove(browser.toAWTComponent());
                //browser = client.loadURL(AuthWindow.AUTH_URL);
                //frame.getContentPane().add(BorderLayout.CENTER, browser.toAWTComponent());
                while (browser.getCefBrowser().canGoBack()) {
                    browser.getCefBrowser().goBack();
                }
                browser.getCefBrowser().reload();
            }
        });
        btnPanel.add(reloadBtn);
        frame.getContentPane().add(BorderLayout.SOUTH, btnPanel);

        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder()
                .argument("--disable-gpu")
                .argument("--disable-software-rasterizer")
//                .argument("--user-agent=\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0\"")
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
        client.getCefClient().addRequestHandler(new CefRequestHandler() {
            @Override
            public boolean onBeforeBrowse(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest, boolean b, boolean b1) {
                return false;
            }

            @Override
            public boolean onBeforeResourceLoad(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest) {
                if(cefRequest.getURL().startsWith("https://accounts.google.com")) {
                    Map<String, String> headerMap = new HashMap<String,String>();
                    cefRequest.getHeaderMap(headerMap);
                    headerMap.remove("User-Agent");
                    headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0");
                    cefRequest.setHeaderMap(headerMap);
                }
                return false;
            }

            @Override
            public CefResourceHandler getResourceHandler(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest) {
                return null;
            }

            @Override
            public void onResourceRedirect(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest, CefResponse cefResponse, StringRef stringRef) {

            }

            @Override
            public boolean onResourceResponse(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest, CefResponse cefResponse) {
                return false;
            }

            @Override
            public void onResourceLoadComplete(CefBrowser cefBrowser, CefFrame cefFrame, CefRequest cefRequest, CefResponse cefResponse, CefURLRequest.Status status, long l) {

            }

            @Override
            public boolean getAuthCredentials(CefBrowser cefBrowser, CefFrame cefFrame, boolean b, String s, int i, String s1, String s2, CefAuthCallback cefAuthCallback) {
                return false;
            }

            @Override
            public boolean onQuotaRequest(CefBrowser cefBrowser, String s, long l, CefRequestCallback cefRequestCallback) {
                return false;
            }

            @Override
            public void onProtocolExecution(CefBrowser cefBrowser, String s, BoolRef boolRef) {

            }

            @Override
            public boolean onCertificateError(CefBrowser cefBrowser, CefLoadHandler.ErrorCode errorCode, String s, CefRequestCallback cefRequestCallback) {
                return false;
            }

            @Override
            public void onPluginCrashed(CefBrowser cefBrowser, String s) {

            }

            @Override
            public void onRenderProcessTerminated(CefBrowser cefBrowser, TerminationStatus terminationStatus) {

            }
        });
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
