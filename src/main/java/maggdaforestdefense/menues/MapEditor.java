package maggdaforestdefense.menues;

import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.Main;
import maggdaforestdefense.network.server.serverGameplay.GenerateableMap;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.storage.MapLoader;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MapEditor extends HBox {

    /**
     * Location for the Menu item Save to.
     * @see #menuClickSave()
     */
    private File saveLocation;

    // MENU Clicks

    /**
     * "Save": Saves the current map to the last storage location.
     */
    public void menuClickSave() {
        if(saveLocation!=null) {
            diskSaveMap(getMap(), saveLocation);
        } else {
            menuClickSaveAs();
        }
    }

    /**
     * "Save as": Saves the current map to a location picked by the user.
     */
    public void menuClickSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a location to save the map");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Forest Defense Maps", "*.fdmap"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All files" , "*.*")
        );
        fileChooser.setInitialFileName("map.fdmap");
        File file = fileChooser.showSaveDialog(MaggdaForestDefense.getInstance().getScene().getWindow());
        if(file != null&&!file.isDirectory()) {
            diskSaveMap(getMap(), file);
        } else {
            Logger.logClient("Didn't save");
        }
    }

    /**
     * "Load": The user picks a map file to load.
     */
    public void menuClickLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a map to load");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Forest Defense Maps", "*.fdmap"),
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("All files" , "*.*")
        );
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Forest Defense Maps", "*.fdmap"));
        File file = fileChooser.showOpenDialog(MaggdaForestDefense.getInstance().getScene().getWindow());
        if(file!=null&&!file.isDirectory()) {
            diskLoadMap(file);
            clean();
        } else {
            Logger.logClient("Didn't load");
        }
    }

    /**
     * "New": The user wants a new map.
     */
    public void menuClickNew() {
        clean();
        newMap();
    }

    /**
     * "Exit": The user wants to exit the editor.
     */
    public void menuClickExit() {
        clean();
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
    }

    // Helpers

    /**
     * Initializes the editor.
     */
    public void initialize() {
        MaggdaForestDefense.getInstance().getScene().getRoot().minHeight(700);
        MaggdaForestDefense.getInstance().getScene().getRoot().minWidth(1000);
    }

    /**
     * Populates the Editor with a new empty map.
     */
    public void newMap() {

    }

    /**
     * Saves file to disk.
     * @param map Map data to save.
     * @param file Location to save to.
     */
    private boolean diskSaveMap(GenerateableMap map, File file) {
        try {
            map.setVersion(GenerateableMap.CURRENT_VERSION);
            MapLoader.saveMap(map, file);
            saveLocation = file;
            return true;
        } catch (MapLoader.MapLoaderException e) {
            Logger.logClient(e.getText());
            e.getAdditionalData().printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Couldn't save map:\n" + e.getText()).showAndWait();
            return false;
        }
    }

    /**
     * Loads file from disk.
     * @param file Location to load from.
     */
    private boolean diskLoadMap(File file) {
        try {
            if(loadMap(MapLoader.loadMap(file))) {
                saveLocation = file;
                return true;
            } else {
                return false;
            }
        } catch (MapLoader.MapLoaderException e) {
            Logger.logClient(e.getText());
            e.getAdditionalData().printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Couldn't load map:\n" + e.getText()).showAndWait();
            return false;
        }
    }

    /**
     * Loads the given map into the editor.
     * @param map The map to load.
     */
    public boolean loadMap(GenerateableMap map) {
        if(map.getVersion()!=GenerateableMap.CURRENT_VERSION) {
            new Alert(Alert.AlertType.WARNING, "This map was saved with a different version of ForestDefense than you are running! You may have problems using or editing it.").showAndWait();
        }

        return true;
    }

    /**
     * Get the map from the editor.
     * @return The map.
     */
    public GenerateableMap getMap() {
        return null;
    }

    /**
     * Cleans up the editor for the next use.
     */
    public void clean() {
        saveLocation = null;
    }
}
