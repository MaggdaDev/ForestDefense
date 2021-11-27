/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import javafx.geometry.Point2D;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import maggdaforestdefense.gameplay.Game;
import static maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientMob.ANGLE_OFFSET_RAD;
import maggdaforestdefense.gameplay.clientGameObjects.ViewOrder;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.mobs.Caterpillar;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.util.GameMaths;
import maggdaforestdefense.util.GsonConverter;
import maggdaforestdefense.util.LineWorm;

/**
 *
 * @author David
 */
public class ClientCaterpillar extends ClientMob {

    private double length;
    private Segment[] segments;
    public final static double WIDTH = 94;
    public final static double SEGMENT_HEIGHT = 60;
    public final static double HEAD_HEIGHT = 68;
    

    public ClientCaterpillar(int id, double x, double y, double hp, double len) {
        super(id, GameImage.MOB_BOSS_CATERPILLAR_1, GameObjectType.M_BOSS_CATERPILLAR, x, y, hp, Mob.MovementType.WALK, WIDTH);
        setPreserveRatio(true);
        setFitWidth(WIDTH);
        setFitHeight(HEAD_HEIGHT);
        this.length = len;
        int segmentAmount = Caterpillar.SEGMENT_AMOUNT;
        segments = new Segment[segmentAmount];
        for (int i = 0; i < segmentAmount - 1; i++) {
            segments[i] = new Segment(SegmentType.BODY, x, y);
        }
        segments[segments.length - 1] = new Segment(SegmentType.TAIL, x, y);

        Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().addAll(segments);
        setViewOrder(ViewOrder.MOB);
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        double newHealth = updateCommand.getNumArgument("hp");
        double xCenter = updateCommand.getNumArgument("x");
        double yCenter = updateCommand.getNumArgument("y");
        int[] segPosDirArr = (int[]) GsonConverter.fromString(updateCommand.getArgument("segments"), (new int[]{}).getClass());
        double newX = xCenter - (getFitWidth() / 2.0d);
        double newY = yCenter - (getFitHeight() / 2.0d);

        setRotate(GameMaths.getDegAngleToYAxis(segPosDirArr[2], segPosDirArr[3]));

        setNewPos(newX, newY);
        updateHealth(newHealth);
        updateShadow();
        handleEffects((EffectSet) GsonConverter.fromString(updateCommand.getArgument("effects"), EffectSet.class));

        //worm
        for (int i = 1; i < segments.length + 1; i++) {
            segments[i - 1].update(new LineWorm.DirectionPoint(new Point2D(segPosDirArr[4 * i] * 0.1, segPosDirArr[i * 4 + 1] * 0.1),
                    new Point2D(segPosDirArr[4 * i + 2], segPosDirArr[i * 4 + 3])));
        }

    }

    class Segment extends ImageView {

        private final SegmentType type;
        private DropShadow shadow;
        public Segment(SegmentType type, double x, double y) {
            setMouseTransparent(true);
            setFitWidth(WIDTH);
            setFitHeight(SEGMENT_HEIGHT);
            this.type = type;
            setPos(x, y);
            switch (type) {
                case BODY:
                    setImage(GameImage.MOB_BOSS_CATERPILLAR_2.getImage());
                    break;
                case TAIL:
                    setImage(GameImage.MOB_BOSS_CATERPILLAR_3.getImage());
                    break;
            }
            setViewOrder(ViewOrder.MOB);
            shadow = new DropShadow();
            shadow.setColor(Color.color(0, 0, 0, 0.3));
            shadow.setBlurType(BlurType.GAUSSIAN);
            setEffect(shadow);
        }

        public void update(LineWorm.DirectionPoint dirPoint) {
            setPos(dirPoint.getPosition().getX(), dirPoint.getPosition().getY());
            setRotate(GameMaths.getDegAngleToYAxis(dirPoint.getDirection().getX(), dirPoint.getDirection().getY()));
            
            shadow.setOffsetX(-Math.sin(ANGLE_OFFSET_RAD - (getRotate()/360)*2*Math.PI) * SHADOW_OFFSET_WALK_MULT * WIDTH);
            shadow.setOffsetY(Math.cos(ANGLE_OFFSET_RAD -(getRotate()/360)*2*Math.PI) * SHADOW_OFFSET_WALK_MULT * WIDTH);
            shadow.setInput(shadow.getInput());
        }

        private void setPos(double x, double y) {
            if (x < 0 || y < 0) {
                setVisible(false);
            } else {
                setVisible(true);
                setLayoutX(x - getFitWidth() / 2);
                setLayoutY(y - getFitHeight() / 2);
            }
        }

        
    }
    
    public static enum SegmentType {
            BODY, TAIL;
        }

    @Override
    public void onRemove() {
        super.onRemove();
        for (int i = 0; i < segments.length; i++) {
            if (Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().contains(segments[i])) {
                Game.getInstance().getGameScreen().getGamePlayGroup().getChildren().remove(segments[i]);
            }
        }
        segments = null;
    }

}
