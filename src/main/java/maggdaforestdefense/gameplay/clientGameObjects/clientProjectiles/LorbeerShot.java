/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.GameLoop;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.TimeUpdatable;

/**
 *
 * @author DavidPrivat
 */
public class LorbeerShot extends Group implements TimeUpdatable {

    private final static int SHOT_AMOUNT = 8;
    private final double ROT_PER_SEC = 1;
    private double currRot = 0;
    private LorbeerLeaf[] leafs;

    public LorbeerShot(double x, double y, double range) {
        leafs = new LorbeerLeaf[SHOT_AMOUNT];
        for (int i = 0; i < SHOT_AMOUNT; i++) {
            leafs[i] = new LorbeerLeaf(range, (((double) i) / SHOT_AMOUNT) * 360);
        }
        getChildren().addAll(leafs);
        GameLoop.addTimeUpdatable(this);
        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().add(this);
        setLayoutX(x);
        setLayoutY(y);

    }

    @Override
    public void update(double timeElapsed) {
        currRot += ROT_PER_SEC * timeElapsed * 2 * Math.PI;
        for (int i = 0; i < SHOT_AMOUNT; i++) {
            double widthFact = 1.0d;
            if(currRot > 0.5 * Math.PI) {
                widthFact = 1 - (currRot - 0.5 * Math.PI) / (0.25*Math.PI);
            } else if (currRot < 0.25 * Math.PI) {
                widthFact = (currRot) / (0.25*Math.PI);
            }
            leafs[i].update(currRot + (((double) i) / SHOT_AMOUNT) * 2 * Math.PI, widthFact);
        }
        
        if(currRot > 0.75*Math.PI) {
            destroy();
        }
    }
    
    public void destroy() {
        Game.getInstance().getGameScreen().safeRemoveGameplayNode(this);
        GameLoop.removeTimeUpdatable(this);
    }

    public static class LorbeerLeaf extends ImageView {

        public final static double FIT_HEIGHT = 30;

        private Rotate rotate;
        private final double range;

        public LorbeerLeaf(double range, double rot) {
            setImage(GameImage.PROJECTILE_LORBEER_SHOT.getImage());
            setPreserveRatio(false);
            setFitHeight(FIT_HEIGHT);
            setFitWidth(range);
            rotate = new Rotate(rot, 0,0 );
            getTransforms().add(rotate);
            this.range = range;
            
            setLayoutX(0);
            setLayoutY(0 - 0.5*FIT_HEIGHT);
        }

        public void update(double angle, double widthFact) {

            rotate.setAngle((angle / (2 * Math.PI)) * 360.0d);
            
            double tempAng = Math.abs(angle % Math.PI);
            if(tempAng < 0.25*Math.PI || tempAng > 0.75 * Math.PI) {
                setFitWidth(widthFact * Math.sqrt(Math.pow(range, 2.0d) + Math.pow(range * Math.tan(angle), 2.0d)));
            } else {
                setFitWidth(widthFact *Math.sqrt(Math.pow(range, 2.0d) + Math.pow(range * Math.tan(0.5*Math.PI - angle), 2.0d)));
            }

            
        }
    }
}
