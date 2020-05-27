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
    
    public static double getAbs(double dX, double dY) {
        return Math.sqrt(Math.pow(dX, 2.0d) + Math.pow(dY, 2.0d));
    }
    
    public static double getDegAngleToYAxis(double dX, double dY) {
        if(getAbs(dX, dY) == 0) {
            return 0;
        }
        double oneVecX = dX / getAbs(dX, dY);
        double rad = Math.asin(oneVecX);
        if(dY > 0 ) {
            rad += Math.PI;
        }
        return radToDegrees(rad);
    }
}
