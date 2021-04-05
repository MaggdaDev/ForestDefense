/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import java.util.Vector;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class Path {

    private Vector<PathCell> cells;
    private Vector<WaySegment> ways;
    private int priority = 0;
    
    double wayWalked = 0;

    public Path() {
        cells = new Vector<>();
        ways = new Vector<>();
    }
    
    public void setPriority(int p) {
        priority = p;
    }
    
    public int getPriority() {
        return priority;
    }

    public void generate(PathCell endCell) {
        cells = endCell.generateVector(cells);
        setUpWay();
    }

    public void setUpWay() {
        for (int i = cells.size() - 1; i > 0; i--) {
            ways.add(new WaySegment(cells.get(i), cells.get(i - 1)));
        }
    }

    public boolean walk(double delta) {
        if(ways.size() == 0) {
            return true;
        }
        if (wayWalked + delta < ways.size() * MapCell.CELL_SIZE) {

            wayWalked += delta;
            return false;
        } else {
            return true;    //arrived
        }
    }

    public double getCurrentX() {
        int currentWay = (int) (wayWalked / MapCell.CELL_SIZE);
        if(ways.size() == 0) {
            return 0;
        }
        double x = ways.get(currentWay).getXOnWay((wayWalked - currentWay * MapCell.CELL_SIZE) / MapCell.CELL_SIZE);
        return x;
    }

    public double getCurrentY() {
        if(ways.size() == 0) {
            return 0;
        }
        int currentWay = (int) (wayWalked / MapCell.CELL_SIZE);
        return ways.get(currentWay).getYOnWay((wayWalked - currentWay * MapCell.CELL_SIZE) / MapCell.CELL_SIZE);
    }
    
    public double getRestWay() {
        return ways.size() * MapCell.CELL_SIZE - wayWalked;
    }
}
