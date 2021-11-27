/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import javafx.geometry.Point2D;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.HitBox;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.towers.Tower;

/**
 *
 * @author David
 */
public class CaterpillarSegment extends Mob {

    public final static double HITBOX_RADIUS = MapCell.CELL_SIZE / 2;
    public final static double DAMAGE_RELAY_FACT = 0.5;
    public final static double DAMAGE = 2;
    private Caterpillar caterpillar;
    private double posBehind;
    private Damage halfDamage;

    public CaterpillarSegment(ServerGame serverGame, Caterpillar caterpillar) {
        super(serverGame, GameObjectType.M_BOSS_CATERPILLAR_SEGMENT, 0, 0, new HitBox.CircularHitBox(HITBOX_RADIUS, 0, 0), 0, DAMAGE, 0, null, 0, MovementType.WALK);
        this.caterpillar = caterpillar;
        halfDamage = new Damage(this);
        halfDamage.addDamage(new Damage.NormalDamage(damage / 2));
    }

    public void updatePosition(Point2D pos, double posBehind) {
        xPos = pos.getX();
        yPos = pos.getY();
        int oldXIdx = currentXIndex;
        int oldYIdx = currentYIndex;
        currentXIndex = (int) (xPos / MapCell.CELL_SIZE);
        currentYIndex = (int) (yPos / MapCell.CELL_SIZE);
        if (oldXIdx != currentXIndex || oldYIdx != currentYIndex) {
            attack(currentXIndex - oldXIdx, currentYIndex - oldYIdx);
        }

        hitBox.updatePos(xPos, yPos);
        this.posBehind = posBehind;
    }

    private void attack(int dX, int dY) {
        Tower currTower;
        for (GameObject obj : serverGame.getGameObjects().values()) {
            if (obj instanceof Tower) {
                currTower = (Tower) obj;
                if (currTower.getXIndex() == currentXIndex && currTower.getYIndex() == currentYIndex) {
                    currTower.damage(damageObject);
                } else if (currTower.getXIndex() == currentXIndex + dY && currTower.getYIndex() == currentYIndex
                        || currTower.getXIndex() == currentXIndex - dY && currTower.getYIndex() == currentYIndex
                        || currTower.getXIndex() == currentXIndex && currTower.getYIndex() == currentYIndex + dX
                        || currTower.getXIndex() == currentXIndex && currTower.getYIndex() == currentYIndex - dX) {
                    currTower.damage(halfDamage);
                }
            }
        }
    }

    @Override
    public double getDistanceToBase() {
        return caterpillar.getDistanceToBase() + posBehind;
    }

    @Override
    protected double applyDamage(double damageVal, Damage damage
    ) {
        return caterpillar.applyDamage(damageVal * DAMAGE_RELAY_FACT, damage);
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NetworkCommand update(double timeElapsed
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
