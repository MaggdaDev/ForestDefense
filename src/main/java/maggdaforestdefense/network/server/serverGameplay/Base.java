/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

/**
 *
 * @author David
 */
public class Base extends MapCell{
    private int essence = ServerGame.START_ESSENCE, maxEssence = ServerGame.START_ESSENCE;
    private ServerGame serverGame;
    public Base(Map map, int xIndex, int yIndex, ServerGame game) {
        super(CellType.BASE, map, xIndex, yIndex);
        setUpNeightbours();
        serverGame = game;
    }
    
    public void damageBase(Mob mob) {
        essence --;
        mob.die(false);
        checkAlive();
    }
    
    public void checkAlive() {
        if(essence < 0) {
            essence = 0;
            serverGame.endGame();
        }
    }
    
    public int getEssence() {
        return essence;
    }
    
    public int getMaxEssence() {
        return maxEssence;
    }

    public void refillEssence() {
        essence = maxEssence;
    }
}
