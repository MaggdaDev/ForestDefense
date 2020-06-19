/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

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
    
    private ObservableList<UpgradeButtonTierBox> boxes;
    
    private Button nextUpgradeTierButton, previousUpgradeTierButton;
    
    private HBox buttonMenuScrollBox;
    private int currentTierShowing = 1;
    
    private final UpgradeSet upgradeSet;
    
    private StackPane upgradeStackPane;
    
    public UpgradeMenu(ClientTower owner) {
        ownerTower = owner;
        gameObjectType = ownerTower.getType();
        
        ImageView treeView = new ImageView();
        treeView.setFitWidth(100);
        treeView.setPreserveRatio(true);
        treePane = new BorderPane(treeView);
        treePane.setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        
        treeNameLabel = new Label();
        treeNameLabel.setFont(font);
        
        
        // Set up upgrades scroll
        ImageView rightArrow = new ImageView(GameImage.MENUICON_ARROW_RIGHT.getImage());
        ImageView leftArrow = new ImageView(GameImage.MENUICON_ARROW_LEFT.getImage());
        rightArrow.setFitWidth(40);
        leftArrow.setFitWidth(40);
        rightArrow.setPreserveRatio(true);
        leftArrow.setPreserveRatio(true);
        previousUpgradeTierButton = new Button("", leftArrow);
        nextUpgradeTierButton = new Button("" ,  rightArrow);
        previousUpgradeTierButton.setOnAction((ActionEvent e)->{
            previousTier();
        });
        nextUpgradeTierButton.setOnAction((ActionEvent e)->{
           nextTier();
        });
        
        boxes = FXCollections.observableArrayList();
        upgradeSet = owner.getUpgradeSet();
        for(int tier = 1; tier <= upgradeSet.getMaxTier(); tier++) {
            UpgradeButtonTierBox box = new UpgradeButtonTierBox(tier, upgradeSet.getAllFromTier(tier));
            boxes.add(box);
            box.setVisible(false);
        }
        upgradeStackPane = new StackPane();
        upgradeStackPane.getChildren().setAll(boxes);

        buttonMenuScrollBox = new HBox(previousUpgradeTierButton, upgradeStackPane, nextUpgradeTierButton);
        buttonMenuScrollBox.setAlignment(Pos.CENTER);
      
        updateButtonDisable();
        updateUpgradesShowing();
        
        
        switch(gameObjectType) {
            case T_SPRUCE:
                treeNameLabel.setText("Spruce");
                treeView.setImage(GameImage.TOWER_SPRUCE_1.getImage());
                
                //UPGRADES
 
                break;
            
            default:
                throw new UnsupportedOperationException();
        }
          getChildren().addAll(treeNameLabel, treePane, new Separator(), buttonMenuScrollBox);
    }
    
    public void updateButtonDisable() {
 
        previousUpgradeTierButton.setDisable(currentTierShowing <= 1);
        
        nextUpgradeTierButton.setDisable(currentTierShowing >= upgradeSet.getMaxTier());
            
        
    }
    
    public void updateUpgradesShowing() {
        for(int i = 0; i < boxes.size(); i++) {
            UpgradeButtonTierBox currBox = boxes.get(i);
            if(i+1 == currentTierShowing) {
                currBox.setVisible(true);
            } else {
                currBox.setVisible(false);
            }
        }
    }
    
    public void nextTier() {
        currentTierShowing++;
        updateButtonDisable();
        updateUpgradesShowing();
    }
    
    public void previousTier() {
        currentTierShowing--;
        updateButtonDisable();
        updateUpgradesShowing();
    }
    
    public class UpgradeButtonTierBox extends VBox {
        
        public UpgradeButtonTierBox(int tier, Upgrade[] upgrades) {
            for(Upgrade upgrade: upgrades) {
                BuyUpgradeButton button = new BuyUpgradeButton(ownerTower, upgrade, true);
                getChildren().add(button);
            }
            
            setAlignment(Pos.CENTER);
            setSpacing(30);
            setFillWidth(false);
        }
    }
}
