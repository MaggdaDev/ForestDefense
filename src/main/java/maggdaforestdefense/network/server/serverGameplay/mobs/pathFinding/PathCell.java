/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import java.util.Vector;
import javafx.geometry.Point2D;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

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
    private double dijkstraDistance = Double.MAX_VALUE;
    
    private PathCell[] neighbours;

    private MapCell.CellType cellType;

    public PathCell(int x, int y, double width, double height, MapCell.CellType cellType) {
        xIndex = x;
        yIndex = y;
        xPos = xIndex * width + width / 2;
        yPos = yIndex * height + height / 2;

        fValue = Double.MAX_VALUE;

        this.cellType = cellType;

    }
    
   

    public double getFValue(PathCell endCell) {
        distanceToEnd = getDistance(this, endCell);
        fValue = distanceToEnd + distanceToStart;
        return fValue;
    }
    
    public void setDijkstraDist(double d) {
        dijkstraDistance = d;
    }
    
    public double getDijkstraDist() {
        return dijkstraDistance;
    }
    

    public Vector<PathCell> generateVector(Vector<PathCell> vec) {
        vec.add(this);
        if (previousCell != null) {
            return previousCell.generateVector(vec);
        } else {
            return vec;
        }
    }
    
    public Point2D getPointBetween(PathCell c2) {
        return new Point2D((this.getXPos() + c2.getXPos())/2, (this.getYPos() + c2.getYPos())/2);
    }
    
    public Point2D getOpposingPoint(Point2D p) {
        return new Point2D(2 * this.getXPos() - p.getX(), 2 * this.getYPos() - p.getY());
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

    public void setCellType(MapCell.CellType t) {
        cellType = t;
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

    public MapCell.CellType getCellType() {
        return cellType;
    }

    public static double getDistance(PathCell c1, PathCell c2) {
        return Math.sqrt(Math.pow(c2.getXPos() - c1.getXPos(), 2.0d) + Math.pow(c2.getYPos() - c1.getYPos(), 2.0d))/MapCell.CELL_SIZE;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof PathCell) {
            PathCell c = (PathCell)o;
            if(c.getXIndex() == this.getXIndex() && c.getYIndex() == this.getYIndex()) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }
}
