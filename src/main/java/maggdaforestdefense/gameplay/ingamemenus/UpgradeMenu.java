/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class UpgradeMenu extends VBox{
    public final static Font font = new Font(40);
    
    private GameObjectType gameObjectType;
    private ClientTower ownerTower;
    
    private BorderPane treePane;
    private Label treeNameLabel;
    
    public UpgradeMenu(ClientTower owner) {
        ownerTower = owner;
        gameObjectType = ownerTower.getType();
        
        ImageView treeView = new ImageView();
        treePane = new BorderPane(treeView);
        treePane.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        
        treeNameLabel = new Label();
        treeNameLabel.setFont(font);
        
        getChildren().addAll(treeNameLabel, treePane);
        
        
        
        switch(gameObjectType) {
            case T_SPRUCE:
                treeNameLabel.setText("Spruce");
                treeView.setImage(GameImage.TOWER_SPRUCE_1.getImage());
                break;
            
            default:
                throw new UnsupportedOperationException();
        }
    }
}
