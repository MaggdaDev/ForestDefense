/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import java.util.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.NodeSizer;

/**
 *
 * @author DavidPrivat
 */
public class UpgradeMenu extends VBox{
    public final static double ARROW_SIZE = 40, TREE_VIEW_WIDTH = 100;
    
    private GameObjectType gameObjectType;
    private ClientTower ownerTower;
    
    private VBox treePane;
    private Label treeNameLabel;
    private ImageView treeView;
    
    private ObservableList<UpgradeButtonTierBox> boxes;
  
    
    private Button nextUpgradeTierButton, previousUpgradeTierButton;
    
    private HBox buttonMenuScrollBox;
    private int currentTierShowing = 1, currentTierToBuy = 1;
    
    private final UpgradeSet upgradeSet;
    
    private StackPane upgradeStackPane;
    
    private BoughtUpgradesBox boughtUpgradesBox;
    
    private SelectedUpgradeBox selectedUpgradeBox;
    
    private Vector<Upgrade> lorbeerTradingUpgrades;
    
    public UpgradeMenu(ClientTower owner) {
        ownerTower = owner;
        gameObjectType = ownerTower.getType();
        lorbeerTradingUpgrades = new Vector<>();
        
        treeView = new ImageView();
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(treeView.fitWidthProperty(), TREE_VIEW_WIDTH);
        treeView.setPreserveRatio(true);
        boughtUpgradesBox = new BoughtUpgradesBox(owner.getUpgradeSet().getMaxTier());
        
        
        treeNameLabel = new Label();
        treePane = new ContentBox();
        treePane.getChildren().addAll(treeNameLabel, treeView, boughtUpgradesBox);
        
        
        treePane.setAlignment(Pos.CENTER);
        treePane.setFillWidth(false);
       maggdaforestdefense.MaggdaForestDefense.bindToHeight(treePane.spacingProperty(), 10);
        
        
        
        selectedUpgradeBox = new SelectedUpgradeBox();
        
        
        // Set up upgrades scroll
        ImageView rightArrow = new ImageView(GameImage.MENUICON_ARROW_RIGHT.getImage());
        ImageView leftArrow = new ImageView(GameImage.MENUICON_ARROW_LEFT.getImage());
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(rightArrow.fitWidthProperty(), ARROW_SIZE);
        maggdaforestdefense.MaggdaForestDefense.bindToHeight(leftArrow.fitWidthProperty(), ARROW_SIZE);
        rightArrow.setPreserveRatio(true);
        leftArrow.setPreserveRatio(true);
        previousUpgradeTierButton = new ScalingButton("", leftArrow);
        nextUpgradeTierButton = new ScalingButton("" ,  rightArrow);

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
        maggdaforestdefense.MaggdaForestDefense.bindPadding(buttonMenuScrollBox.paddingProperty(), 20);
        maggdaforestdefense.MaggdaForestDefense.bindBorder(buttonMenuScrollBox.borderProperty(), Color.DARKGREEN, BorderStrokeStyle.SOLID, 10, 3);
      
        updateButtonDisable();
        updateUpgradesShowing();
        updateButtonsLocked();
        
        switch(gameObjectType) {
            case T_SPRUCE:
                treeNameLabel.setText("Spruce");
                treeView.setImage(GameImage.TOWER_SPRUCE_1.getImage());
                
                //UPGRADES
 
                break;
            
            case T_MAPLE:
                treeNameLabel.setText("Maple");
                treeView.setImage(GameImage.TOWER_MAPLE_1.getImage());
                
                break;
                
            case T_LORBEER:
                treeNameLabel.setText("Lorbeer");
                treeView.setImage(GameImage.TOWER_LORBEER_1.getImage());
                
                break;
            case T_OAK:
                treeNameLabel.setText("Oak");
                treeView.setImage(GameImage.TOWER_OAK_1.getImage());
                break;
            default:
                throw new UnsupportedOperationException();
        }
          getChildren().addAll(treePane, buttonMenuScrollBox, selectedUpgradeBox);
          maggdaforestdefense.MaggdaForestDefense.bindToHeight(spacingProperty(), 20);
          setAlignment(Pos.CENTER);
          
          selectedUpgradeBox.setVisible(false);
          

          
          
    }
    
