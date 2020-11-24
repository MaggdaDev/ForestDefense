/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientMaple;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class ClientMapleShot extends ClientProjectile {

    public final static int LEAF_AMOUNT = 12;

    private MapleLeaf[] leafs;

    public ClientMapleShot(int id, double x, double y) {
        super(id, null, GameObjectType.P_MAPLE_SHOT, x, y);
        setPreserveRatio(true);
        setFitHeight(0);
        
        leafs = new MapleLeaf[LEAF_AMOUNT];

        for (int i = 0; i < LEAF_AMOUNT; i++) {
            double currentRotate = 2 * Math.PI * ((double) i) / LEAF_AMOUNT;
            leafs[i] = new MapleLeaf(currentRotate, x, y);
            Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(leafs[i]);
        }
    }

    @Override
    public void update(NetworkCommand command) {
        double radius = command.getNumArgument("radius");
        
        MapleLeaf currentLeaf;
        for(int i = 0; i < LEAF_AMOUNT; i++) {
            currentLeaf = leafs[i];
            currentLeaf.update(radius);
        }
    }

    @Override
    public void onRemove() {
        for (int i = 0; i < LEAF_AMOUNT; i++) {
            if (Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().contains(leafs[i])) {
                Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().remove(leafs[i]);
            }
        }

        leafs = null;
    }

    public static class MapleLeaf extends ImageView {

        private Image image;
        private static final double SIZE = 90;
        
        private final double startCenterX, startCenterY, xMovementFact, yMovementFact;
        
        private final double rotate;

        public MapleLeaf(double rotate, double startX, double startY) {
            this.rotate = rotate;
            setRotate(rotate / (2 * Math.PI) * 360 + 90);
            image = GameImage.PROJECTILE_MAPLE_SHOT.getImage();
            setImage(image);
            setFitWidth(SIZE);
            setFitHeight(SIZE);
            startCenterX = startX;
            startCenterY = startY;
            setCenter(startCenterX, startCenterY);
            
            xMovementFact = Math.cos(rotate);
            yMovementFact = Math.sin(rotate);
            
            setViewOrder(ViewOrder.PROJECTILE);
            
            setVisible(true);

        }

        public void update(double newRadius) {
            setCenter(startCenterX + newRadius * xMovementFact, startCenterY + newRadius * yMovementFact);
          
        }
        
        private void setCenter(double x, double y) {
            setLayoutX(x - SIZE / 2.0d);
            setLayoutY(y - SIZE / 2.0d);
        }
    }
}
