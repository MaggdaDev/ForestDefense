package maggdaforestdefense.config;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationManager {
    private static Configuration configuration;

    private static Path configFile = Paths.get("./config.json");

    public static Configuration getConfig() {
        if(configuration==null) {
            Charset charset = StandardCharsets.UTF_8;
            try (BufferedReader reader = Files.newBufferedReader(configFile, charset)) {
                configuration=new Gson().fromJson(reader, Configuration.class);
            } catch (IOException x) {
                setConfig(new Configuration());
                return new Configuration();
            }
        }
        return configuration;
    }

    public static boolean setConfig(Configuration newCfg) {
        Gson gson = new Gson();
        String s = gson.toJson(newCfg);
        Charset charset = StandardCharsets.UTF_8;
        try (BufferedWriter writer = Files.newBufferedWriter(configFile, charset)) {
            writer.write(s, 0, s.length());
            configuration = newCfg;
            return true;
        } catch (IOException x) {
            x.printStackTrace();
            return false;
        }
    }
}
