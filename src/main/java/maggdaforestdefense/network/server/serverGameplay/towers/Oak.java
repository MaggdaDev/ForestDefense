/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.towers;

import java.util.Vector;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.ActiveSkill;
import maggdaforestdefense.network.server.serverGameplay.EffectSet;
import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.Upgrade;
import maggdaforestdefense.network.server.serverGameplay.UpgradeSet;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.*;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.SimplePermaStack;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.UpgradeHandler;

/**
 *
 * @author DavidPrivat
 */
public class Oak extends Tower {

    public final static int DEFAULT_PRIZE = 100;
    public final static double DEFAULT_HEALTH = 250, DEFAULT_REGEN = 1, DEFAULT_RANGE = 0, GROWING_TIME = 15;
    public final static RangeType RANGE_TYPE = RangeType.CIRCLE;

    //Upgrade var
    private boolean isEicheln = false, isWurzeln = false, isRaueRinde = false, isSozial = false, isVerbundeneWurzeln = false, isTotalRegen = false, isEichenWallOrigin = false, isEichenWallMember = false, isSpontanErhaertung = false, isEichelErnte = false;
    private UpgradeHandler sozialHandler;
    private boolean isAnyVerbundeneWurzeln = false;
    private int verbundeneWurzelnAmount = 0;
    private double totalRegenTimer = 0;
    private Vector<Oak> wallOaks;
    private Oak wallOrigin;
    private double spontanErhaertungTimer = 0;
    private int eichelErnteCounter = 0;
    private Vector<Mob> eichelErnteMobs;
    private int addEichelErnte = 0;
    // Upgrade const
    public final double HARTE_RINDE_ADD = 50;
    public final double ATTRACT_RANGE = 1.5;
    public final double REGEN_PER_WATER_ADD = 5;
    public final double SOZIAL_PERCENTAGE_OF_DAMAGE = 0.5;
    public final double VERBUNDENE_WURZELN_PERCENTAGE = 0.5;
    public final double TOTAL_REGEN_COOLDOWN = 200;
    public final double SPONTAN_ERHAERTUNG_COOLDOWN = 10;
    public final double HEALTH_PER_EICHEL_ERNTE = 10;
    

    public Oak(ServerGame game, double x, double y) {
        super(game, x, y, GameObjectType.T_OAK, DEFAULT_PRIZE, UpgradeSet.OAK_SET, DEFAULT_HEALTH, DEFAULT_REGEN, DEFAULT_RANGE, new CanAttackSet(false, false, false), GROWING_TIME, RANGE_TYPE);
        onDamageTaken.add((mob) -> {
            isAnyVerbundeneWurzeln = false;
            verbundeneWurzelnAmount = 0;
            serverGame.getGameObjects().forEach((String id, GameObject gameObject) -> {
                if (gameObject instanceof Oak) {
                    if(((Oak) gameObject).isVerbundeneWurzeln) {
                        isAnyVerbundeneWurzeln = true;
                        verbundeneWurzelnAmount++;
                    }
                }
            });
            
            if(isAnyVerbundeneWurzeln) {
                double healPerOak = ((Mob)mob).getDamage() * VERBUNDENE_WURZELN_PERCENTAGE / (double)verbundeneWurzelnAmount;
                serverGame.getGameObjects().forEach((String id, GameObject gameObject) -> {
                if (gameObject instanceof Oak) {
                    if(((Oak) gameObject).isVerbundeneWurzeln) {
                        ((Oak)gameObject).heal(healPerOak);
                    }
                }
            }); 
             
            }
            
            if(isInWall()) {
                heal(((Mob)mob).getDamage());
                wallOrigin.doWallDamage(((Mob)mob).getDamage());
            }
            
            if(isSpontanErhaertung) {
                if(effectSet.isActive(EffectSet.EffectType.OAK_ERHAERTUNG)) {
                    heal(((Mob)mob).getDamage());
                    effectSet.removeEffect(EffectSet.EffectType.OAK_ERHAERTUNG);
                }
            } 
            
            if(isEichelErnte) {
                if(!eichelErnteMobs.contains(mob)) {
                    eichelErnteMobs.add((Mob)mob);
                    eichelErnteCounter++;
                    maxHealth += HEALTH_PER_EICHEL_ERNTE;
                    addEichelErnte += HEALTH_PER_EICHEL_ERNTE;
                    serverGame.sendCommandToAllPlayers(new NetworkCommand(NetworkCommand.CommandType.UPDATE_SIMPLE_PERMA_STACKS, new CommandArgument[]{
                        new CommandArgument("id", id),
                        new CommandArgument("type", SimplePermaStack.OAK_EICHELERNTE.ordinal()),
                        new CommandArgument("value", addEichelErnte)
                    }));
                    for(int i = 0; i < eichelErnteMobs.size(); i++) {
                        Mob currMob = eichelErnteMobs.get(i);
                        if(!currMob.checkAlive()) {
                            eichelErnteMobs.remove(currMob);
                        }
                    }
                    
                }
            }
        });
        onTowerChanges.add((o)->{
            if(isEichenWallMember) {
                if(!wallOrigin.isAlive) {
                    isEichenWallMember = false;
                    wallOrigin = null;
                }
            }
        });
        
        wallOaks = new Vector<>();
        wallOaks.add(this);
        eichelErnteMobs = new Vector<>();

    }

