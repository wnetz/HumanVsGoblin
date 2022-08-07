package Logic.mapping;

import GameObject.Land;

public class Point
{
    private int x,y;
    private Land land;
    public Point(int x1, int y1, Land l)
    {
        x = x1;
        y = y1;
        land = l;
    }
    public Point(int x1, int y1)
    {
        x = x1;
        y = y1;
    }
    public double getDistance(Point p1)
    {
        return Math.abs(Math.sqrt(Math.pow(x - p1.x,2) + Math.pow(y - p1.y,2)));
    }
    @Override
    public boolean equals(Object p1)
    {
        return p1 instanceof Point ? x == ((Point)p1).x && y == ((Point)p1).y : false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public int det(Point point)
    {
        return x*point.y - y* point.x;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
