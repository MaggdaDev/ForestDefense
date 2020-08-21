/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientTower;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class EssenceAnimation extends ImageView {

    public static double HEIGHT = 40, SPEED = 200;
    public static double pixelBetweenFrames = 20;
    private double lastFrameUpdateX = 0;
    private double lastFrameUpdateY = 0;
    private int animationState = 0;

    public EssenceAnimation(int startXIndex, int startYIndex, ClientTower tower) {
        setImage(GameImage.ESSENCE_ANIMATION_1.getImage());
        setPreserveRatio(true);
        double ratio = getImage().getWidth() / getImage().getHeight();
        setFitHeight(HEIGHT);
        setFitWidth(ratio * HEIGHT);

        translateXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                updateAnimation();
            }

        });
        translateYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                updateAnimation();
            }

        });

        double thisWidth = getFitWidth();
        double thisHeight = getFitHeight();

        double startX = calculatePos(startXIndex, thisWidth);
        double startY = calculatePos(startYIndex, thisHeight);
        double endX = calculatePos(tower.getXIndex(), thisWidth);
        double endY = calculatePos(tower.getYIndex(), thisHeight);

        double dX = endX - startX;
        double dY = endY - startY;
        double abs = Math.sqrt(dX * dX + dY * dY);

        Path path = new Path();
        QuadCurveTo quadTo = new QuadCurveTo();
        quadTo.setControlX(startX + 0.5 * dX);
        quadTo.setControlY(startY + 0.5 * dY + 0.3 * dX * dX / abs);
        quadTo.setX(endX);
        quadTo.setY(endY);
        path.getElements().addAll(new MoveTo(startX, startY), quadTo);

        PathTransition transition = new PathTransition();
        transition.setPath(path);
        transition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        transition.setNode(this);
        transition.setDuration(Duration.seconds(abs / SPEED));
        
        ScaleTransition scaleTrans = new ScaleTransition();
        scaleTrans.setNode(this);
        scaleTrans.setAutoReverse(true);
        scaleTrans.setFromX(0);
        scaleTrans.setFromY(0);
        scaleTrans.setToX(1);
        scaleTrans.setToY(1);
        scaleTrans.setCycleCount(2);
        scaleTrans.setDuration(Duration.seconds(0.5 * abs / SPEED));

        ParallelTransition parTrans = new ParallelTransition(transition, scaleTrans);
        parTrans.setNode(this);

        parTrans.setOnFinished((ActionEvent e) -> {
            Game.getInstance().essenceAnimationFinished(this, tower);
        });

        parTrans.play();

    }

    private void updateAnimation() {
        if (Math.sqrt(Math.pow(getTranslateX() - lastFrameUpdateX, 2.0d) + Math.pow(getTranslateY() - lastFrameUpdateY, 2.0d)) > pixelBetweenFrames) {
            lastFrameUpdateX = getTranslateX();
            lastFrameUpdateY = getTranslateY();
            animationState++;
            animationState %= 4;

            switch (animationState) {
                case 0:
                    setImage(GameImage.ESSENCE_ANIMATION_1.getImage());
                    break;
                case 1:
                    setImage(GameImage.ESSENCE_ANIMATION_2.getImage());
                    break;
                case 2:
                    setImage(GameImage.ESSENCE_ANIMATION_3.getImage());
                    break;
                case 3:
                    setImage(GameImage.ESSENCE_ANIMATION_4.getImage());
                    break;
            }
        }
    }

    private static double calculatePos(int index, double thisSize) {
        return (index + 0.5) * MapCell.CELL_SIZE;
    }
}