    public VBox getTreePane() {
        return treePane;
    }
    
    public void updateButtonsLocked() {
        for(int tier = 1; tier <= upgradeSet.getMaxTier(); tier++) {
            
            if(tier < currentTierToBuy) {
                for(BuyUpgradeButton button: boxes.get(tier-1).getButtons()) {
                    if(!button.isBought()) {
                        button.setBought(false);
                        button.setLocked(false);
                        button.setTierBought(true);
                    }
                }
            } else if(tier == currentTierToBuy) {
                for(BuyUpgradeButton button: boxes.get(tier-1).getButtons()) {
                    
                        button.setClickState(BuyUpgradeButton.CLICK_STATES.USUAL);
                        button.setLocked(false);
                    
                }
            } else {
                for(BuyUpgradeButton button: boxes.get(tier-1).getButtons()) {
                    
                        button.setLocked(true);
                    
                }
            }
        }
    }
    
    public String getTreeName() {
        return treeNameLabel.getText();
    }
    
    public void updateCoins(double coins) {
        boxes.forEach((UpgradeButtonTierBox box)->{
            box.updateCoins(coins);
        });
        selectedUpgradeBox.update();
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
        
        selectedUpgradeBox.setUpgrade(null, false);
    }
    
    public void previousTier() {
        currentTierShowing--;
        updateButtonDisable();
        updateUpgradesShowing();
        selectedUpgradeBox.setUpgrade(null, false);
    }
    
    public void buyUpgrade(int tier, int type) { 
        boxes.get(tier-1).getButtons().get(type-1).setBought(true);
        currentTierToBuy = tier+1;
        
        Upgrade upgrade = upgradeSet.getUpgrade(tier, type);
        boughtUpgradesBox.setBought(upgrade, tier);
        
        updateButtonsLocked();
        selectedUpgradeBox.update();
        
        ownerTower.setTier(tier);
    }
    
    public SelectedUpgradeBox getSelectedUpgradeBox() {
        return selectedUpgradeBox;
    }

    public void setTreeImage(Image image) {
        treeView.setImage(image);
    }

    public void clearLorbeerTrading() {
        lorbeerTradingUpgrades.forEach((Upgrade upgrade)->{
            boxes.get(upgradeSet.getTier(upgrade) - 1).getUpgradeButton(upgradeSet.getType(upgrade) - 1).setLorbeerTrade(false);
        });
        lorbeerTradingUpgrades.clear();
    }

    public void addLorbeerTrade(Upgrade upgrade) {
        boxes.get(upgradeSet.getTier(upgrade) - 1).getUpgradeButton(upgradeSet.getType(upgrade) - 1).setLorbeerTrade(true);
        lorbeerTradingUpgrades.add(upgrade);
    }
    
    public void removeLorbeerTrade(Upgrade upgrade) {
        boxes.get(upgradeSet.getTier(upgrade) - 1).getUpgradeButton(upgradeSet.getType(upgrade) - 1).setLorbeerTrade(false);
        if(lorbeerTradingUpgrades.contains(upgrade)) {
        lorbeerTradingUpgrades.remove(upgrade);
        }
    }
    
    public class UpgradeButtonTierBox extends FlowPane {
        public final static double GAP = 20;
        private ObservableList<BuyUpgradeButton> buttons;
        
        public ObservableList<BuyUpgradeButton> getButtons() {
            return buttons;
        }
        
