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
    double wayWalked = 0;

    public Path() {
        cells = new Vector<>();
        ways = new Vector<>();
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

    public void walk(double delta) {
        if (wayWalked + delta < ways.size() * MapCell.CELL_SIZE) {

            wayWalked += delta;
        }
    }

    public double getCurrentX() {
        int currentWay = (int) (wayWalked / MapCell.CELL_SIZE);
        double x = ways.get(currentWay).getXOnWay((wayWalked - currentWay * MapCell.CELL_SIZE) / MapCell.CELL_SIZE);
        return x;
    }

    public double getCurrentY() {
        int currentWay = (int) (wayWalked / MapCell.CELL_SIZE);
        return ways.get(currentWay).getYOnWay((wayWalked - currentWay * MapCell.CELL_SIZE) / MapCell.CELL_SIZE);
    }
    
    public double getRestWay() {
        return ways.size() * MapCell.CELL_SIZE - wayWalked;
    }
}