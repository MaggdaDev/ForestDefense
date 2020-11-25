/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.projectiles.MapleShot;
import maggdaforestdefense.network.server.serverGameplay.projectiles.SpruceShot;
import static maggdaforestdefense.network.server.serverGameplay.towers.Spruce.RANGE_TYPE;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public class Maple extends Tower {

    //BALANCING
    public final static int DEFAULT_PRIZE = 1;
    public final static double DEFAULT_HEALTH = 100;
    public final static double DEFAULT_REGEN = 1;
    public final static double DEFAULT_RANGE = 1.5d;
    public final static double GROWING_TIME = 3;
    public final static boolean CAN_ATTACK_DIGGING = false, CAN_ATTACK_WALKING = true, CAN_ATTACK_FLYING = false;
    public final static RangeType RANGE_TYPE = RangeType.CIRCLE;

    private double shootTime = 2, shootTimer = 0;

    // UPGRADE CONSTANTS
    public final static double BUND_DER_AHORNE_RANGE = 2, BUND_DER_AHORNE_ADD = 1;
    public final static double ZERLEGEND_SHOOTTIMER_PRCT_OF_MISSING = 0.1;

    // UPGRADe
    private boolean isBundDerAhorn = false;
    private boolean isZerlegend = false;

    public Maple(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_MAPLE, DEFAULT_PRIZE, UpgradeSet.MAPLE_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(CAN_ATTACK_DIGGING, CAN_ATTACK_WALKING, CAN_ATTACK_FLYING), GROWING_TIME);

        onTowerChanges.add((o) -> {
            isBundDerAhorn = false;
            serverGame.getGameObjects().forEach((String key, GameObject gameObject) -> {
                if (gameObject != this && gameObject instanceof Tower) {
                    Tower tower = (Tower) gameObject;
                    if (tower.getUpgrades().contains(Upgrade.MAPLE_1_2)) {
                        if (GameMaths.isInSquareRange(xIndex, yIndex, tower.getXIndex(), tower.getYIndex(), BUND_DER_AHORNE_RANGE)) {
                            isBundDerAhorn = true;
                        }
                    }
                }
            });
            if (isBundDerAhorn) {
                range = DEFAULT_RANGE + 1.0d;
            }
        });

    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);

        switch (upgrade) {
            case MAPLE_3_3:
                isZerlegend = true;
                break;
        }
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public NetworkCommand update(double timeElapsed) {
        if (!checkAlive()) {
            return null;
        }

        if (!isMature) {
            GameImage currImage = updateGrowing(timeElapsed);
            isMature = growingAnimation.isFinished();
            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{
                new CommandArgument("id", id),
                new CommandArgument("hp", healthPoints),
                new CommandArgument("image", currImage.ordinal()),
                new CommandArgument("timeLeft", growingAnimation.getTimeLeft()),
                new CommandArgument("range", range)
            });
        } else {

            // Regen
            updateRegen(timeElapsed);

            // Shooting
            shootTimer += timeElapsed;

            if (shootTimer > shootTime) {
                int mobsInRange = howManyMobsInRange(range, canAttackSet);
                if (mobsInRange > 0) {
                    Logger.logServer(mobsInRange + "mobs in range detected");
                    shootTimer = 0;
                    shoot(mobsInRange);
                }
            }

            // Health
            // Upgrades
            performUpgradesOnUpdate();

            return new NetworkCommand(NetworkCommand.CommandType.UPDATE_GAME_OBJECT, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("hp", healthPoints), new CommandArgument("range", range)});
        }
    }

    public void notifyEnemyHit() {
        if(isZerlegend) {
            shootTimer += (shootTime - shootTimer) * ZERLEGEND_SHOOTTIMER_PRCT_OF_MISSING;
        }
    }

    private void shoot(int mobsInRange) {
        serverGame.addProjectile(new MapleShot(serverGame.getNextId(), getCenterX(), getCenterY(), this, canAttackSet, serverGame, mobsInRange, range));
        performUpgradesOnShoot();
    }

    @Override
    public RangeType getRangeType() {
        return RANGE_TYPE;
    }

}