        public UpgradeButtonTierBox(int tier, Upgrade[] upgrades) {
            buttons = FXCollections.observableArrayList();
            
            for(int i = 0; i < upgrades.length; i++) {
                Upgrade upgrade = upgrades[i];
                BuyUpgradeButton button = new BuyUpgradeButton(ownerTower, upgrade, true, tier, i+1);
                getChildren().add(button);
                buttons.add(button);
            }
            
            setAlignment(Pos.CENTER);
            maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(hgapProperty(), GAP);
            maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(vgapProperty(), GAP);
            
            
        }

        public void updateCoins(double coins) {
            buttons.forEach((BuyUpgradeButton button)->{
               button.updateCoins(coins); 
            });
        }
        
        public BuyUpgradeButton getUpgradeButton(int type) {
            return buttons.get(type);
        }
    }
    
    public class BoughtUpgradesBox extends HBox {
        public UpgradeDisplayPane[] panes;
        public BoughtUpgradesBox(int tierAmount) {
            panes = new UpgradeDisplayPane[tierAmount];
            for(int i = 0; i < tierAmount; i++) {
                UpgradeDisplayPane addPane = new UpgradeDisplayPane();
                panes[i] = addPane;
                getChildren().add(addPane);
            }
            maggdaforestdefense.MaggdaForestDefense.bindToWidth(spacingProperty(), 20);
            
            setManaged(true);
            setAlignment(Pos.CENTER);
        }
        
        public void setBought(Upgrade upgrade, int tier) {
            panes[tier-1].getImageView().setImage(upgrade.getIcon());
        }
        
        class UpgradeDisplayPane extends StackPane{
            public final static double SIZE = 80;
            private ImageView imageView;
            
            public UpgradeDisplayPane() {
                imageView = new ImageView();
                imageView.setPreserveRatio(true);
                maggdaforestdefense.MaggdaForestDefense.bindToHeight(imageView.fitWidthProperty(), SIZE);
                
                maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(prefHeightProperty(), SIZE);
                maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(prefWidthProperty(), SIZE);
                maggdaforestdefense.MaggdaForestDefense.bindBorder(borderProperty(), Color.DARKMAGENTA, BorderStrokeStyle.SOLID, 10, 5);
                
                getChildren().add(imageView);
                

            }
            public ImageView getImageView() {
                return imageView;
            }
        }
    }
    
    public class SelectedUpgradeBox extends VBox {
        public static final double ICON_SIZE = 100;
        private Button buyButton;
        private ImageView upgradeIcon;
        private Label descriptionLabel;
        private BuyUpgradeButton currentButton;
        
        public SelectedUpgradeBox() {
            buyButton = new ScalingButton("BUY");
            
            upgradeIcon = new ImageView();
            upgradeIcon.setPreserveRatio(true);
            maggdaforestdefense.MaggdaForestDefense.bindToHeight(upgradeIcon.fitWidthProperty(), ICON_SIZE);
     
            
            descriptionLabel = new Label();
            descriptionLabel.setWrapText(true);
            
            getChildren().addAll(upgradeIcon, descriptionLabel, buyButton);
            
            setManaged(true);
            setAlignment(Pos.CENTER);
            
            setBorder(new Border(new BorderStroke(Color.DARKGREEN, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
            setPadding(new Insets(20));
            

        }
        
        public void setUpgrade(BuyUpgradeButton button, boolean buyable) {
            if(button == null) {
                currentButton = null;
                setVisible(false);
                buyButton.setDisable(true);
                return;
            }
            currentButton = button;
            upgradeIcon.setImage(button.getUpgrade().getIcon());
            buyButton.setDisable(!buyable);
            buyButton.setOnAction((ActionEvent e)->{
                button.sendBuyOrder();
            });
            descriptionLabel.setText(button.getUpgrade().getDescription());
            descriptionLabel.setPrefWidth(boxes.get(0).getWidth());
            setVisible(true);
        }
        
        public void update() {
            if(currentButton == null) {
                buyButton.setDisable(true);
                setVisible(false);
            } else {
                setVisible(true);
                buyButton.setDisable(!currentButton.isBuyable());
            }
        }
    }
}
