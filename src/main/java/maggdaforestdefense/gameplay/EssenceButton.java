/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;


import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public class EssenceButton extends ImageView{
    public final static double WIDTH = 30;
    private ClientTower owner;
    public EssenceButton(ClientTower owner) {
        this.owner = owner;
        setImage(GameImage.ESSENCE_BUTTON.getImage());
        setPreserveRatio(true);
        setFitWidth(WIDTH);
        
        setOnMouseClicked((MouseEvent e)->{
            owner.requestEssence();
        });
        setOnMouseEntered((MouseEvent e)->{
            ScaleTransition trans = new ScaleTransition(Duration.seconds(0.1), this);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setToX(2);
            trans.setToY(2);
            trans.play();
        });
        setOnMouseExited((MouseEvent e)->{
            ScaleTransition trans = new ScaleTransition(Duration.seconds(0.1), this);
            trans.setInterpolator(Interpolator.EASE_BOTH);
            trans.setToX(1);
            trans.setToY(1);
            trans.play();
        });
        
        setVisible(false);
        
        setViewOrder(ViewOrder.POPUP);
    }
    
    public void show() {
        setScaleX(1);
        setScaleY(1);
        setLayoutX((owner.getXIndex() + 0.5) * MapCell.CELL_SIZE - 0.5 * getFitWidth());
        setLayoutY((owner.getYIndex()) * MapCell.CELL_SIZE);
        setVisible(true);
    }
    
    public void hide() {
        setVisible(false);
    }
}
