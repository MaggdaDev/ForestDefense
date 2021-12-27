/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class InformationBubble extends Label {

    public final static double DURATION = 0.5;
    private ParallelTransition parTrans;
    private PathTransition pathTrans;
    private FadeTransition fadeTrans;
    private CubicCurve curve;
    private double bigFact = 1;

    public InformationBubble(String text, InformationType type, double startX, double startY) {
        setLayoutX(startX);
        setLayoutY(startY);
        if(type != InformationType.FICHTEN_FORSCHUNG) {
            setText(text);
        }
        switch(type) {
            case LORBEER: case GOLD:
                setFont(new Font(22));
                bigFact = 3;
                break;
            case FICHTEN_FORSCHUNG:
                ImageView typeView = new ImageView(GameObjectType.valueOf(text).getImage());
                typeView.setPreserveRatio(true);
                typeView.setFitWidth(15);
                typeView.setEffect(new DropShadow(5, Color.RED));
                setGraphic(typeView);
                break;
            default:
                setFont(new Font(11));
                break;
        }
        
        setEffect(new Glow());
        setTextFill(type.getColor());

        fadeTrans = new FadeTransition(Duration.seconds(DURATION * bigFact), this);
        fadeTrans.setFromValue(1);
        fadeTrans.setToValue(0);

        int xSign = Randomizer.randSign();
        double randXAdd1 = xSign * Randomizer.randDouble(20, 30);
        double randXAdd2 = xSign * Randomizer.randDouble(30, 40);
        double randYAdd1 = -Randomizer.randDouble(5, 15);
        double randYAdd2 = -Randomizer.randDouble(15, 20);

        curve = new CubicCurve(0, 0, randXAdd1, randYAdd1, randXAdd1 + randXAdd2, randYAdd1 + randYAdd2, 2 * randXAdd1 + randXAdd2, 0);

        pathTrans = new PathTransition(Duration.seconds(DURATION*bigFact), curve, this);
        pathTrans.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double arg0) {
                return 1.5 - 0.75 / (arg0 + 0.5);
            }
        });
        
        parTrans = new ParallelTransition(pathTrans, fadeTrans);
        
        parTrans.play();

    }

    public static enum InformationType {
        GOLD(Color.GOLD),
        TREE_HP(Color.RED),
        LORBEER(Color.BLACK),
        MOB_HP(Color.BLUE),
        FICHTEN_FORSCHUNG(Color.GREEN);

        private final Paint color;

        InformationType(Paint p) {
            color = p;
        }

        public Paint getColor() {
            return color;
        }
    }

}
