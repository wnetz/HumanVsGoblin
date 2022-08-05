package Logic.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Triangle
{
    private Point[] points = new Point[3];
    private Triangle triangles[] = new Triangle[3];
    public Triangle(Point p1,Point p2,Point p3)
    {
        points[0] = p1;
        points[1] = p2;
        points[2] = p3;

        triangles[0] = null;
        triangles[1] = null;
        triangles[2] = null;

        if(!ccw())
        {
            reorder();
        }
    }
    public Triangle(Point[] p1)
    {
        points[0] = p1[0];
        points[1] = p1[2];
        points[2] = p1[3];

        triangles[0] = null;
        triangles[1] = null;
        triangles[2] = null;

        if(!ccw())
        {
            reorder();
        }
    }
    @Override
    public boolean equals(Object t1)
    {
        return t1 instanceof Triangle ?
                points[0].equals(((Triangle)t1).points[0]) &&
                points[1].equals(((Triangle)t1).points[1]) &&
                points[2].equals(((Triangle)t1).points[2])
                : false;
    }
    public boolean isAdjacent(Triangle t1)
    {
        return (points[0].equals(t1.points[0]) && points[1].equals(t1.points[1]) && !points[2].equals(t1.points[2])) ||
                (points[0].equals(t1.points[0]) && !points[1].equals(t1.points[1]) && points[2].equals(t1.points[2])) ||
                (!points[0].equals(t1.points[0]) && points[1].equals(t1.points[1]) && points[2].equals(t1.points[2]));
    }
    public Point[] getOpposite(Point p)
    {
        Point ans = null;
        Point[] edge = (Point[]) List.of(points).stream().filter(a-> !a.equals(p)).toArray();
        for(Triangle triangle: triangles)
        {
            if(triangle!=null && triangle.contains(edge[0]) && triangle.contains(edge[1]))
            {
                ArrayList<Point> adjacentPoints = new ArrayList<>(List.of(triangle.getPoints()));
                adjacentPoints.removeAll(List.of(edge));
                ans = adjacentPoints.get(0);
            }
        }
        return new Point[]{ans,edge[0],edge[1]};
    }
    public boolean contains(Point point)
    {
        return points[0].equals(point) || points[1].equals(point) || points[2].equals(point);
    }
    private boolean ccw () {
        return (points[1].getX() - points[0].getX())*(points[2].getY() - points[0].getY())-(points[2].getX() - points[0].getX())*(points[1].getY() - points[0].getY()) > 0;
    }
    private void reorder()
    {
        double ab = points[0].getDistence(points[1]);
        double bc = points[1].getDistence(points[2]);
        double ca = points[2].getDistence(points[0]);
        Point a = null, center = new Point((points[0].getX()+points[1].getX()+points[2].getX())/3,(points[0].getY()+points[1].getY()+points[2].getY())/3);
        if(ab >= bc && ab >= ca)
        {
            a = points[2];
        }
        else if (bc >= ab && bc >= ca)
        {
            a = points[0];
        }
        else if (ca >= ab && ca >= bc)
        {
            a = points[1];
        }
        ArrayList<Point> ps = new ArrayList<>(List.of(points).stream().sorted((p1,p2) -> {
            double a1 = (Math.toDegrees(Math.atan2(p1.getX() - center.getX(), p1.getY() - center.getY())) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(p2.getX() - center.getX(), p2.getY() - center.getY())) + 360) % 360;
            return (int) (a1 - a2);
        }).collect(Collectors.toList()));
        /*while(!ps.get(0).equals(a))
        {
            ps.add(0,ps.get(ps.size()-1));
            ps.remove(ps.size()-1);
        }*/
        points = new Point[]{ps.get(0),ps.get(1),ps.get(2)};
    }
    public Point[] getHypotenuse()
    {
        double ab = points[0].getDistence(points[1]);
        double bc = points[1].getDistence(points[2]);
        double ca = points[2].getDistence(points[0]);
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
        if(!ccw())
        {
            reorder();
        }
    }
}
