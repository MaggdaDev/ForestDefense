/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.ingamemenus;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import maggdaforestdefense.MaggdaForestDefense;
import maggdaforestdefense.gameplay.Game;
import static maggdaforestdefense.gameplay.ingamemenus.UpgradeMenu.ARROW_SIZE;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.client.NetworkManager;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Exceptions;
import maggdaforestdefense.util.NodeSizer;

/**
 *
 * @author DavidPrivat
 */
public class PlantTowerButton extends Button {
    public final static double IMAGE_SIZE = 100;

    private ImageView imageView;
    private int xIndex, yIndex;
    private GameObjectType gameObjectType;
    private int coinsPrize;
    private PrizeLabel prizeLabel;

    private BuyTreeBox buyTreeBox;
    private PlantMenu plantMenu;

    public PlantTowerButton(GameObjectType type, int x, int y, int prize, PlantMenu plantMenu) throws Exceptions.GameObjectNotCompatibleException {
        gameObjectType = type;
        this.plantMenu = plantMenu;
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(imageView.fitHeightProperty(), IMAGE_SIZE);
        
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(minWidthProperty(), IMAGE_SIZE);
        maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(minHeightProperty(), IMAGE_SIZE);

       

        xIndex = x;
        yIndex = y;
        coinsPrize = prize;

        prizeLabel = new PrizeLabel(coinsPrize);

        setOnAction((ActionEvent e) -> {
            plantMenu.setBuyTreeBox(buyTreeBox);
        });

        

        VBox graphic = new VBox(imageView, prizeLabel);
        graphic.setAlignment(Pos.CENTER);
        graphic.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        setGraphic(graphic);
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));

        getStyleClass().clear();
        getStyleClass().add("addTowerButton");
        
        imageView.setImage(type.getImage());
        switch (type) {
            case T_SPRUCE:
                
                buyTreeBox = new BuyTreeBox(Game.language.SPRUCE_NAME, Game.language.SPRUCE_DESCRIPTION, GameImage.TOWER_SPRUCE_1, prize);
                break;
            case T_MAPLE:
                
                buyTreeBox = new BuyTreeBox(Game.language.MAPLE_NAME, Game.language.MAPLE_DESCRIPTION, GameImage.TOWER_MAPLE_1, prize);
                break;
            case T_LORBEER:

                buyTreeBox = new BuyTreeBox(Game.language.LORBEER_NAME, Game.language.LORBEER_DESCRIPTION, GameImage.TOWER_LORBEER_1, prize);
                break;
            case T_OAK:
                buyTreeBox = new BuyTreeBox(Game.language.OAK_NAME, Game.language.OAK_DESCRIPTION, GameImage.TOWER_OAK_1, prize);
                break;
            default:
                throw new Exceptions.GameObjectNotCompatibleException();

        }
        


    }

    private void plantTower() {
        NetworkManager.getInstance().sendCommand(new NetworkCommand(NetworkCommand.CommandType.ADD_TOWER, new CommandArgument[]{
            new CommandArgument("x", String.valueOf(xIndex * MapCell.CELL_SIZE)),
            new CommandArgument("y", String.valueOf(yIndex * MapCell.CELL_SIZE)),
            new CommandArgument("type", String.valueOf(gameObjectType.ordinal()))}));
    }

    void updateCoins(double coins) {
        if (coins < coinsPrize) {
            setOpacity(0.5);
        } else {
            setOpacity(1);
        }
    }

    public class BuyTreeBox extends VBox{
        public static final double TREE_VIEW_HEIGHT = 250;

        private Button buyButton, backButton;
        private ImageView treeView;
        private Label treeName, treeDescription;
        private PrizeLabel prizeLabel;

        public BuyTreeBox(String name, String description, GameImage image, int prize) {
            buyButton = new ScalingButton("BUY", new PrizeLabel(prize));
            buyButton.setOnAction((ActionEvent e)->{
                plantTower();
            });
            
            backButton = new ScalingButton("BACK");
            backButton.setOnAction((ActionEvent e)->{
                plantMenu.setBuyTreeBox(null);
            });



            treeView = new ImageView(image.getImage());
            treeView.setPreserveRatio(true);
            maggdaforestdefense.MaggdaForestDefense.bindToHeight(treeView.fitHeightProperty(), TREE_VIEW_HEIGHT);
            MaggdaForestDefense.bindToWidth(minWidthProperty(), 250);
            

            treeName = new Label(name);

            treeDescription = new Label(description);
            treeDescription.setWrapText(true);
            treeDescription.maxWidthProperty().bind(plantMenu.widthProperty().multiply(0.6));
            
            
            HBox buttonBox = new HBox(backButton, buyButton);
            maggdaforestdefense.MaggdaForestDefense.bindToWidth(buttonBox.spacingProperty(), 10);
            maggdaforestdefense.MaggdaForestDefense.bindToHeight(spacingProperty(), 20);
            buttonBox.setFillHeight(true);
            buttonBox.setAlignment(Pos.CENTER);

            getChildren().addAll(treeName, treeView, treeDescription, buttonBox);
            setAlignment(Pos.CENTER);
            
            

        }

        public void updateCoins(double coins) {
            if (coins < coinsPrize) {
                buyButton.setDisable(true);
                buyButton.setOpacity(0.5);
            } else {
                buyButton.setDisable(false);
                buyButton.setOpacity(1);
            }
        }

    }

}
