/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.menues;

import java.awt.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class FindGameMenu extends VBox{
    private FXMLLoader loader;

    @FXML
    private Button backBtn;
    
    @FXML
    private VBox contentVBox;

    public FindGameMenu() {
        Logger.debugClient("FXML loading works");
    }

    public void initialize() {
        Logger.debugClient("FXML loading actually works");

    }
    
    public void addEntry(String id) {
        contentVBox.getChildren().addAll(new Separator(), new JoinGameEntry(id));
    }
    
    @FXML private void back(ActionEvent e) {
        MenuManager.getInstance().setScreenShown(MenuManager.Screen.PLAY);
    }
    
    public static class JoinGameEntry extends HBox{
        private String gameId;
        private Label idLabel;
        private Button joinBtn;
        public JoinGameEntry(String id) {
            gameId = id;
            idLabel = new Label("Game-ID: " + id);
            joinBtn = new Button("JOIN");
            
            setSpacing(50);
            
            getChildren().addAll(idLabel, joinBtn);
        }
    }

}
