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
public class PathCell {

    private int xIndex, yIndex;
    private double distanceToStart = 0, distanceToEnd = 0;
    private double xPos, yPos;
    private double fValue;
    private PathCell previousCell;
    private PathCell[] neighbours;

    public PathCell(int x, int y, double width, double height) {
        xIndex = x;
        yIndex = y;
        xPos = xIndex * width + width / 2;
        yPos = yIndex * height + height / 2;

        fValue = Double.MAX_VALUE;

        

    }

    public double getFValue(PathCell endCell) {
        distanceToEnd = getDistance(this, endCell);
        fValue = distanceToEnd + distanceToStart;
        return fValue;
    }
    
    public Vector<PathCell> generateVector(Vector<PathCell> vec) {
        vec.add(this);
        if(previousCell != null) {
            return previousCell.generateVector(vec);
        } else {
            return vec;
        }
    }
    
    public void setNeighbours(PathCell[] n) {
        neighbours = n;
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getDistanceToStart() {
        return distanceToStart;
    }

    public double getDistanceToEnd() {
        return distanceToEnd;
    }

    public void setDistanceToStart(double d) {
        distanceToStart = d;

    }

    public void setDistanceToEnd(double d) {
        distanceToEnd = d;
    }

    public int getXIndex() {
        return xIndex;
    }

    public int getYIndex() {
        return yIndex;
    }

    public void setPrevious(PathCell c) {
        previousCell = c;
    }

    public PathCell getPreviousCell() {
        return previousCell;
    }

    public PathCell[] getNeighbours() {
        return neighbours;
    }

    public static double getDistance(PathCell c1, PathCell c2) {
        return Math.sqrt(Math.pow(c2.getXPos() - c1.getXPos(), 2.0d) + Math.pow(c2.getYPos() - c1.getYPos(), 2.0d));
    }
}
