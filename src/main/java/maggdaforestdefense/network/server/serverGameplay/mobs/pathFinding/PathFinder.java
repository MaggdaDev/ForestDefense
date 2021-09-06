/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import javafx.geometry.Point2D;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.Map;
import maggdaforestdefense.network.server.serverGameplay.MapCell;
import maggdaforestdefense.network.server.serverGameplay.ServerGame;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author DavidPrivat
 */
public class PathFinder {

    private PriorityQueue<PathCell> openList;
    private HashSet<PathCell> closedList;
    private PathCell start, end;
    private MapDistanceSet mapDistanceSet;
    private PathCell[][] map;

    private GameObjectType mobType;

    public PathFinder(PathCell start, PathCell end, PathCell[][] map, GameObjectType objectType, MapDistanceSet distanceSet) {
        mobType = objectType;
        this.map = map;
        mapDistanceSet = distanceSet;

        //Pathfind
        closedList = new HashSet();
        openList = new PriorityQueue<>(new Comparator() {
            @Override
            public int compare(Object arg0, Object arg1) {
                if (((PathCell) arg0).getFValue(end) < ((PathCell) arg1).getFValue(end)) {
                    return -1;
                } else if (((PathCell) arg0).getFValue(end) == ((PathCell) arg1).getFValue(end)) {
                    return 0;
                } else {
                    return 1;
                }

            }
        });

        this.start = start;
        this.end = end;
    }

    public Path findPath() {
        switch (mobType) {
            case M_BOSS_CATERPILLAR:
                return caterpillar();
            default:
                return aStar();
        }
    }

    public Path caterpillar() {
        PathCell currentCell = start;
        PathCell nextCell = null, lastCell = null;
        Vector<PathCell> pathVec = new Vector<>();
        pathVec.add(start);
        int counter = 1;
        while (currentCell != end) {
            double bestDist = -1;
            for (PathCell currNeighbour : currentCell.getNeighbours()) {
                if (currNeighbour != null && currNeighbour != lastCell) {
                    double currDist = calculateDistance(currentCell, currNeighbour, counter);
                    if (bestDist == -1 || currDist < bestDist) {
                        nextCell = currNeighbour;
                        bestDist = currDist;

                    }
                }
            }
            if(pathVec.contains(nextCell)) {
                counter += 2;
            }
            lastCell = currentCell;
            currentCell = nextCell;
            pathVec.add(0, currentCell);
            counter++;
        }
        return new Path(pathVec);
    }

