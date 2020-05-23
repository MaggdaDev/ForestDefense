/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs;

import maggdaforestdefense.network.server.serverGameplay.GameObject;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.Path;
import maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding.PathFinder;

/**
 *
 * @author DavidPrivat
 */
public abstract class Mob extends GameObject {

    protected double xPos, yPos;
    protected int startXIndex, startYIndex;
    protected ServerGame serverGame;
    
    protected PathFinder pathFinder;
    protected Path path;
    
    protected GameObjectType gameObjectType;
    

    public Mob(ServerGame game, GameObjectType objectType) {
        super(game.getNextId());
        serverGame = game;
        gameObjectType = objectType;
    }
    

    protected void findStartPos() {
        int width = serverGame.getMap().getCells().length;
        int height = serverGame.getMap().getCells()[0].length;
        int random = (int)(Math.random()*4);
        switch(random) {
            case 0:
                startXIndex = 0;
                startYIndex = (int)(Math.random() * height);
                break;
            case 1:
                startYIndex = 0;
                startXIndex =(int) (Math.random() * width);
                break;
            case 2:
                startXIndex = width-1;
                startYIndex = (int) (Math.random() * height);
                break;
            case 3:
                startYIndex = height-1;
                startXIndex = (int) (Math.random() * width);
                break;
        }
        xPos = startXIndex * MapCell.CELL_SIZE;
        yPos = startYIndex * MapCell.CELL_SIZE;
        initializePathFinder();
    }
    
    protected void initializePathFinder() {
        pathFinder = new PathFinder(serverGame.getMap().getCells()[startXIndex][startYIndex].getPathCell(), serverGame.getMap().getBase().getPathCell(), serverGame.getMap().toPathCellMap(), gameObjectType);
        path = pathFinder.findPath();
    }

    
    
}
