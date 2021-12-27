/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientTowers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.HealthBar;
import maggdaforestdefense.gameplay.InformationBubble;
import static maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientMaple.RANGE_TYPE;
import maggdaforestdefense.gameplay.ingamemenus.ContentBox;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.towers.Spruce;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.GsonConverter;

/**
 *
 * @author DavidPrivat
 */
public class ClientSpruce extends ClientTower {
    public final static Tower.RangeType RANGE_TYPE = Spruce.RANGE_TYPE;
    private HashMap<String, Double> fichtenForschung;
    private FichtenForschungBox fichtenForschungBox;
    public ClientSpruce(int id, int xIndex, int yIndex, double growingTime) {
        super(id, GameImage.TOWER_SPRUCE_1, GameObjectType.T_SPRUCE, UpgradeSet.SPRUCE_SET, xIndex, yIndex, Spruce.DEFAULT_RANGE, Spruce.HEALTH, growingTime, RANGE_TYPE);
        fichtenForschungBox = new FichtenForschungBox();
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        healthPoints = updateCommand.getNumArgument("hp");
        updateHealth(healthPoints);
        
        if(isMature) {
            EffectSet e = EffectSet.fromString(updateCommand.getArgument("effects"));
            handleEffects(e);
        }
        
        if(updateCommand.containsArgument("image")) {
            setImage(GameImage.values()[(int)updateCommand.getNumArgument("image")].getImage());
            updateGrowing(updateCommand.getNumArgument("timeLeft"));
        } else {
            if(!isMature) {
                isMature = true;
            }
        }
        
        
    }
    
    @Override
    public void setTier(int tier) {
        tier ++;
        Image image;
        switch(tier) {
            case 4:
                image = GameImage.TOWER_SPRUCE_4.getImage();
                break;
            case 3:
                image = GameImage.TOWER_SPRUCE_3.getImage();
                break;
            case 2:
                image = GameImage.TOWER_SPRUCE_2.getImage();
                break;
            case 1: default:
                image = GameImage.TOWER_SPRUCE_1.getImage();
                break;
        }
        
        setImage(image);
        upgradeMenu.setTreeImage(image);
    }
    
    @Override
    public void buyUpgrade(int tier, int type) {
        if (UpgradeSet.SPRUCE_SET.getUpgrade(tier, type) == Upgrade.SPRUCE_3_4) {
            upgradeMenu.getTreePane().getChildren().add(fichtenForschungBox);
        }
        
        upgradeMenu.buyUpgrade(tier, type);
    }

    public void editFichtenforschung(NetworkCommand command) {
        HashMap<String,Double> newFichtenForschung = (HashMap<String,Double>)GsonConverter.fromString(command.getArgument("hashmap"), HashMap.class);
        String[] keys = newFichtenForschung.keySet().toArray(new String[newFichtenForschung.size()]);
        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            Double num = newFichtenForschung.get(key);
            fichtenForschungBox.setValue(i, num.intValue());
            if((fichtenForschung == null && num > 0) || (fichtenForschung != null && num > fichtenForschung.get(key))) {
                    Logger.logClient("Fichtenforschungsstack for: " + key);
                    Game.addGamePlayNode(new InformationBubble(key, InformationBubble.InformationType.FICHTEN_FORSCHUNG, xPos+ MapCell.CELL_SIZE/2, yPos));           
            } 
        }
        fichtenForschung = newFichtenForschung;
    }
    
    private class FichtenForschungBox extends ContentBox {
        private FichtenForschungEntry[] entries;
        private Label label;
        public FichtenForschungBox() {
            entries = new FichtenForschungEntry[GameObject.getMobs().length];
            for(int i = 0; i < GameObject.getMobs().length; i++) {
                entries[i] = new FichtenForschungEntry(GameObject.getMobs()[i]);
            }
            label = new Label("Fichtenforschung:");
            
            getChildren().add(label);
            getChildren().addAll(entries);
        }
        public void setValue(int index, int value) {
            entries[index].setValue(value);
        }
        private class FichtenForschungEntry extends HBox {
            public final static double ICON_WIDTH = 23;
            private Label mobTypeLabel;
            private Label stacksLabel;
            private ImageView mobTypeView;
        
            public FichtenForschungEntry(GameObjectType mobType) {
                mobTypeView = new ImageView(mobType.getImage());
                mobTypeView.setPreserveRatio(true);
                maggdaforestdefense.MaggdaForestDefense.bindToSizeFact(mobTypeView.fitWidthProperty(), ICON_WIDTH);
                mobTypeLabel = new Label(" : ", mobTypeView);
                stacksLabel = new Label("0");
                getChildren().addAll(mobTypeLabel, stacksLabel);
                setAlignment(Pos.CENTER_LEFT);
            }
            public void setValue(int val) {
                stacksLabel.setText(String.valueOf(val));
            }
        }

       
    }
        
        
}

    
 
    
    
    

