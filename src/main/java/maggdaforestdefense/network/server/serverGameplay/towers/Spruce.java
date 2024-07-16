/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import java.util.HashMap;
import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.SimplePermaStack;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.projectiles.SpruceShot;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.GameMaths;
import maggdaforestdefense.util.GsonConverter;
import maggdaforestdefense.util.Randomizer;

/**
 *
 * @author DavidPrivat
 */
public class Spruce extends Tower {

    public final static double DEFAULT_RANGE = 2;              //map cells
    public final static double DEFAULT_SHOOT_TIME = 0.75;        //per sec
    public final static int DEFAULT_PRIZE = 100;
    public final static double HEALTH = 50;
    public final static double DEFAULT_REGEN = 0;
    public final static boolean CAN_ATTACK_DIGGING = false, CAN_ATTACK_WALKING = true, CAN_ATTACK_FLYING = false;
    public final static double GROWING_TIME = 3;
    public final static RangeType RANGE_TYPE = RangeType.SQUARED;

    //Balancing stats;
    double xPos, yPos;
    double shootTimer = 0, shootTime = DEFAULT_SHOOT_TIME;

    // UPGRADE CONSTANTS
    public final static double FICHTEN_WUT_MULTIPLIER = 0.995;
    public final static double NADEL_STAERKUNG_MULT = 3;
    public final static double REGEN_ADD_FICHTENFREUNDSCHAFT = 0.1;
    public final static double UPGRADE_LIFE_STEAL = 0.05;
    public final static double RESEARCH_PROBABILITY = 0.1;

    // UPGRADE VARIABLES
    private double fichtenWutBuff = 1;

    private double monoculturalMultiplier = 1;  // *deltaT => >1
    private int spruceCounter = 0;

    private double rasendeFichteMultiplier = 1;
    private int rasendeFichteKillCounter = 0;

    private double aufruestungMultiplier = 1;
    private int aufruestungCounter = 0;

    private int fichtenFreundschaftCounter = 0;

    private int aufruhrDerFichtenCounter = 0;
    private double aufruhrDerFichtenDamagePerSpruce = 0;

    private HashMap<String, Integer> researchStacks;
    private boolean isResearchStacksUpdate = false;

    // multiplier
    public Spruce(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_SPRUCE, DEFAULT_PRIZE, UpgradeSet.SPRUCE_SET, HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(CAN_ATTACK_DIGGING, CAN_ATTACK_WALKING, CAN_ATTACK_FLYING), GROWING_TIME, RANGE_TYPE);
        xPos = x;
        yPos = y;

        // Fichtenfreundschaft
        onTowerChanges.add((o) -> {
            fichtenFreundschaftCounter = 0;
            serverGame.getGameObjects().forEach((String key, GameObject gameObject) -> {
                if (gameObject != this && gameObject instanceof Tower) {
                    Tower tower = (Tower) gameObject;
                    if (tower.getUpgrades().contains(Upgrade.SPRUCE_2_6)) {
                        if (GameMaths.isInSquareRange(xIndex, yIndex, tower.getXIndex(), tower.getYIndex(), tower.getRange())) {
                            fichtenFreundschaftCounter++;
                        }
                    }
                }
            });
            regenerationPerSecond = fichtenFreundschaftCounter * REGEN_ADD_FICHTENFREUNDSCHAFT;
        });

