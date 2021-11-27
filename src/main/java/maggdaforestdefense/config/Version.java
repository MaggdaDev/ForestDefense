package maggdaforestdefense.config;

import maggdaforestdefense.storage.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Version {
    public static String version;

    public static String getVersion() {
        if(version==null) {
            version = readVersion();
        }
        return version;
    }

    private static String readVersion() {
        try {
            InputStream stream = Version.class.getClassLoader().getResourceAsStream("maggdaforestdefense/config/version.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            while (reader.ready()) {
                builder.append(reader.readLine());
            }
            reader.close();
            stream.close();
            return builder.toString();
        } catch (IOException e) {
            Logger.logClient("Can't read version");
            return "?";
        }
    }
}
