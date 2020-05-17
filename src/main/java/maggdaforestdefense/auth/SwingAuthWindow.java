package maggdaforestdefense.auth;

import org.apache.http.impl.cookie.RFC2965DiscardAttributeHandler;
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

public class SwingAuthWindow {
    private Afterwards afterwards;

    private JFrame frame;
    private JButton cancelBtn;
    private JButton anonBtn;
    private Pandomium pandomium;
    private PandomiumClient client;
    private PandomiumBrowser browser;

    public SwingAuthWindow(Afterwards afterwards) {
        this.afterwards = afterwards;

        frame = new JFrame("Sign in");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cancel();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cancel();
            }
        });
        frame.getContentPane().add(BorderLayout.WEST, cancelBtn);
        anonBtn = new JButton("Anonymous");
        frame.getContentPane().add(BorderLayout.EAST, anonBtn);

        PandomiumSettings settings = PandomiumSettings.getDefaultSettingsBuilder()
                .argument("--disable-gpu")
                .argument("--disable-software-rasterizer")
                .build();

        pandomium = new Pandomium(settings);
        pandomium.initialize();

        client = pandomium.createClient();
        browser = client.loadURL("http://google.com");

        frame.getContentPane().add(BorderLayout.NORTH, browser.toAWTComponent());
    }

    public void show() {
        frame.setVisible(true);
    }

    private void cancel() {
        frame.dispose();
        System.exit(0);
    }
}
