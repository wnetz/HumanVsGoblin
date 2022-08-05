package Logic.mapping;

import GameObject.Land;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class DelaunayTriangulations
{
    private static boolean pointInTriangle(Point point, Triangle triangle)
    {
        Point[] trianglePoints = triangle.getPoints();
        double abc = (trianglePoints[0].getX()*(trianglePoints[1].getY()-trianglePoints[2].getY()) +
                trianglePoints[1].getX()*(trianglePoints[2].getY()-trianglePoints[0].getY()) +
                trianglePoints[2].getX()*(trianglePoints[0].getY()-trianglePoints[1].getY()))/2;
        double pbc = (point.getX()*(trianglePoints[1].getY()-trianglePoints[2].getY()) +
                trianglePoints[1].getX()*(trianglePoints[2].getY()-point.getY()) +
                trianglePoints[2].getX()*(point.getY()-trianglePoints[1].getY()))/2;
        double apc = (trianglePoints[0].getX()*(point.getY()-trianglePoints[2].getY()) +
                point.getX()*(trianglePoints[2].getY()-trianglePoints[0].getY()) +
                trianglePoints[2].getX()*(trianglePoints[0].getY()-point.getY()))/2;
        double abp = (trianglePoints[0].getX()*(trianglePoints[1].getY()-point.getY()) +
                trianglePoints[1].getX()*(point.getY()-trianglePoints[0].getY()) +
                point.getX()*(trianglePoints[0].getY()-trianglePoints[1].getY()))/2;
        return abc == pbc + apc + abp;
        /*Point ab = new Point(trianglePoints[1].getX()-trianglePoints[0].getX(),trianglePoints[1].getY()-trianglePoints[0].getY());
        Point bc = new Point(trianglePoints[2].getX()-trianglePoints[1].getX(),trianglePoints[2].getY()-trianglePoints[1].getY());
        Point ca = new Point(trianglePoints[0].getX()-trianglePoints[2].getX(),trianglePoints[0].getY()-trianglePoints[2].getY());
        Point ap = new Point(point.getX()-trianglePoints[0].getX(),point.getY()-trianglePoints[0].getY());
        Point bp = new Point(point.getX()-trianglePoints[1].getX(),point.getY()-trianglePoints[1].getY());
        Point cp = new Point(point.getX()-trianglePoints[2].getX(),point.getY()-trianglePoints[2].getY());

        Point normal1 = new Point(ab.getX(),-1*ab.getY());
        Point normal2 = new Point(bc.getX(),-1*bc.getY());
        Point normal3 = new Point(ca.getX(), -1*ca.getY());

        double s1 = ap.getX()* normal1.getX() + ap.getY()* normal1.getY();
        double s2 = bp.getX()* normal2.getX() + bp.getY()* normal2.getY();
        double s3 = cp.getX()* normal3.getX() + cp.getY()* normal3.getY();

        double tolerance = .0001;

        if ((s1<0&&s2<0&&s3<0) || (s1<tolerance&&s2<0&&s3<0) || (s2<tolerance&&s1<0&&s3<0) || (s3<tolerance&&s1<0&&s2<0)) // inside triangle
        {
            return true;
        }
        else{
            return false;
        }*/
    }

    private static Triangle[] updateAdjacency(Triangle triangle, Triangle[] adjacent)
    {
        ArrayList<Triangle> adjacentTriangles = new ArrayList<>();
        for(Point[] edge : triangle.getEdges())
        {
            for(Triangle adj: adjacent)
            {
                if(adj != null && adj.equals(triangle) && adj.contains(edge[0])&& adj.contains(edge[1]))
                {
                    adjacentTriangles.add(adj);
                }
            }
        }
        triangle.setTriangles((Triangle[]) adjacentTriangles.toArray());
        return (Triangle[]) adjacentTriangles.toArray();
    }
    public static  void triangulate(ArrayList<Land> land)
    {
        ArrayList<Point> points = new ArrayList<>(land.stream().map(a-> new Point(a.getX(),a.getY(),a)).collect(Collectors.toList()));
        ArrayList<Triangle> triangles = new ArrayList<>();
        // find min and max boundaries of point cloud
        double xmin=0,xmax=0,ymin=0,ymax=0;
        for (int i = 0; i < points.size(); i++)
        {
            if (points.get(i).getX()>xmax) 
            {
                xmax = points.get(i).getX();
            }
            else if (points.get(i).getX()<xmin)
            {
                xmin = points.get(i).getX();
            }
            if (points.get(i).getY()>ymax)
            {
                ymax = points.get(i).getY();
            }
            else if (points.get(i).getY()<ymin)
            {
                ymin = points.get(i).getY();
            }
        }
        // remap everything (preserving the aspect ratio) to between (0,0)-(1,1)
        double height=ymax-ymin;
        double width=xmax-xmin;
        double d=Math.max(height,width); // d=largest dimension
        for (int i = 0; i < points.size(); i++)//transform domain and range to 0-1
        {
            points.get(i).setX((points.get(i).getX()-xmin)/d);
            points.get(i).setY((points.get(i).getY()-ymin)/d);
        }

        Point bigTriangle1 = new Point(-100,-100);
        Point bigTriangle2 = new Point(100,-100);
        Point bigTriangle3 = new Point(0,100);
        points.add(bigTriangle1);
        points.add(bigTriangle2);
        points.add(bigTriangle3);

        triangles.add(new Triangle(bigTriangle1,bigTriangle2,bigTriangle3));

        int currentTriangle = 1;
        Stack<Triangle> triangleStack = new Stack<>();

        for(int i = 0; i < points.size()-3;i++)
        {
            // find triangle that contains point
            int lastTriangle = currentTriangle -1;
            while (true)
            {
                Point point = points.get(i);
                Triangle triangle = triangles.get(lastTriangle);
                if(pointInTriangle(point,triangle))
                {
                    triangles.remove(triangle);
                    Point[] trianglePoints = triangles.get(currentTriangle).getPoints();
                    //new triangles
                    Triangle newTriangle1 = new Triangle(point,trianglePoints[0],trianglePoints[1]);
                    Triangle newTriangle2 = new Triangle(point,trianglePoints[1],trianglePoints[2]);
                    Triangle newTriangle3 = new Triangle(point,trianglePoints[2],trianglePoints[0]);
                    //set know adjacency's
                    newTriangle1.setTriangles(new Triangle[]{null,newTriangle2,newTriangle3});
                    newTriangle2.setTriangles(new Triangle[]{null,newTriangle3,newTriangle1});
                    newTriangle3.setTriangles(new Triangle[]{null,newTriangle1,newTriangle2});
                    //add new triangles
                    triangles.add(newTriangle1);
                    triangles.add(newTriangle2);
                    triangles.add(newTriangle3);
                    currentTriangle += 2;

                    //update adjacency's
                    Triangle[] adjacentTriangles = triangle.getTriangles();
                    for(int t1 = 0; t1 < adjacentTriangles.length; t1++)
                    {
                        if (adjacentTriangles[t1] != null)
                        {
                            Triangle[] temp = adjacentTriangles[t1].getTriangles();
                            for (int t2 = 0; t2 < temp.length; t2++)
                            {
                                if (newTriangle1.isAdjacent(temp[t2]))
                                {
                                    temp[t2] = newTriangle1;
                                    Triangle[] triangle1 = newTriangle1.getTriangles();
                                    triangle1[0] = adjacentTriangles[t1];
                                    newTriangle1.setTriangles(triangle1);
                                }
                                if (newTriangle2.isAdjacent(temp[t2]))
                                {
                                    temp[t2] = newTriangle2;
                                    Triangle[] triangle2 = newTriangle2.getTriangles();
                                    triangle2[0] = adjacentTriangles[t1];
                                    newTriangle2.setTriangles(triangle2);
                                }
                                if (newTriangle3.isAdjacent(temp[t2]))
                                {
                                    temp[t2] = newTriangle3;
                                    Triangle[] triangle3 = newTriangle3.getTriangles();
                                    triangle3[0] = adjacentTriangles[t1];
                                    newTriangle3.setTriangles(triangle3);
                                }
                            }
                            adjacentTriangles[t1].setTriangles(temp);
                        }
                    }

                    if (newTriangle1.getTriangles()[0] != null)
                    {
                        triangleStack.push(newTriangle1);
                    }
                    if (newTriangle2.getTriangles()[0] != null)
                    {
                        triangleStack.push(newTriangle2);
                    }
                    if (newTriangle3.getTriangles()[0] != null)
                    {
                        triangleStack.push(newTriangle3);
                    }
                    while (triangleStack.size()>0)
                    {
                        Triangle stackTriangle = triangleStack.pop();
                        Point[] stackPoints = stackTriangle.getPoints();
                        for(Point p: stackPoints)
                        {
                            Point[] oppositePoints = stackTriangle.getOpposite(p);
                            Point oppositePoint = oppositePoints[0];

                            Triangle oppositeTriangle = List.of(stackTriangle.getTriangles()).stream().filter(a->a.equals(new Triangle(oppositePoints))).collect(Collectors.toList()).get(0);

                            ArrayList<Triangle> allAdjacent = new ArrayList<>(List.of(stackTriangle.getTriangles()));
                            allAdjacent.addAll(List.of(oppositeTriangle.getTriangles()));
                            allAdjacent.remove(stackTriangle);
                            allAdjacent.remove(oppositeTriangle);
                            allAdjacent = new ArrayList<>(allAdjacent.stream().filter(a->a!=null).collect(Collectors.toList()));

                            // check if opposite point is inside circle
                            double ax_ = stackPoints[0].getX()-oppositePoint.getX();
                            double ay_ = stackPoints[0].getY()-oppositePoint.getY();
                            double bx_ = stackPoints[1].getX()-oppositePoint.getX();
                            double by_ = stackPoints[1].getY()-oppositePoint.getY();
                            double cx_ = stackPoints[2].getX()-oppositePoint.getX();
                            double cy_ = stackPoints[2].getY()-oppositePoint.getY();
                            double inCircle = (ax_*ax_ + ay_*ay_) * (bx_*cy_-cx_*by_) -
                                    (bx_*bx_ + by_*by_) * (ax_*cy_-cx_*ay_) +
                                    (cx_*cx_ + cy_*cy_) * (ax_*by_-bx_*ay_);

                            if(inCircle >= 0)
                            {
                                Triangle opposite = List.of(stackTriangle.getTriangles()).stream().
                                        filter(a->a.equals(new Triangle(p,stackTriangle.getHypotenuse()[0],stackTriangle.getHypotenuse()[1])))
                                        .collect(Collectors.toList()).get(0);
                                Triangle swap1 = new Triangle(oppositePoint,p,oppositePoints[1]);
                                Triangle swap2 = new Triangle(oppositePoint,p,oppositePoints[2]);
                                allAdjacent.add(swap1);
                                allAdjacent.add(swap2);
                                swap1.setTriangles(updateAdjacency(swap1, (Triangle[]) allAdjacent.toArray()));
                                swap2.setTriangles(updateAdjacency(swap2, (Triangle[]) allAdjacent.toArray()));
                                //add new
                                triangles.add(swap1);
                                triangles.add(swap2);
                                triangleStack.push(swap1);
                                triangleStack.push(swap2);

                                //remove old
                                triangles.remove(stackTriangle);
                                triangles.remove(opposite);
                                triangleStack.remove(opposite);
                                break;
                            }
                        }

                        /*double cosa = (stackPoints[0].getX()-stackPoints[2].getX())*(stackPoints[1].getX()-stackPoints[2].getX())+(stackPoints[0].getY()-stackPoints[2].getY())*(stackPoints[1].getY()-stackPoints[2].getY());
                        double cosb = (stackPoints[1].getX()-p.getX()             )*(stackPoints[0].getX()-p.getX()             )+(stackPoints[1].getY()-p.getY()             )*(stackPoints[0].getY()-p.getY());
                        double sina = (stackPoints[0].getX()-stackPoints[2].getX())*(stackPoints[1].getY()-stackPoints[2].getY())-(stackPoints[0].getY()-stackPoints[2].getY())*(stackPoints[1].getX()-stackPoints[2].getX());
                        double sinb = (stackPoints[1].getX()-p.getX()             )*(stackPoints[0].getY()-p.getY()             )-(stackPoints[1].getY()-p.getY()             )*(stackPoints[0].getX()-p.getX());

                        if (((cosa<0)&&(cosb<0))||(-cosa*sinb)>(cosb*sina))
                        {
                        }*/

                    }
                }
            }
        }
    }
}
