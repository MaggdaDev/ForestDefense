/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;

/**
 *
 * @author DavidPrivat
 */
public abstract class Mob extends GameObject {

    protected double xPos, yPos;
    protected ServerGame serverGame;

    public Mob(ServerGame game) {
        super(game.getNextId());
        serverGame = game;
    }
    

    protected void findStartPos() {
        int width = serverGame.getMap().getCells().length;
        int height = serverGame.getMap().getCells()[0].length;
        int random = (int)(Math.random()*4);
        switch(random) {
            case 0:
                xPos = 0;
                yPos = Math.random() * height * MapCell.CELL_SIZE;
                break;
            case 1:
                yPos = 0;
                xPos = Math.random() * width * MapCell.CELL_SIZE;
                break;
            case 2:
                xPos = width * MapCell.CELL_SIZE;
                yPos = Math.random() * height * MapCell.CELL_SIZE;
                break;
            case 3:
                yPos = height * MapCell.CELL_SIZE;
                xPos = Math.random() * width * MapCell.CELL_SIZE;
                break;
        }
    }

    
    
}
