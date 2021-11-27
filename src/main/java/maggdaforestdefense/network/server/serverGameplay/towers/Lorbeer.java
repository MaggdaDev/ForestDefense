/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.Damage;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Blattlaus;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.Randomizer;
import maggdaforestdefense.util.RandomEvent;

/**
 *
 * @author DavidPrivat
 */
public class Lorbeer extends Tower {

    public static final int DEFAULT_PRIZE = 350;
    public final static double DEFAULT_HEALTH = 20, DEFAULT_REGEN = 0;
    public final static double DEFAULT_RANGE = 2;
    public final static double DEFAULT_GROWING_TIME = 30;
    public final static RangeType RANGE_TYPE = RangeType.SQUARED;
    public final static double DEFAULT_DAMAGE = 10;
    public final static int DEFAULT_MAX_LORBEERS = 10;
    public final static int DEFAULT_GOLD_PER_LORBEER = 25;

    private Damage damageObject;
    private Damage.NormalDamage usualDamage;

    private double attackCooldown = 8.0d, attackTimer = attackCooldown;
    private boolean canAttack = true;

    private int lorbeerAmount = 0, maxLorbeerAmount = DEFAULT_MAX_LORBEERS;
    private int goldPerLorbeer = DEFAULT_GOLD_PER_LORBEER;

    private Vector<CommandArgument> updateCommandArgs;

    //UPGRADES
    private double goldPerLorbeerUpgradeMult = 1;

    private Damage.NormalDamage executiveDamage;

    private boolean isExecutive = false;
    public final static double EXECUTIVE_PERCENTMISSINGHEALTH = 0.2d;

    private boolean isErnteRausch = false;
    public final static int RAUSCH_KILL_AMOUNT = 2;
    private int ernteRauschKillCount = 0;

    private boolean isMassenproduktion = false;
    public final static double MASSENPRODUKTION_MULT = 1.1;

    private boolean isWiederverwertung = false;
    public final static double WIEDERVWERTUNG_ADD = 5;

    private boolean isAutomatic = false;

    private boolean isPrestige = false;
    public final static int PRESTIGE_ADD = 10;

    private boolean isKopfgeld = false;
    private HeadHuntGenerator headHuntGenerator;
    private HeadHuntGenerator.HeadHunt headHunt;

    private boolean isTauschhandel = false;
    private Vector<Upgrade> tauschhandelUpgrades;
    private ConcurrentLinkedQueue<Upgrade> tradeUpgradesAdd, tradeUpgradesRemove;
    private double tauschhandelPow = 0.5d;

    public Lorbeer(ServerGame game, double xPos, double yPos) {
        super(game, xPos, yPos, GameObjectType.T_LORBEER, DEFAULT_PRIZE, UpgradeSet.LORBEER_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(true, true, true), DEFAULT_GROWING_TIME, RANGE_TYPE);
        damageObject = new Damage(this);
        usualDamage = new Damage.NormalDamage(DEFAULT_DAMAGE);
        executiveDamage = new Damage.NormalDamage(0.0d);

        damageObject.addAllDamage(new Damage.DamageSubclass[]{usualDamage, executiveDamage});
        //damageObject.addAllDamageMultiplier(new Damage.DamageMultiplier[]{});

        onKill.add((o)
                -> {
            if (lorbeerAmount < maxLorbeerAmount) {
                lorbeerAmount++;
            }
        });

        headHuntGenerator = new HeadHuntGenerator(serverGame, String.valueOf(id));
        tauschhandelUpgrades = new Vector<>();
        tradeUpgradesAdd = new ConcurrentLinkedQueue<>();
        tradeUpgradesRemove = new ConcurrentLinkedQueue<>();
        updateCommandArgs = new Vector<CommandArgument>();
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);