    @Override
    public void updateSpecific(double timeElapsed) {
        addUpdateArg(new CommandArgument("maxHealth", maxHealth));
        
        if(isTotalRegen) {
            if(totalRegenTimer > 0) {
                totalRegenTimer -= timeElapsed;
            }
            addUpdateArg(new CommandArgument("totalRegenCooldown", totalRegenTimer));
        }
        if(isSpontanErhaertung) {
            if(spontanErhaertungTimer > 0) {
                spontanErhaertungTimer -= timeElapsed;
            }
            addUpdateArg(new CommandArgument("spontanErhaertungCooldown", spontanErhaertungTimer));
        }
    }

    @Override
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);

        switch (upgrade) {
            case OAK_1_1:
                maxHealth += HARTE_RINDE_ADD;
                healthPoints += HARTE_RINDE_ADD;
                break;
            case OAK_1_2:
                isEicheln = true;
                break;
            case OAK_1_3:
                isWurzeln = true;
                break;
            case OAK_1_4:
                isRaueRinde = true;
                onDamageTaken.add((m) -> {
                    Mob mob = (Mob) m;
                    mob.getEffectSet().addEffect(new EffectSet.Effect(EffectSet.EffectType.SENSITIVE, 3));
                });
                break;
            case OAK_2_1:
                onNewRound.add((o) -> {
                    healthPoints = maxHealth;
                });
                break;
            case OAK_2_2:
                double regenAdd = 0;
                for (PathCell cell : mapCell.getPathCell().getNeighbours()) {
                    if (cell.getCellType() == MapCell.CellType.WATER) {
                        regenAdd += REGEN_PER_WATER_ADD;
                    }

                }
                regenerationPerSecond = regenAdd;
                break;
            case OAK_2_3:
                isSozial = true;
                Oak thisOak = this;
                sozialHandler = new UpgradeHandler() {
                    private final Oak ownerOak = thisOak;

                    @Override
                    public void handleUpgrade(Object mob) {
                        ownerOak.doSozialeEiche(((Mob) mob).getTargetTower(), ((Mob) mob).getDamage());
                    }
                };
                onTowerChanges.add((o) -> {
                    for (PathCell pathCell : mapCell.getPathCell().getNeighbours()) {
                        MapCell cell = serverGame.getMap().getCells()[pathCell.getXIndex()][pathCell.getYIndex()];
                        Tower tower = cell.getTower();
                        if (tower != null) {
                            tower.addOnDamageTaken(sozialHandler);
                        }
                    }
                });

                break;
            case OAK_2_4:
                isVerbundeneWurzeln = true;
                break;
            case OAK_3_1:
                isTotalRegen = true;
                break;
            case OAK_3_2:   // Eichenwall
                isEichenWallOrigin = true;
                wallOrigin = this;
                setUpEichenWall();
                
                break;
            case OAK_3_3:
                isSpontanErhaertung = true;
                break;
            case OAK_3_4:
                isEichelErnte = true;
                break;

        }
    }

    public void doSozialeEiche(Tower tower, double damage) {
        if (healthPoints - SOZIAL_PERCENTAGE_OF_DAMAGE * damage > 0.3 * maxHealth) {
            healthPoints -= SOZIAL_PERCENTAGE_OF_DAMAGE * damage;
            tower.heal(SOZIAL_PERCENTAGE_OF_DAMAGE * damage);
        }
    }
    
    public void doSpontanErhaertung() {
        Logger.logServer("ERHÄRTUNG!!!!!!!!!!!!!!!!!");
        if(spontanErhaertungTimer <= 0) {
            effectSet.addEffect(new EffectSet.Effect(EffectSet.EffectType.OAK_ERHAERTUNG, 3));
            spontanErhaertungTimer = SPONTAN_ERHAERTUNG_COOLDOWN;
        }
    }
    
    public void doWallDamage(double damage) {
        double damagePerOak = damage / (double)wallOaks.size();
        wallOaks.forEach((Oak oak)->{
            oak.getWallDamaged(damagePerOak);
        });
    }
    
    public void getWallDamaged(double d) {
        healthPoints -= d;
    }
    
    private void setUpEichenWall() {
        int hAmount = 1;
        int vAmount = 1;
        

        
        // H pos check
        int counter = 1;
        while(true) {
            if(xIndex + counter >= serverGame.getMap().getCells().length) {
                break;
            }
            MapCell currentCell = serverGame.getMap().getCells()[xIndex + counter][yIndex];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                break;
            }
            hAmount ++;
            counter ++;
        }
        // H neg check
        counter = 1;
        while(true) {
            if(xIndex - counter < 0) {
                break;
            }
            MapCell currentCell = serverGame.getMap().getCells()[xIndex - counter][yIndex];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {  
                break;
            }
            hAmount ++;
            counter ++;
        }
        // V pos check
        counter = 1;
        while(true) {
            if(yIndex + counter >= serverGame.getMap().getCells()[0].length) {
                break;
            }
            MapCell currentCell = serverGame.getMap().getCells()[xIndex][yIndex+counter];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                break;
            }
            vAmount ++;
            counter ++;
        }
        // V neg check
        counter = 1;
        while(true) {
            if(yIndex - counter < 0) {
                break;
            }
            MapCell currentCell = serverGame.getMap().getCells()[xIndex][yIndex-counter];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                break;
            }
            vAmount ++;
            counter ++;
        }
        
        if(vAmount > hAmount) {
            setUpVerticalWall();
        } else {
            setUpHorizontalWall();
        }
        
    }
    
    private void setUpVerticalWall() {
        for(int y = yIndex+1; y < serverGame.getMap().getCells()[xIndex].length; y++) {
            MapCell currentCell = serverGame.getMap().getCells()[xIndex][y];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                y = serverGame.getMap().getCells()[xIndex].length;
            } else{
                Oak currOak = (Oak)currentCell.getTower();
                    wallOaks.add(currOak);
                    currOak.setInWall(this);
                
            }
            
        }
        
        for(int y = yIndex-1; y >= 0; y--) {
            MapCell currentCell = serverGame.getMap().getCells()[xIndex][y];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                y = -1;
            } else{
                Oak currOak = (Oak)currentCell.getTower();
                    wallOaks.add(currOak);
                    currOak.setInWall(this);
            }
            
        }
    }
    
    private void setUpHorizontalWall() {
        for(int x = xIndex+1; x < serverGame.getMap().getCells().length; x++) {
            MapCell currentCell = serverGame.getMap().getCells()[x][yIndex];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                x = serverGame.getMap().getCells().length;
            } else{
                Oak currOak = (Oak)currentCell.getTower();
                    wallOaks.add(currOak);
                    currOak.setInWall(this);
            }
            
        }
        
        for(int x = xIndex-1; x >= 0; x--) {
            MapCell currentCell = serverGame.getMap().getCells()[x][yIndex];
            if(currentCell.getTower() == null || !(currentCell.getTower() instanceof Oak) || ((Oak)currentCell.getTower()).isInWall()) {
                x = -1;
            } else{
                Oak currOak = (Oak)currentCell.getTower();
                    wallOaks.add(currOak);
                    currOak.setInWall(this);
            }
            
        }
    }
    
    public boolean isVerbundeneWurzeln() {
        return isVerbundeneWurzeln;
    }
    
    public boolean isInWall() {
        return (isEichenWallMember || isEichenWallOrigin);
    }
    
    public void setInWall(Oak oakWallOrigin) {
        isEichenWallMember = true;
        wallOrigin = oakWallOrigin;
    }
    
    public void doTotalRegen() {
        if(isTotalRegen && totalRegenTimer <= 0) {
            totalRegenTimer = TOTAL_REGEN_COOLDOWN;
            healthPoints = maxHealth;
        }
    }
    
    @Override
    public void performActiveSkill(ActiveSkill skill) {
        switch(skill) {
            case OAK_TOTALREGEN:
                doTotalRegen();
                break;
            case OAK_SPONTANERHAERTUNG:
                doSpontanErhaertung();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

  
    @Override
    public CommandArgument[] toNetworkCommandArgs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean shouldPrioritize(double dist, Mob.MovementType movement) {
        if (isEicheln && movement == Mob.MovementType.FLY && dist < ATTRACT_RANGE * mapCell.CELL_SIZE) {
            return true;
        }
        if (isWurzeln && movement == Mob.MovementType.DIG && dist < ATTRACT_RANGE * mapCell.CELL_SIZE) {
            return true;
        }
        return false;
    }

}
