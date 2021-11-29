/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import javafx.geometry.Point2D;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class WaySegment {

    private Point2D start, end, pivot;
    private boolean isCurve;
    private PathCell origin, cell, destination;

    public WaySegment(PathCell origin, PathCell cell, PathCell destination) {
        if (origin == null || destination == null || origin.getXIndex() == destination.getXIndex() || origin.getYIndex() == destination.getYIndex()) {
            isCurve = false;
        } else {
            isCurve = true;
            pivot = origin.getPointBetween(destination);
        }

        if (origin == null) {
            start = new Point2D(cell.getXPos(), cell.getYPos());
        } else {
            start = cell.getPointBetween(origin);
        }

        if (destination == null) {
            end = new Point2D(cell.getXPos(), cell.getYPos());
        } else {
            end = cell.getPointBetween(destination);
        }
        this.origin = origin;
        this.cell = cell;
        this.destination = destination;
    }

    public Point2D getPointOnWay(double pos) {      // part between 0 (start) and 1 (end)

        if (isCurve) {
            double ax = Math.signum(pivot.getX() - start.getX());
            double ay = Math.signum(pivot.getY() - start.getY());
            double rotDir = -(Math.abs(ax) * 2.0d - 1.0d);
            double angAdd = 0.5d * Math.PI * Math.abs(ay);
            double xSign = Math.signum(cell.getXPos() - pivot.getX());
            double ySign = Math.signum(cell.getYPos() - pivot.getY());
            double x = Math.abs(Math.cos(angAdd + rotDir * pos * (0.5d * Math.PI))) * xSign;
            double y = Math.abs(Math.sin(angAdd + rotDir * pos * (0.5d * Math.PI))) * ySign;
            return pivot.add(new Point2D(x, y).multiply(MapCell.CELL_SIZE * 0.5d));
        } else {
            return start.add(end.subtract(start).multiply(pos));
        }
    }

    public double getXOnWay(double pos) {        //part between 0 (start) and 1 (end)
        return getPointOnWay(pos).getX();
    }

    public double getYOnWay(double pos) {        //part between 0 (start) and 1 (end)
        return getPointOnWay(pos).getY();
    }

    public Point2D getDirection(double pos) {
        if (isCurve) {
            Point2D diff = getPointOnWay(pos).subtract(pivot);
            Point2D poss1 = new Point2D(diff.getY(), diff.getX() * (-1)).normalize();
            Point2D poss2 = new Point2D(diff.getY() * (-1), diff.getX()).normalize();
            Point2D line = end.subtract(start);
            if (poss1.distance(line) < poss2.distance(line)) {
                return poss1;
            } else {
                return poss2;
            }
        } else {
            return end.subtract(start);
        }
    }

    public double getDirectionX(double pos) {
        return getDirection(pos).getX();
    }

    public double getDirectionY(double pos) {
        return getDirection(pos).getY();
    }

    public double getDistance() {
        if (isCurve) {
            return MapCell.CELL_SIZE * 0.25d * Math.PI;
        } else {
            if (origin == null && destination == null) {
                return 0;
            } else if (origin == null || destination == null) {
                return 0.5d * MapCell.CELL_SIZE;
            } else {
                return MapCell.CELL_SIZE;
            }
        }
    }

}
