package maggdaforestdefense.storage;

import com.google.gson.Gson;
import javafx.stage.FileChooser;
import maggdaforestdefense.network.server.serverGameplay.GenerateableMap;
import maggdaforestdefense.network.server.serverGameplay.Map;
import org.apache.xml.dtm.ref.sax2dtm.SAX2RTFDTM;

import java.io.*;
import java.lang.reflect.Field;

public class MapLoader {
    public static GenerateableMap loadMap(File file) throws MapLoaderException {
        try {
            return new Gson().fromJson(new FileReader(file), GenerateableMap.class);
        } catch (FileNotFoundException e) {
            throw new MapLoaderException().setText("File not found.").setAdditionalData(e);
        } catch (Exception e) {
            throw new MapLoaderException().setText("File could not be loaded. Are you sure this is a map?").setAdditionalData(e);
        }

    }

    public static void saveMap(GenerateableMap map, File file) throws MapLoaderException {
        try {
            FileWriter fw = new FileWriter(file);
            new Gson().toJson(map, fw);
            fw.close();
        } catch (Exception e) {
            throw new MapLoaderException().setText("File could not be saved.").setAdditionalData(e);
        }
    }

    public static class MapLoaderException extends Exception  {
        private String text;
        private Exception additionalData;

        public String getText() {
            return text;
        }

        public MapLoaderException setText(String text) {
            this.text = text;
            return this;
        }

        public Exception getAdditionalData() {
            return additionalData;
        }

        public MapLoaderException setAdditionalData(Exception additionalData) {
            this.additionalData = additionalData;
            return this;
        }
    }
}
