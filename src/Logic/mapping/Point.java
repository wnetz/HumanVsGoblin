package Logic.mapping;

import GameObject.Land;

public class Point
{
    private Land land;
    private double x,y;
    public  Point(double x1,double y1,Land l)
    {
        land = l;
        x = x1;
        y = y1;
    }
    public  Point(double x1,double y1)
    {
        land = null;
        x = x1;
        y = y1;
    }
    public double getDistence(Point p1)
    {
        return Math.sqrt(Math.pow(x - p1.x,2) + Math.pow(y - p1.y,2));
    }
    @Override
    public boolean equals(Object p1)
    {
        return p1 instanceof Point ? x == ((Point)p1).x && y == ((Point)p1).y : false;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
