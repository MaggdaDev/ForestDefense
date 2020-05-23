/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay.mobs.pathFinding;

/**
 *
 * @author DavidPrivat
 */
public class WaySegment {

    private double startX, startY, deltaX, deltaY;

    public WaySegment(PathCell start, PathCell end) {
        startX = start.getXPos();
        startY = start.getYPos();
        deltaX = end.getXPos() - startX;
        deltaY = end.getYPos() - startY;
    }
    
    public double getXOnWay(double pos) {        //part between 0 (start) and 1 (end)
        double ret = startX + deltaX * pos;
        return ret;
    }
    
    public double getYOnWay(double pos) {        //part between 0 (start) and 1 (end)
        return startY + deltaY * pos;
    }
    
    
}
