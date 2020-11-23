package maggdaforestdefense.menues;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.gameplay.ClientMap;
import maggdaforestdefense.gameplay.ClientMapCell;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.gameplay.playerinput.PlayerInputHandler;
import maggdaforestdefense.gameplay.playerinput.RangeIndicator;
import maggdaforestdefense.gameplay.playerinput.SelectionClickedSquare;
import maggdaforestdefense.gameplay.playerinput.SelectionSqare;
import maggdaforestdefense.network.server.serverGameplay.GenerateableMap;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.storage.MapLoader;
import maggdaforestdefense.util.KeyEventHandler;

import java.io.File;
import java.net.InetAddress;

public class MapEditor extends VBox {

    /**
     * Location for the Menu item Save to.
     * @see #menuClickSave()
     */
    private File saveLocation;

    // Current map data

    private EditorPresetCell[][] presetCells;
    private int sizeX, sizeY;

    // FXML stuff

    @FXML public Group mapGroup;

    // MENU Clicks

    /**
     * "Save": Saves the current map to the last storage location.
     */
    public void menuClickSave() {
        if(MapEditor.getInstance().saveLocation!=null) {
            MapEditor.getInstance().diskSaveMap(getMap(), saveLocation);
        } else {
            MapEditor.getInstance().menuClickSaveAs();
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
            MapEditor.getInstance().diskSaveMap(getMap(), file);
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
            MapEditor.getInstance().clean();
            MapEditor.getInstance().diskLoadMap(file);
        } else {
            Logger.logClient("Didn't load");
        }
    }

    /**
     * "New": The user wants a new map.
     */
    public void menuClickNew() {
        MapEditor.getInstance().clean();
        MapEditor.getInstance().newMap();
    }

