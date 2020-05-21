/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import java.util.Vector;

/**
 *
 * @author DavidPrivat
 */
public class Path {
    
    private Vector<PathCell> cells;
    public Path() {
       cells = new Vector<>(); 
    }
    
    public void generate(PathCell endCell) {
        cells = endCell.generateVector(cells);
    }
}
