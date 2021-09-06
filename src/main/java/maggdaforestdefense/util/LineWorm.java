/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Point2D;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class LineWorm {

    private List<DirectionPoint> points;   // array of at least 2 points

    public LineWorm() {
        this.points = new ArrayList<>();
    }

    private LineWorm(double[] pointArr) {
        this.points = new ArrayList<>();
        for (int i = 0; i < pointArr.length / 4; i++) {
            points.add(new DirectionPoint(new Point2D(pointArr[4 * i], pointArr[4 * i + 1]), new Point2D(pointArr[4 * i + 2], pointArr[4 * i + 3])));
        }
    }

    public List<DirectionPoint> getPoints() {
        return points;
    }

    public DirectionPoint getFromHead(Point2D head, double len) {
        double remainingLen = len;
        double headToFirstPoint = head.distance(points.get(points.size() - 1).getPosition());
        if (headToFirstPoint > len) {
            return getPointOnWay(points.get(points.size() - 1), head, len);
        }
        if (points.size() < 2) {
            return points.get(0);
        }
        remainingLen -= headToFirstPoint;
        for (int i = points.size() - 2; i >= 0; i--) {
            double currDist = points.get(i).getDistanceTo(points.get(i + 1));
            if (currDist > remainingLen) {
                return getPointOnWay(points.get(i), points.get(i+1).getPosition(), remainingLen);
            } else {
                remainingLen -= currDist;
            }
        }

        return points.get(0);
    }

    private DirectionPoint getPointOnWay(DirectionPoint start, Point2D end, double len) {
        double complLen = end.distance(start.getPosition()) - len;

        return new DirectionPoint(start.getPosition().add(start.getDirection().normalize().multiply(complLen)), start.getDirection());
    }

    public void addPoint(double xPos, double yPos, double xSpd, double ySpd) {
        DirectionPoint newPoint = new DirectionPoint(new Point2D(xPos, yPos), new Point2D(xSpd, ySpd));
        if (points.size() == 0) {
            points.add(newPoint);
            return;
        } else if (newPoint.sameDirection(points.get(points.size() - 1))) {
            return;
        }

        DirectionPoint cornerPoint = DirectionPoint.calcCornerPoint(points.get(points.size() - 1), newPoint);
        points.add(cornerPoint);
    }
    
    public Point2D getHeadDirection() {
        return points.get(points.size() - 1).getDirection();
    }

    @Override
    public String toString() {
        String retString = "------------------------\n";
        for (DirectionPoint currPoint : points) {
            retString += "\n  |\n" + currPoint.toString();
        }
        retString += "\n----------------------";
        return retString;
    }

    public double[] toArray() {
        double[] pointArr = new double[points.size() * 4];
        for (int i = 0; i < points.size(); i++) {
            pointArr[4 * i] = points.get(i).getPosition().getX();
            pointArr[4 * i + 1] = points.get(i).getPosition().getY();
            pointArr[4 * i + 2] = points.get(i).getDirection().getX();
            pointArr[4 * i + 3] = points.get(i).getDirection().getY();
        }
        return pointArr;
    }

    public static LineWorm fromArray(double[] arr) {
        return new LineWorm(arr);
    }

    public static class DirectionPoint {

        private Point2D position, direction;

        public DirectionPoint(Point2D pos, Point2D spd) {
            this.position = new Point2D(pos.getX(), pos.getY());
            this.direction = (new Point2D(spd.getX(), spd.getY())).normalize();
        }

        public double getDistanceTo(DirectionPoint p2) {
            return position.distance(p2.getPosition());
        }

        public void setDirection(Point2D dir) {
            this.direction = dir.normalize();
        }

        public void setPosition(Point2D pos) {
            this.position = pos;
        }

        public Point2D getPosition() {
            return position;
        }

        public Point2D getDirection() {
            return direction;
        }

        @Override
        public String toString() {
            return "xPos: " + position.getX() + "   yPos: " + position.getY() + "      xDir: " + direction.getX() + "   yDir: " + direction.getY();
        }

        public boolean sameDirection(DirectionPoint other) {
            Point2D diff = this.direction.subtract(other.getDirection());
            if (diff.magnitude() < 0.1) {
                return true;
            } else {
                return false;
            }
        }

        public static DirectionPoint calcCornerPoint(DirectionPoint oldPoint, DirectionPoint newPoint) {
            DirectionPoint retPoint = new DirectionPoint(Point2D.ZERO, Point2D.ZERO);
            retPoint.setDirection(newPoint.getDirection());
            Point2D pos = new Point2D(oldPoint.getPosition().getX() * Math.abs(newPoint.getDirection().getX()) + newPoint.getPosition().getX() * Math.abs(oldPoint.getDirection().getX()),
                    oldPoint.getPosition().getY() * Math.abs(newPoint.getDirection().getY()) + newPoint.getPosition().getY() * Math.abs(oldPoint.getDirection().getY()));
            retPoint.setPosition(pos);
            return retPoint;
        }
    }
}