    /**
     * "Exit": The user wants to exit the editor.
     */
    public void menuClickExit() {
        MapEditor.getInstance().clean();
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.MAIN_MENU);
    }

    // Helpers

    /**
     * Initializes the editor.
     */
    /*public void firstInit() {
        MaggdaForestDefense.getInstance().getScene().getRoot().minHeight(700);
        MaggdaForestDefense.getInstance().getScene().getRoot().minWidth(1000);
        mapGroup = (Group) (((SplitPane) this.getChildren().get(1)).getItems().get(0)); // TODO: Get the group in a better way!

        setCursor(Cursor.OPEN_HAND);
        setOnMousePressed((MouseEvent e)->{
            PlayerInputHandler.getInstance().setMousePressed(true, e);
        });
        setOnMouseReleased((MouseEvent e)->{
            PlayerInputHandler.getInstance().setMousePressed(false, e);
            setCursor(Cursor.OPEN_HAND);
        });
        setOnMouseDragged((MouseEvent e)->{
            PlayerInputHandler.getInstance().mouseMoved(e);
            setCursor(Cursor.CLOSED_HAND);
        });

        mapGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), 0, 0));
        MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {
            scrolling += e.getDeltaY();
            mapGroup.getTransforms().clear();
            mapGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), maggdaforestdefense.MaggdaForestDefense.getWindowWidth() / 2, maggdaforestdefense.MaggdaForestDefense.getWindowHeight() / 2));

        });

        newMap();
    }*/
    public void firstInit() {
        MaggdaForestDefense.getInstance().getScene().getRoot().minHeight(700);
        MaggdaForestDefense.getInstance().getScene().getRoot().minWidth(1000);

        MapEditor.getInstance().setCursor(Cursor.OPEN_HAND);
        MapEditor.getInstance().setOnMousePressed((MouseEvent e)->{
            PlayerInputHandler.getInstance().setMousePressed(true, e);
        });
        MapEditor.getInstance().setOnMouseReleased((MouseEvent e)->{
            PlayerInputHandler.getInstance().setMousePressed(false, e);
            setCursor(Cursor.OPEN_HAND);
        });
        MapEditor.getInstance().setOnMouseDragged((MouseEvent e)->{
            PlayerInputHandler.getInstance().mouseMoved(e);
            setCursor(Cursor.CLOSED_HAND);
        });

        MapEditor.getInstance().mapGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), 0, 0));
        MaggdaForestDefense.getInstance().getScene().setOnScroll((ScrollEvent e) -> {
            MapEditor.getInstance().scrolling += e.getDeltaY();
            MapEditor.getInstance().mapGroup.getTransforms().clear();
            MapEditor.getInstance().mapGroup.getTransforms().add(new Scale(getScaleFromScroll(), getScaleFromScroll(), maggdaforestdefense.MaggdaForestDefense.getWindowWidth() / 2, maggdaforestdefense.MaggdaForestDefense.getWindowHeight() / 2));

        });

        MapEditor.getInstance().newMap();
    }

    private double scrolling = 0;

    private double getScaleFromScroll() {
        return Math.pow(1.001, scrolling);
    }

    /**
     * Populates the Editor with a new empty map.
     */
    public void newMap() {
        loadMap(new GenerateableMap());
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
        populateMap(map.getPresetCells(), map.getSizeX(), map.getSizeY());
        return true;
    }

    /**
     * Get the map from the editor.
     * @return The map.
     */
    public GenerateableMap getMap() {
        return new GenerateableMap().setSizeX(this.sizeX).setSizeY(this.sizeY).setPresetCells(this.presetCells);
    }

    /**
     * Cleans up the editor for the next use.
     */
    public void clean() {
        saveLocation = null;
        mapGroup.getChildren().clear();
    }

    /**
     * Populates the Map with the tiles.
     * @param oldCells The cells to put there.
     * @param sizeX New X size
     * @param sizeY New Y size
     */
    private void populateMap(GenerateableMap.PresetCellInterface[][] oldCells, int sizeX, int sizeY) {
        mapGroup.getChildren().clear();
        EditorPresetCell[][] newCells = new EditorPresetCell[sizeX][sizeY];
        for (int x = 0; x < newCells.length; x++) {
            EditorPresetCell[] yArray = new EditorPresetCell[sizeY];
            for (int y = 0; y < yArray.length; y++) {
                if(oldCells[x][y] !=null && oldCells[x][y].getType() != null) {
                    yArray[y] = new EditorPresetCell(oldCells[x][y]);
                } else {
                    yArray[y] = new EditorPresetCell(x, y);
                }
                yArray[y].initFx();
                mapGroup.getChildren().addAll(yArray[y]);
            }
            newCells[x] = yArray;
        }
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        presetCells = newCells;
        new InputHandler();
    }

    /**
     * Get the map cells for saving
     * @return Cell array
     */
    private GenerateableMap.PresetCellInterface[][] getPresetCells() {
        return presetCells;
    }

    protected EditorPresetCell[][] getCells() {
        return presetCells;
    }

    // Editor
    private ClickHandlerState clickHandlerState;

    public void cellClick(int posX, int posY) {

    }

    @FXML public TextField textMapSize;

    @FXML private void applyMapSize() {
        int newSize = 0;
        try {
            newSize = Integer.parseInt(textMapSize.getCharacters().toString());
        } catch (NumberFormatException e) {}
        if(newSize>1 && newSize<500) {
            MapEditor.getInstance().populateMap(presetCells, newSize, newSize);
        } else {
            new Alert(Alert.AlertType.WARNING, "You can't set this size").showAndWait();
        }
    }

    // Instance

    private static MapEditor instance;

    /*public MapEditor() {
        instance = this;
    }*/
    /**
     * Initializes the instance.
     * Populated after fxml-loading.
     */
    public void initialize() {
        MapEditor.instance = this;
    }

    public static MapEditor getInstance() {
        return instance;
    }

    // Classes
    public class EditorPresetCell extends StackPane implements GenerateableMap.PresetCellInterface {
        private MapCell.CellType type;
        private int posX, posY;

        public EditorPresetCell(int posX, int posY) {
            type = MapCell.CellType.DIRT;
            this.posX = posX;
            this.posY = posY;
        }

        public EditorPresetCell(GenerateableMap.PresetCellInterface cell) {
            this.type = cell.getType();
            this.posX = cell.getPosX();
            this.posY = cell.getPosY();
        }

        public void initFx() {
            imageView = new ImageView(type.getImage());
            imageView.setFitHeight(MapCell.CELL_SIZE);
            imageView.setFitWidth(MapCell.CELL_SIZE);

            getChildren().add(imageView);

            setLayoutX((((double) (getPosX())) + 0.0d) * MapCell.CELL_SIZE);
            setLayoutY((((double) (getPosY())) + 0.0d) * MapCell.CELL_SIZE);

            setOnMouseEntered((MouseEvent e) -> {
                addSelectionSquare();
            });

            setOnMouseExited((MouseEvent e) -> {
                removeSelectionSquare();
            });
            setOnMouseClicked((MouseEvent e) -> {
                InputHandler.getInstance().mapCellClicked(this);
                SelectionClickedSquare.getInstance().addToMapCell(this);
            });
        }

        // FX Stuff
        private ImageView imageView;
        private boolean isSelectionQuare = false, isPlanted = false, isSelectionClickedSquare = false;

        public void addSelectionSquare() {
            if (!isSelectionQuare) {
                isSelectionQuare = true;
                getChildren().add(SelectionSqare.getInstance());
            }
        }

        public void addSelectionClickedSquare() {
            isSelectionClickedSquare = true;
            if(!getChildren().contains(SelectionClickedSquare.getInstance())) {
                getChildren().add(SelectionClickedSquare.getInstance());
            }
        }

        public void removeSelectionClickedSquare() {
            isSelectionClickedSquare = false;
            if(getChildren().contains(SelectionClickedSquare.getInstance())) {
                getChildren().remove(SelectionClickedSquare.getInstance());
            }
        }

        public void removeSelectionSquare() {
            if (isSelectionQuare) {
                isSelectionQuare = false;
                getChildren().remove(SelectionSqare.getInstance());
            }
        }

        // GETTER AND SETTER

        public MapCell.CellType getType() {
            return type;
        }

        public void setType(MapCell.CellType type) {
            this.type = type;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return posY;
        }

        public void setPosY(int posY) {
            this.posY = posY;
        }
    }

    public static enum ClickHandlerState {
        TILE_PLACE,
        TILE_EDIT,
        BIOM_SINGLE
    }

    // Copied stuff

    public static class SelectionSqare extends Rectangle {

        private EditorPresetCell mapCell;

        private static SelectionSqare instance;

        public SelectionSqare() {
            instance = this;
            mapCell = MapEditor.getInstance().getCells()[0][0];

            setWidth(MapCell.CELL_SIZE-4);
            setHeight(MapCell.CELL_SIZE-4);
            setFill(Color.TRANSPARENT);
            setStroke(Color.GRAY);
            setStrokeWidth(4);
        }

        public void updatePosition(MouseEvent e) {
            int xIndex = (int) Math.round(e.getSceneX() / MapCell.CELL_SIZE -1);
            int yIndex = (int) Math.round(e.getSceneY() / MapCell.CELL_SIZE -1);
            if (xIndex >= MapEditor.getInstance().getCells().length) {
                xIndex = MapEditor.getInstance().getCells().length - 1;
            }
            if (yIndex >= MapEditor.getInstance().getCells()[xIndex].length) {
                yIndex = MapEditor.getInstance().getCells()[xIndex].length - 1;
            }
            EditorPresetCell newCell = MapEditor.getInstance().getCells()[xIndex][yIndex];
            if(newCell != mapCell) {
                mapCell.removeSelectionSquare();
                mapCell = newCell;
                mapCell.addSelectionSquare();
            }
        }

        public static SelectionSqare getInstance() {
            return instance;
        }
    }

    public static class SelectionClickedSquare extends Rectangle {

        private EditorPresetCell mapCell;

        private static SelectionClickedSquare instance;

        public SelectionClickedSquare() {
            instance = this;
            mapCell = MapEditor.getInstance().getCells()[0][0];

            setWidth(MapCell.CELL_SIZE - 6);
            setHeight(MapCell.CELL_SIZE - 6);
            setFill(Color.TRANSPARENT);
            setStroke(Color.web("484848"));
            setStrokeWidth(6);
        }

        public static SelectionClickedSquare getInstance() {
            return instance;
        }

        public void addToMapCell(EditorPresetCell c) {
            mapCell.removeSelectionClickedSquare();
            mapCell = c;
            mapCell.addSelectionClickedSquare();
        }
    }

    public static class InputHandler {

        private SelectionSqare selectionSquare;
        private SelectionClickedSquare selectionClickedSquare;

        private static InputHandler instance;

        private double dragStartMouseX, dragStartMouseY, dragStartLayoutX, dragStartLayoutY;
        private boolean mousePressed = false;

        public InputHandler() {
            instance = this;

            selectionSquare = new SelectionSqare();
            selectionClickedSquare = new SelectionClickedSquare();

        }

        public void mapCellClicked(EditorPresetCell clickedCell) {
            MapEditor.getInstance().cellClick(clickedCell.posX, clickedCell.posY);
        }

        public static InputHandler getInstance() {
            return instance;
        }

        public void setMousePressed(boolean b, MouseEvent e) {
            if (mousePressed == false && b == true) {
                dragStartMouseX = e.getX();
                dragStartMouseY = e.getY();
                dragStartLayoutX = MapEditor.getInstance().mapGroup.getLayoutX();
                dragStartLayoutY = MapEditor.getInstance().mapGroup.getLayoutY();
                Logger.logClient("New Start");
            }
            mousePressed = b;

        }

        public void mouseMoved(MouseEvent e) {
            if (mousePressed) {
                Logger.logClient("moved");
                MapEditor.getInstance().mapGroup.setLayoutX(dragStartLayoutX + e.getX() - dragStartMouseX);
                MapEditor.getInstance().mapGroup.setLayoutY(dragStartLayoutY + e.getY() - dragStartMouseY);
            }
        }

    }


}