        switch (upgrade) {
            case LORBEER_1_1:   // WEITREICHEND
                range += 1;
                break;
            case LORBEER_1_2:   // ERTRAGREICH
                goldPerLorbeerUpgradeMult = (1.5 * goldPerLorbeer);
                goldPerLorbeer = (int) (DEFAULT_GOLD_PER_LORBEER * goldPerLorbeerUpgradeMult);
                tauschhandelPow -= 0.1;
                break;
            case LORBEER_1_3:   // EFFIZIENTNE ERNTE
                attackCooldown /= 1.5;
                break;
            case LORBEER_1_4:   // SPEICHER
                maxLorbeerAmount *= 1.5;
                tauschhandelPow -= 0.2;
                break;

            case LORBEER_2_1:   // EXECUTIVE
                isExecutive = true;
                break;
            case LORBEER_2_2:   // ERNTERAUSCH
                isErnteRausch = true;
                break;
            case LORBEER_2_3:   // MASSENPRODUKTION
                isMassenproduktion = true;
                tauschhandelPow -= 0.2;
                break;
            case LORBEER_2_4:   // WIEDERVERWERTUNG
                isWiederverwertung = true;
                break;
            case LORBEER_3_1:   // AUTOMATISCHE
                isAutomatic = true;
                break;
            case LORBEER_3_2:   // PRESTIGE
                isPrestige = true;
                break;
            case LORBEER_3_3:   // Bounty
                isKopfgeld = true;
                generateHeadHunt();
                break;
            case LORBEER_3_4:   // Tauschhandel
                isTauschhandel = true;
                break;

        }
    }

    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateSpecific(double timeElapsed) {

        if (attackTimer <= attackCooldown) {
            attackTimer += timeElapsed;

        } else {
            canAttack = true;
        }

        // Health
        // Upgrades
        if (isMassenproduktion) {
            goldPerLorbeer = (int) (DEFAULT_GOLD_PER_LORBEER * goldPerLorbeerUpgradeMult * (1 + lorbeerAmount / DEFAULT_MAX_LORBEERS));
        }

        if (isAutomatic) {
            if (canAttack) {
                getMobsInRange(range).forEach((Mob mob) -> {
                    if (mob.wouldDie(damageObject)) {
                        attack();
                    }
                });
            }
        }

        addUpdateArg(new CommandArgument("range", range));
        addUpdateArg(new CommandArgument("attackCooldown", attackCooldown - attackTimer));

        if (isTauschhandel) {
            updateTauschhandel();
        }

        if (isKopfgeld) {
            if (headHunt.isUpdate()) {
                sendKopfgeldUpdate();
            }
        } else {
            addUpdateArg(new CommandArgument("lorbeeren", lorbeerAmount + "-" + maxLorbeerAmount));
            addUpdateArg(new CommandArgument("coinsPerLorbeer", goldPerLorbeer));
        }

    }

    @Override
    public void performActiveSkill(ActiveSkill skill) {
        switch (skill) {
            case LORBEER_ATTACK:
                attack();
                break;
            case LORBEER_SELL:
                sellLorbeers();
                break;
            case LORBEER_PRESTIGE:
                prestige();
                break;
            case LORBEER_TRADE:
                trade();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void sellLorbeers() {
        if (!isKopfgeld) {
            if (lorbeerAmount > 0) {
                serverGame.addGold(goldPerLorbeer * lorbeerAmount);
                lorbeerAmount = 0;

            }
        } else {
            serverGame.addGold(headHunt.getGoldOnFinished());
            generateHeadHunt();
        }
    }

    private void attack() {
        if (canAttack) {
            canAttack = false;
            attackTimer = 0;

            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.PERFORM_ACTIVESKILL_TC, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("skill", ActiveSkill.LORBEER_ATTACK.ordinal())}));

            ernteRauschKillCount = 0;
            serverGame.getMobs().forEach((Mob mob) -> {
                if (isInRange(mob, range)) {

                    executiveDamage.setDamageVal(0.0d);

                    if (isExecutive) {
                        executiveDamage.setDamageVal(EXECUTIVE_PERCENTMISSINGHEALTH * (mob.getMaxHP() - mob.getHP()));
                    }
                    mob.damage(damageObject);
                    if (mob.getHP() <= 0) {
                        ernteRauschKillCount++;
                        if (isKopfgeld && headHunt != null) {
                            headHunt.notifyKill(mob);
                        }
                    } else if (isWiederverwertung) {
                        mob.getEffectSet().addEffect(new EffectSet.Effect(EffectSet.EffectType.GOLDED, EffectSet.Effect.UNLIMITED));
                    }
                }
            });
            if (isErnteRausch && ernteRauschKillCount >= RAUSCH_KILL_AMOUNT) {
                attackTimer += attackCooldown / 2;
            }

        }

    }

    private void prestige() {
        if (lorbeerAmount >= maxLorbeerAmount) {
            lorbeerAmount = 0;
            double extraMult = maxLorbeerAmount / DEFAULT_MAX_LORBEERS;
            goldPerLorbeer += PRESTIGE_ADD * extraMult;
        }
    }

    private void trade() {
        Logger.logServer("trade!");
        if (lorbeerAmount >= maxLorbeerAmount) {
            lorbeerAmount = 0;
            addTradeUpgrade(generateTauschhandelUpgrade());
        }
    }

    public void addTradeUpgrade(Upgrade upgrade) {
        tradeUpgradesAdd.add(upgrade);
    }

    public boolean removeTradeUpgrade(Upgrade upgrade) {
        if (tauschhandelUpgrades.contains(upgrade)) {
            tradeUpgradesRemove.add(upgrade);
            return true;
        } else {
            return false;
        }
    }

    private void updateTauschhandel() {
        if (tradeUpgradesAdd.size() != 0) {
            Upgrade add = tradeUpgradesAdd.poll();
            serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.EDIT_TAUSCHHANDEL, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("mode", 1), new CommandArgument("upgradeId", add.ordinal())}));
            tauschhandelUpgrades.add(add);
        }
        if (tradeUpgradesRemove.size() != 0) {
            Upgrade remove = tradeUpgradesRemove.poll();
            if (tauschhandelUpgrades.contains(remove)) {
                serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.EDIT_TAUSCHHANDEL, new CommandArgument[]{new CommandArgument("id", id), new CommandArgument("mode", 0), new CommandArgument("upgradeId", remove.ordinal())}));
                tauschhandelUpgrades.remove(remove);
            }
        }
    }

    private void sendKopfgeldUpdate() {
        serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.EDIT_KOPFGELD, new CommandArgument[]{
            new CommandArgument("id", id),
            new CommandArgument("headhunt", headHunt.toString())}));
    }

    private Upgrade generateTauschhandelUpgrade() {

        Randomizer rand = new Randomizer();

        for (Upgrade upgrade : Upgrade.values()) {
            rand.addEvent(new RandomEvent(upgrade.ordinal(), Math.pow((double) upgrade.getPrize(), -tauschhandelPow)));
        }

        return Upgrade.values()[rand.throwDice()];
    }

    private void generateHeadHunt() {
        double fact = 1.0d;
        if (isMassenproduktion) {
            fact += 0.25;
        }
        if (upgrades.contains(Upgrade.LORBEER_1_2)) {
            fact += 0.25;
        }
        if (upgrades.contains(Upgrade.LORBEER_1_4)) {
            fact += 0.25;
        }
        headHunt = headHuntGenerator.generateHeadHunt(fact);
    }

    public static class HeadHuntGenerator {

        private Randomizer randomizer;
        private ServerGame game;
        private String id;

        public HeadHuntGenerator(ServerGame game, String id) {
            this.id = id;
            setUpRandomizer();
            this.game = game;
        }

        private final void setUpRandomizer() {
            randomizer = new Randomizer();
            randomizer.addEvent(new RandomEvent(1, 4));
            randomizer.addEvent(new RandomEvent(2, 4));
            randomizer.addEvent(new RandomEvent(3, 4));
            randomizer.addEvent(new RandomEvent(4, 2));
            randomizer.addEvent(new RandomEvent(5, 1));
        }

        public HeadHunt generateHeadHunt(double upgradesFact) {
            return new HeadHunt(randomDifficulty(), upgradesFact);
        }

        private int randomDifficulty() {
            return randomizer.throwDice();
        }

        public static class HeadHunt {

            private List<GameObjectType> mobs;
            private Vector<Mission> missions;
            private int totalGold, goldOnFinished;
            private double bonusFact = 1;
            private boolean isUpdate = true;

            public HeadHunt(int difficulty, double upgradeFact) {

                missions = new Vector<>();
                mobs = new ArrayList<>(Arrays.asList(GameObject.getMobs()));
                mobs.sort(new Comparator<GameObjectType>() {
                    @Override
                    public int compare(GameObjectType arg0, GameObjectType arg1) {
                        return Mob.getCoinValue(arg0) - Mob.getCoinValue(arg1);
                    }
                });

                switch (difficulty) {
                    case 1:
                        totalGold = 50;
                        break;
                    case 2:
                        totalGold = 100;
                        break;
                    case 3:
                        totalGold = 200;
                        break;
                    case 4:
                        totalGold = 500;
                        bonusFact = 1.125;
                        break;
                    case 5:
                        totalGold = 1000;
                        bonusFact = 1.25;
                        break;
                    default:
                        totalGold = 100;
                        break;
                }
                bonusFact *= upgradeFact;

                calculateMissions();

            }

            public void notifyKill(Mob mob) {
                isUpdate = true;
                Logger.logServer("Head hunt kill notified!");
                missions.forEach((Mission mission) -> {
                    mission.notifyKill(mob);
                });
            }

            public boolean isUpdate() {
                boolean oldIsUpdate = isUpdate;
                isUpdate = false;
                return oldIsUpdate;
            }

            private final void calculateMissions() {
                int goldToGet = totalGold;

                while (goldToGet > 0 && mobs.size() > 0) {
                    GameObjectType currentType = (GameObjectType) Randomizer.getRandomElement(mobs);
                    mobs.remove(currentType);
                    int amount = (int) (((double) goldToGet) / Mob.getCoinValue(currentType));
                    if (amount != 0) {
                        goldToGet -= amount * 0.5 * Mob.getCoinValue(currentType);
                        missions.add(new Mission(amount, currentType));
                    }
                }
            }

            public boolean isFinished() {
                for (Mission mission : missions) {
                    if (mission.getAmountReady() != mission.getAmount()) {
                        return false;
                    }
                }
                return true;
            }

            public Vector<Mission> getMissions() {
                return missions;
            }

            public int getGoldOnFinished() {
                return ((int) (bonusFact * totalGold * 0.1)) * 10;
            }

            @Override
            public String toString() {
                return new Gson().toJson(this);
            }

            public static HeadHunt fromString(String s) {
                return new Gson().fromJson(s, HeadHunt.class);
            }

        }

        public static class Mission {

            public final int amount;
            public final GameObjectType gameObjecType;
            public final int coinValue;
            private int amountReady = 0;

            public Mission(int amount, GameObjectType gameObjectType) {
                this.amount = amount;
                this.gameObjecType = gameObjectType;
                this.coinValue = Mob.getCoinValue(gameObjecType);
            }

            public void notifyKill(Mob mob) {
                if (amountReady < amount && mob.getGameObjectType() == gameObjecType) {
                    amountReady++;
                }
            }

            public int getAmountReady() {
                return amountReady;
            }

            public GameObjectType getGameObjectType() {
                return gameObjecType;
            }

            public int getAmount() {
                return amount;
            }
        }
    }

}
