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
public class Path {

    private Vector<PathCell> cells;
    private Vector<WaySegment> ways;
    private int priority = 0;

    private double wayWalked = 0, totalWayWalked = 0;

    private double totalDistance;

    private int currWayIndex = 0;

    public Path() {
        cells = new Vector<>();
        ways = new Vector<>();
    }

    public Path(Vector<PathCell> pathVec) {
        cells = pathVec;
        ways = new Vector<>();
        setUpWay();
        totalDistance = 0;
        ways.forEach((WaySegment seg) -> {
            totalDistance += seg.getDistance();
        });
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
        totalDistance = 0;
        ways.forEach((WaySegment seg) -> {
            totalDistance += seg.getDistance();
        });
    }

    public void setUpWay() {
        for (int i = cells.size() - 1; i >= 0; i--) {
            PathCell origin = null, cell = null, destination = null;
            if (i < cells.size() - 1) {
                origin = cells.get(i + 1);
            }
            cell = cells.get(i);
            if (i > 0) {
                destination = cells.get(i - 1);
            }
            ways.add(new WaySegment(origin, cell, destination));
        }
    }

    public boolean walk(double delta) {
        if (ways.size() == 0) {
            return true;
        }
        wayWalked += delta;
        totalWayWalked += delta;
        if (wayWalked > getCurrSegment().getDistance()) {
            if (currWayIndex + 1 > ways.size() - 1) {
                System.out.println("ARRIVED");
                return true; // arrived
            } else {
                wayWalked -= getCurrSegment().getDistance();
                currWayIndex++;
            }
        }
        return false;
    }

    public int[] getPosDirBehind(double distBehind) {
        int searchedIndex = currWayIndex;
        double searchedWayWalked;
        if (distBehind < wayWalked) {
            searchedWayWalked = wayWalked - distBehind;
        } else {
            double wayToGo = distBehind - wayWalked;
            searchedIndex--;
            while (searchedIndex >= 0 && wayToGo - ways.get(searchedIndex).getDistance() > 0) {
                wayToGo -= ways.get(searchedIndex).getDistance();
                searchedIndex--;
            }
            if (searchedIndex < 0) {
                return new int[]{-1, -1, -1, -1};
            }
            searchedWayWalked = ways.get(searchedIndex).getDistance() - wayToGo;
        }
        Point2D point = getPointOnSegment(searchedIndex, searchedWayWalked);
        Point2D dir = getDirOnSegment(searchedIndex, searchedWayWalked);
        int[] retArr = new int[]{
            (int) Math.round(10 * point.getX()),
            (int) Math.round(10 * point.getY()),
            (int) Math.round(99 * dir.getX()),
            (int) Math.round(99 * dir.getY())};
        return retArr;
    }

    public Point2D getCurrentDir() {
        return getCurrSegment().getDirection(getPercentOfCurrSeg());
    }

    private double getPercentOfCurrSeg() {
        WaySegment currentWaySegment = getCurrSegment();
        double ret = wayWalked / currentWaySegment.getDistance();
        return ret;
    }

    public Point2D getPointOnSegment(int segIndex, double way) {
        return ways.get(segIndex).getPointOnWay(way / ways.get(segIndex).getDistance());
    }

    public Point2D getDirOnSegment(int segIndex, double way) {
        return ways.get(segIndex).getDirection(way / ways.get(segIndex).getDistance());
    }

    public double getCurrentX() {
        if (ways.size() == 0) {
            return 0;
        }
        WaySegment currentWaySegment = getCurrSegment();

        double x = currentWaySegment.getXOnWay(getPercentOfCurrSeg());
        return x;
    }

    public double getCurrentY() {
        if (ways.size() == 0) {
            return 0;
        }
        WaySegment currentWaySegment = getCurrSegment();
        double y = currentWaySegment.getYOnWay(getPercentOfCurrSeg());
        return y;
    }

    public WaySegment getCurrSegment() {
        if (ways.size() == 0) {
            return null;
        }
        return ways.get(currWayIndex);

    }

    public double getRestWay() {
        return totalDistance - totalWayWalked;
    }
}
