/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public abstract class HitBox {

    private final HitBoxType hitBoxType;

    private double xPos, yPos;
    private HitBox(HitBoxType type, double x, double y) {
        hitBoxType = type;
        xPos = x;
        yPos = y;
    }

    public static class CircularHitBox extends HitBox {

        double radius;

        public CircularHitBox(double radius, double centerX, double centerY) {
            super(HitBoxType.CIRCLE, centerX, centerY);
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }
    }

    public static boolean intersects(HitBox h1, HitBox h2) {
        switch (h1.getType()) {
            case CIRCLE:
                CircularHitBox c1 = (CircularHitBox)h1;
                switch(h2.getType()) {
                    case CIRCLE:
                        CircularHitBox c2 = (CircularHitBox) h2;
                        if(GameMaths.getAbs(c2.getX() - c1.getX(), c2.getY() - c1.getY()) > c1.getRadius() + c2.getRadius()) {
                            return false;
                        } else {
                            return true;
                        }
                    
                    default: 
                        throw new UnsupportedOperationException();
                }
            
            default:
                throw new UnsupportedOperationException();
        }

    }

    public HitBoxType getType() {
        return hitBoxType;
    }
    
    public double getX() {
        return xPos;
    }
    
    public double getY() {
        return yPos;
    }
    
    public void updatePos(double x, double y) {
        xPos = x;
        yPos = y;
    }

    public static enum HitBoxType {
        CIRCLE;
    }
}
