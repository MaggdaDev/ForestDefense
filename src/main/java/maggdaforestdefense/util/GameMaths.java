/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

/**
 *
 * @author DavidPrivat
 */
public class GameMaths {
    public static double nanoToSeconds(long nano) {
        return nano / (1000.0d * 1000.0d * 1000.0d);
    }
    
    public static double radToDegrees(double rad) {
        return (0.5*rad / Math.PI) * 360;
    }
    
    public static double degreesToRad(double deg) {
        return (deg / 360) * Math.PI * 2;
    }
    
    public static double getAbs(double dX, double dY) {
        return Math.sqrt(Math.pow(dX, 2.0d) + Math.pow(dY, 2.0d));
    }
    
    public static double getDegAngleToYAxis(double dX, double dY) {
        if(getAbs(dX, dY) == 0) {
            return 0;
        }
        double oneVecX = dX / getAbs(dX, dY);
        double radToX = Math.acos(oneVecX);
        if(dY < 0 ) {
            radToX = 2*Math.PI - radToX;
        }
        return radToDegrees(radToX + 0.5*Math.PI);
    }
    
    public static boolean isInSquareRange(double x1, double y1, double x2, double y2, double range) {
        if(Math.abs(x1 - x2) <= range && Math.abs(y1 - y2) <= range) {
            return true;
        } else {
            return false;
        }
    }
}