    public Path dijkstra() {

        ArrayList<PathCell> q = new ArrayList<>();
        for (int i = 0; i < Map.MAP_SIZE; i++) {
            for (int j = 0; j < Map.MAP_SIZE; j++) {
                q.add(map[i][j]);
                map[i][j].setDijkstraDist(Double.MAX_VALUE);

            }
        }
        Path path = new Path();
        start.setDijkstraDist(0);

        while (!q.isEmpty()) {
            Collections.sort(q, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    PathCell p1 = (PathCell) o1;
                    PathCell p2 = (PathCell) o2;
                    if (p1.getDijkstraDist() == p2.getDijkstraDist()) {
                        return 0;
                    } else if (p1.getDijkstraDist() < p2.getDijkstraDist()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            PathCell currCell = q.remove(0);

            for (PathCell currNeighbour : currCell.getNeighbours()) {
                if (q.contains(currNeighbour)) {
                    double alternativeWay = currCell.getDijkstraDist() + calculateDistance(currCell, currNeighbour);
                    if (alternativeWay < currNeighbour.getDijkstraDist()) {
                        currNeighbour.setDijkstraDist(alternativeWay);
                        currNeighbour.setPrevious(currCell);
                    }
                }
            }
        }
        start.setPrevious(null);
        path.generate(end);
        return path;
    }

    public Path aStar() {
        Path path = new Path();
        openList.clear();
        closedList.clear();
        openList.add(start);

        boolean endFound = false;

        while (!endFound) {
            PathCell currentCell = openList.remove();

            if (currentCell.equals(end)) {
                endFound = true;
                continue;
            }
            closedList.add(currentCell);

            //expand
            for (PathCell neighbourCell : currentCell.getNeighbours()) {

                if (neighbourCell == null || closedList.contains(neighbourCell)) {
                    continue;
                }
                double newDistToStart = currentCell.getDistanceToStart() + calculateDistance(currentCell, neighbourCell);

                if (openList.contains(neighbourCell) && newDistToStart >= neighbourCell.getDistanceToStart()) {
                    continue;
                }
                neighbourCell.setPrevious(currentCell);
                neighbourCell.setDistanceToStart(newDistToStart);

                if (!openList.contains(neighbourCell)) {
                    openList.add(neighbourCell);
                }
            }

            if (openList.isEmpty()) {
                return null;
            }

        }
        start.setPrevious(null);
        path.generate(end);
        return path;
    }

    public double calculateDistance(PathCell c1, PathCell c2, int pathLengthAlready) {
        double distance = 0;
        for (PathCell cell : new PathCell[]{c1, c2}) {
            switch (cell.getCellType()) {
                case BASE:
                case DIRT:
                case UNDEFINED:
                    distance += mapDistanceSet.getDirt();
                    break;
                case WATER:
                    distance += mapDistanceSet.getWater();
                    break;
                case STONE:
                    distance += mapDistanceSet.getStone();
                    break;
                case SAND:
                    distance += mapDistanceSet.getSand();
                    break;

            }
        }
        double momentum = 200 - 1.3 * pathLengthAlready;//1000 * Math.exp(-0.02d * pathLengthAlready);
        double distToMid = new Point2D(end.getXPos(), end.getYPos()).distance(new Point2D(c1.getXPos(), c1.getYPos()));
        double petalForce = momentum * momentum / distToMid;
        Point2D dirToMid = new Point2D(end.getXPos() - c1.getXPos(), end.getYPos() - c1.getYPos()).normalize();
        double forceToMid = 50;
        Point2D forceVecToMid = dirToMid.multiply(forceToMid - petalForce);
        Point2D cellDiff = new Point2D(c2.getXPos() - c1.getXPos(), c2.getYPos() - c1.getYPos()).normalize();
        Point2D rotDir = new Point2D(end.getYPos() - c1.getYPos(), c1.getXPos() - end.getXPos()).normalize();
        Point2D rotForce = rotDir.multiply(momentum);
        Point2D totalDir = rotForce.add(forceVecToMid);
        double alpha = Math.abs(Math.acos(totalDir.normalize().dotProduct(cellDiff)));
        double antiRotAdd = (alpha / Math.PI) * 200;
        if(distToMid < MapCell.CELL_SIZE * 2) {
            distance *= 0.2;
        }
        return distance + antiRotAdd;

    }

    public double calculateDistance(PathCell c1, PathCell c2) {
        double distance = 0;
        switch (mobType) {
            case M_BOSS_CATERPILLAR:
                distance = 0;
                for (PathCell cell : new PathCell[]{c1, c2}) {
                    switch (cell.getCellType()) {
                        case BASE:
                        case DIRT:
                        case UNDEFINED:
                            distance += mapDistanceSet.getDirt();
                            break;
                        case WATER:
                            distance += mapDistanceSet.getWater();
                            break;
                        case STONE:
                            distance += mapDistanceSet.getStone();
                            break;
                        case SAND:
                            distance += mapDistanceSet.getSand();
                            break;

                    }
                }
                Point2D rotDir = new Point2D(end.getYPos() - c1.getYPos(), c1.getXPos() - end.getXPos()).normalize();
                Point2D cellDiff = new Point2D(c2.getXPos() - c1.getXPos(), c2.getYPos() - c1.getYPos()).normalize();
                double alpha = Math.abs(Math.acos(rotDir.dotProduct(cellDiff)));
                double distToMid = new Point2D(end.getXPos(), end.getYPos()).distance(new Point2D(c1.getXPos(), c1.getYPos()));
                double mult = (alpha / Math.PI) * 20000000;
                return distance + mult;
            default:
                distance = 0;
                for (PathCell cell : new PathCell[]{c1, c2}) {
                    switch (cell.getCellType()) {
                        case BASE:
                        case DIRT:
                        case UNDEFINED:
                            distance += mapDistanceSet.getDirt();
                            break;
                        case WATER:
                            distance += mapDistanceSet.getWater();
                            break;
                        case STONE:
                            distance += mapDistanceSet.getStone();
                            break;
                        case SAND:
                            distance += mapDistanceSet.getSand();
                            break;

                    }
                }
                return distance;
        }
    }
}