        researchStacks = new HashMap<String, Integer>();
        for (GameObjectType type : GameObject.getMobs()) {
            researchStacks.put(type.name(), 0);
        }

    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        return new CommandArgument[]{/*
            new CommandArgument("x", String.valueOf(xPos)),
            new CommandArgument("y", String.valueOf(yPos)),
            new CommandArgument("type", String.valueOf(GameObjectType.T_SPRUCE.ordinal())),
            new CommandArgument("growingTime", growingTime),
            new CommandArgument("id", String.valueOf(id))
         */};
    }

    @Override
    public void updateSpecific(double timeElapsed) {

        // Shooting
        shootTimer += timeElapsed * monoculturalMultiplier * rasendeFichteMultiplier * aufruestungMultiplier;

        if (shootTimer > shootTime * fichtenWutBuff) {
            Mob target = findTarget(range, serverGame.getMobs());
            if (target != null) {
                shootTimer = 0;
                shoot(target);
            }
        }
        
        if(isResearchStacksUpdate) {
            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.EDIT_FICHTENFORSCHUNG, new CommandArgument[]{
            new CommandArgument("id", this.id),
            new CommandArgument("hashmap", GsonConverter.toGsonString(researchStacks))}));
        }

    }

    private void shoot(Mob target) {
        serverGame.addProjectile(new SpruceShot(serverGame.getNextId(), serverGame, getCenterX(), getCenterY(), target, this, canAttackSet));
        performUpgradesOnShoot();
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);

        switch (upgrade) {
            case SPRUCE_1_2:        // Fichtenmonokultur
                onTowerChanges.add((o) -> {
                    spruceCounter = 0;
                    serverGame.getGameObjects().forEach((String key, GameObject gameObject) -> {
                        if (gameObject.getGameObjectType() == (GameObjectType.T_SPRUCE)) {
                            spruceCounter++;
                        }
                    });
                    monoculturalMultiplier = 1 + (0.2 * Math.sqrt(spruceCounter));
                });
                break;
            case SPRUCE_1_4:        // HOEHER WACHSEN
                canAttackSet.setCanAttackFlying(true);
                break;
            case SPRUCE_1_6:        // LIFESTEAL
                lifeSteal = UPGRADE_LIFE_STEAL;
                break;
            case SPRUCE_2_1:        // AUFRUESTUNG
                onShoot.add((o) -> {
                    aufruestungCounter = 0;
                    serverGame.getMobs().forEach((Mob mob) -> {
                        if (isInRange(mob, range)) {
                            aufruestungCounter++;
                        }
                    });
                    aufruestungMultiplier = Math.sqrt(0.3 * (int) aufruestungCounter + 1.0d);
                });
                break;
            case SPRUCE_2_2:        // Fichtenwut
                onShoot.add((o) -> {
                    fichtenWutBuff *= FICHTEN_WUT_MULTIPLIER;
                });
                onNewRound.add((o) -> {
                    fichtenWutBuff = 1;
                });
                break;
            case SPRUCE_2_4:        // WURZELHIEB
                canAttackSet.setCanAttackDigging(true);
                break;
            case SPRUCE_3_1:        //Serienmoerder
                onKill.add((o) -> {
                    shootTimer = shootTime * 0.99;
                });
                break;

            case SPRUCE_3_2:        //rasende fichte
                onKill.add((o) -> {
                    rasendeFichteKillCounter++;
                    rasendeFichteMultiplier = Math.sqrt(0.01 * (int) rasendeFichteKillCounter + 1.0d);
                    serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPDATE_SIMPLE_PERMA_STACKS, new CommandArgument[]{
                        new CommandArgument("id", id),
                        new CommandArgument("type", SimplePermaStack.SPRUCE_RASEND.ordinal()),
                        new CommandArgument("value", rasendeFichteMultiplier)
                    }));
                });
                break;
            case SPRUCE_3_4:        // fichtenforschung

                onKill.add((obj) -> {
                    Mob killed = (Mob) obj;
                    GameObjectType type = killed.getGameObjectType();

                    Randomizer.performWithProb(() -> {
                        if (researchStacks.containsKey(type.name())) {
                            researchStacks.replace(type.name(), researchStacks.get(type.name()) + 1);
                            Logger.logServer("(Spruce): Added researchstack for: " + type.name());
                             isResearchStacksUpdate = true;
                        } else {
                            Logger.logServer("(Spruce): Cant add research stack for: " + type.name());
                        }
                       
                    }, RESEARCH_PROBABILITY);

                });
                isResearchStacksUpdate = true;
                break;
            case SPRUCE_3_6:        // aufruhr der fickten
                onUpdate.add((o) -> {
                    if (healthPoints < maxHealth * 0.3) {
                        double diff = maxHealth * 0.3 - healthPoints;
                        aufruhrDerFichtenCounter = 0;
                        serverGame.getGameObjects().forEach((String key, GameObject g) -> {
                            if (g instanceof Spruce) {
                                Spruce spruce = (Spruce) g;
                                if (spruce.getHealthPoints() > 0.3 * spruce.getMaxHealthPoints()) {
                                    aufruhrDerFichtenCounter++;
                                }
                            }
                        });
                        aufruhrDerFichtenDamagePerSpruce = diff / ((double) aufruhrDerFichtenCounter);
                        serverGame.getGameObjects().forEach((String key, GameObject g) -> {
                            if (g instanceof Spruce) {
                                Spruce spruce = (Spruce) g;
                                healthPoints += spruce.sacrificeHealth(aufruhrDerFichtenDamagePerSpruce);
                            }
                        });
                    }
                });
                break;

        }
    }

    public double sacrificeHealth(double sugg) {
        double oldHealth = healthPoints;
        if (healthPoints - sugg < 0.3 * maxHealth) {
            healthPoints = 0.3 * maxHealth;

        } else {
            healthPoints -= sugg;
        }
        return oldHealth - healthPoints;
    }

    private HashMap<String, Integer> getResearchStacks() {
        return researchStacks;
    }

    public double getResearchMultiplierFor(String name) {
        double add = 0;
        if (researchStacks.containsKey(name)) {
            add = 0.5 * Math.sqrt(researchStacks.get(name));
        }
        return 1 + add;
    }

}
