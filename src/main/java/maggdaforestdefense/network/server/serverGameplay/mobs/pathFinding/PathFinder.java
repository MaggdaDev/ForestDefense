/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import maggdaforestdefense.network.server.serverGameplay.MapCell;

/**
 *
 * @author DavidPrivat
 */
public class PathFinder {
    private PriorityQueue<PathCell> openList;
    private HashSet<PathCell> closedList;
    private PathCell start, end;
    public PathFinder(PathCell start, PathCell end, PathCell[][] map) {
        closedList = new HashSet();
        openList = new PriorityQueue<>(new Comparator() {
            @Override
            public int compare(Object arg0, Object arg1) {
                if(((PathCell)arg0).getFValue(end) < ((PathCell)arg1).getFValue(end)) {
                    return -1;
                } else if(((PathCell)arg0).getFValue(end) == ((PathCell)arg1).getFValue(end)) {
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
        Path path = new Path();
        openList.add(start);
        
        boolean endFound = false;
        
        while(!endFound) {
            PathCell currentCell = openList.remove();
            
            if(currentCell.equals(end)) {
                endFound = true;
                continue;
            }
            closedList.add(currentCell);
            
            //expand
            for(PathCell neighbourCell: currentCell.getNeighbours()) {
                
                if(neighbourCell == null || closedList.contains(neighbourCell)) {
                    continue;
                }
                double newDistToStart = currentCell.getDistanceToStart() + calculateDistance(currentCell, neighbourCell);
                
                if(openList.contains(neighbourCell) && newDistToStart >= neighbourCell.getDistanceToStart()) {
                    continue;
                }
                neighbourCell.setPrevious(currentCell);
                neighbourCell.setDistanceToStart(newDistToStart);
                
                if(!openList.contains(neighbourCell)) {
                    openList.add(neighbourCell);
                }
            }
            
            if(openList.isEmpty()) {
                return null;
            }
            
        }
        
        path.generate(end);
        
        
        return path;
    }
    
    public static double calculateDistance(PathCell c1, PathCell c2) {
        return MapCell.CELL_SIZE;
    }
}
