/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import java.util.ArrayList;
import javafx.geometry.Point2D;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import static maggdaforestdefense.network.server.serverGameplay.mobs.Bug.HIT_BOX_RADIUS;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.MapDistanceSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.Path;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathCell;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathFinder;
import maggdaforestdefense.network.server.serverGameplay.projectiles.Projectile;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;
import maggdaforestdefense.util.GsonConverter;
import maggdaforestdefense.util.LineWorm;

/**
 *
 * @author David
 */
public class Caterpillar extends Mob {

    //BALANCE
    public final static double START_HP = 4000;
    public final static double DEFAULT_SPEED = 1;
    public final static int TOWER_VISION_RANGE = 0;
    public final static double DEFAULT_DAMAGE = 20;
    public final static double DEFAULT_ATTACK_TIME = 0.5;
    public final static MapDistanceSet DISTANCE_SET = new MapDistanceSet(50, 1, 20, 200);
    public final static double DEFAULT_ARMOR = 0;
    public final static double MAX_SEGMENT_LENGTH = 42;
    public final static double MIN_SEGMENT_LENGTH = 32;
    public final static double MAX_DIFF_PER_SEGMENT = MAX_SEGMENT_LENGTH - MIN_SEGMENT_LENGTH;  // 26
    public final static int SEGMENT_AMOUNT = 20;
    public final static double MAX_TOTAL_LENGTH = (1 + SEGMENT_AMOUNT) * MAX_SEGMENT_LENGTH;
    public final static double MIN_TOTAL_LENGTH = (1 + SEGMENT_AMOUNT) * MIN_SEGMENT_LENGTH;

    public final static double HITBOX_RADIUS = MapCell.CELL_SIZE / 2;

    private SegmentArray segmentArray;
    private double length;
    private double animationTimer = 0, movementMult = 1, omega = 4;
    private CaterpillarSegment[] damageEntities;
    private double attackTime = 2 * Math.PI / omega;
    private double attackTimer = 0;

    public Caterpillar(ServerGame game) {
        super(game, GameObjectType.M_BOSS_CATERPILLAR,
                START_HP, DEFAULT_SPEED, new HitBox.CircularHitBox(HITBOX_RADIUS, 0, 0), TOWER_VISION_RANGE, DEFAULT_DAMAGE, DEFAULT_ATTACK_TIME, DISTANCE_SET,
                DEFAULT_ARMOR, MovementType.WALK);
        findStartPos();
        length = MIN_TOTAL_LENGTH;

        damageEntities = new CaterpillarSegment[SEGMENT_AMOUNT];
        for (int i = 0; i < SEGMENT_AMOUNT; i++) {
            damageEntities[i] = new CaterpillarSegment(serverGame, this);
        }
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        if (!updateAlive()) {
            sentDeathToClient = true;
        }
        updateIndexPosition();
        updateAnimation(timeElapsed);
        updateMovement(timeElapsed * movementMult);
        updateWorm();
        updateDamageTarget(timeElapsed);
        

        return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
            new CommandArgument("id", String.valueOf(id)),
            new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("hp", String.valueOf(healthPoints)),
            new CommandArgument("movement", movementType.ordinal()),
            new CommandArgument("effects", effectSet.toString()),
            new CommandArgument("segments", GsonConverter.toGsonString(segmentArray.getSegmentArr())),
            new CommandArgument("length", length)});
    }
    

    @Override
    public double getSpeed() {
        return 100;
    }
    
        
    private void updateWorm() {
        segmentArray = new SegmentArray(SEGMENT_AMOUNT, length, path);
    }

    private void updateAnimation(double timeElapsed) {
        if (!targetReached) {
            animationTimer += timeElapsed;
        }
        double sin = Math.sin(omega * animationTimer);
        double cos = Math.cos(omega * animationTimer);
        double derLen = omega * 0.5d * (MAX_TOTAL_LENGTH - MIN_TOTAL_LENGTH) * cos;
        movementMult = Math.max(0.05 * derLen, derLen);
        length = MIN_TOTAL_LENGTH + (MAX_TOTAL_LENGTH - MIN_TOTAL_LENGTH) * 0.5 * (1.0d + sin);
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{
            new CommandArgument("x", xPos),
            new CommandArgument("y", yPos),
            new CommandArgument("hp", healthPoints),
            new CommandArgument("id", String.valueOf(id)),
            new CommandArgument("type", GameObjectType.M_BOSS_CATERPILLAR.ordinal()),
            new CommandArgument("length", length)};

    }

    public void checkSegmentCollision(Projectile projectile) {
        for (CaterpillarSegment currSeg : damageEntities) {
            if (HitBox.intersects(currSeg.getHitBox(), projectile.getHitBox())) {
                projectile.dealDamage(currSeg);
            }
        }
    }

    class SegmentArray {

        private int[] segments;

        public SegmentArray(int segAmount, double length, Path path) {
            double lenPerSeg = length / segAmount;
            segments = new int[(1 + segAmount) * 4];
            int[] currArr;
            for (int i = 0; i < segAmount + 1; i++) {

                double posBehind;
                if (i == 0) {
                    posBehind = 0;
                } else {
                    posBehind = (i + 0.13d) * lenPerSeg;
                }

                currArr = path.getPosDirBehind(posBehind);
                segments[4 * i] = currArr[0];
                segments[4 * i + 1] = currArr[1];
                segments[4 * i + 2] = currArr[2];
                segments[4 * i + 3] = currArr[3];
                if (i != 0) {
                    damageEntities[i - 1].updatePosition(getPosAt(i - 1), posBehind);
                }
            }

        }

        public Point2D getPosAt(int index) {
            return new Point2D(segments[index * 4] / 10.0d, segments[index * 4 + 1] / 10.0d);
        }

        public int[] getSegmentArr() {
            return segments;
        }
    }

    public CaterpillarSegment[] getSegments() {
        return damageEntities;
    }

}
