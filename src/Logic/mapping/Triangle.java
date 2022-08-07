package Logic.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Triangle
{
    private Point[] points = new Point[3];
    private Triangle triangles[] = new Triangle[3];
    public Triangle(Point p1, Point p2, Point p3)
    {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;

        triangles[0] = null;
        triangles[1] = null;
        triangles[2] = null;

        reorder();
    }
    public Triangle(Point[] p1)
    {
        points[0] = p1[0];
        points[1] = p1[1];
        points[2] = p1[2];

        triangles[0] = null;
        triangles[1] = null;
        triangles[2] = null;

        reorder();
    }
    @Override
    public boolean equals(Object t1)
    {
        if(t1 instanceof Triangle && t1 != null)
        {
            ArrayList<Point> ts= new ArrayList<>(List.of(((Triangle) t1).getPoints()));
            return ts.contains(points[0]) && ts.contains(points[1]) && ts.contains(points[2]);
        }
        return false;
    }
    public boolean isAdjacent(Triangle t1)
    {
        int a = -1;
        int b = -1;
        int c = -1;
        if(t1 != null)
        {
            for (int i = 0; i < t1.points.length; i++)
            {
                if (points[0].equals(t1.points[i]))
                {
                    a = i;
                } else if (points[1].equals(t1.points[i]))
                {
                    b = i;
                } else if (points[2].equals(t1.points[i]))
                {
                    c = i;
                }
            }
            return (a >= 0 && b >= 0 && c == -1) || (a >= 0 && b == -1 && c >= 0) || (a == -1 && b >= 0 && c >= 0);
        }
        return false;
        /*return t1 != null &&
                ((points[0].equals(t1.points[0]) && points[1].equals(t1.points[1]) && !points[2].equals(t1.points[2])) ||
                (points[0].equals(t1.points[0]) && !points[1].equals(t1.points[1]) && points[2].equals(t1.points[2])) ||
                (!points[0].equals(t1.points[0]) && points[1].equals(t1.points[1]) && points[2].equals(t1.points[2])));*/
    }
    public Point[] getOpposite(Point p)
    {
        Point ans = null;
        ArrayList<Point> edge = new ArrayList<>(List.of(points).stream().filter(a-> !a.equals(p)).collect(Collectors.toList()));
        for(Triangle triangle: triangles)
        {
            if(triangle!=null && triangle.contains(edge.get(0)) && triangle.contains(edge.get(1)))
            {
                ArrayList<Point> adjacentPoints = new ArrayList<>(List.of(triangle.getPoints()));
                adjacentPoints.removeAll(edge);
                ans = adjacentPoints.get(0);
            }
        }
        return ans != null ? new Point[]{ans,edge.get(0),edge.get(1)} : null;
    }
    public boolean contains(Point point)
    {
        return points[0].equals(point) || points[1].equals(point) || points[2].equals(point);
    }
    private boolean ccw () {
        return (points[1].getX() - points[0].getX())*(points[2].getY() - points[0].getY())-(points[2].getX() - points[0].getX())*(points[1].getY() - points[0].getY()) > 0;
    }
    public void reorder()
    {
        Point center = new Point((points[0].getX()+points[1].getX()+points[2].getX())/3,(points[0].getY()+points[1].getY()+points[2].getY())/3);
        ArrayList<Point> ps = new ArrayList<>(List.of(points).stream().sorted((p1,p2) -> {
            double a1 = (Math.toDegrees(Math.atan2(p1.getX() - center.getX(), p1.getY() - center.getY())) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(p2.getX() - center.getX(), p2.getY() - center.getY())) + 360) % 360;
            return (int) (a1-a2);
        }).collect(Collectors.toList()));

        points = new Point[]{ps.get(0),ps.get(1),ps.get(2)};
    }
    public Point[] getHypotenuse()
    {
        double ab = points[0].getDistance(points[1]);
        double bc = points[1].getDistance(points[2]);
        double ca = points[2].getDistance(points[0]);
        double max = Math.max(ab,Math.max(bc,ca));
        Point[] hypotenuse = new Point[2];
        if(ab == max)
        {
            hypotenuse[0]= points[0];
            hypotenuse[1]= points[1];
        }
        else if(bc == max)
        {
            hypotenuse[0]= points[1];
            hypotenuse[1]= points[2];
        }
        else if(ca == max)
        {
            hypotenuse[0]= points[2];
            hypotenuse[1]= points[0];
        }
        return hypotenuse;
    }
    public Point[][] getEdges()
    {
        return new Point[][]{{points[0],points[1]},{points[1],points[2]},{points[2],points[0]}};
    }
    public Triangle[] getTriangles() {
        return triangles;
    }
    public Point[] getPoints() {
        return points;
    }

    public void setTriangles(Triangle[] triangles) {
        this.triangles = triangles;
    }

    public void setPoints(Point[] points) {
        this.points = points;
        reorder();
    }

    @Override
    public String toString() {
        return points[0] + " " + points[1] + " " + points[2];
    }
}
