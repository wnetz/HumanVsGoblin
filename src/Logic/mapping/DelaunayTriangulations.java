package Logic.mapping;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

public class DelaunayTriangulations
{
    static ArrayList<Point> points;
    static ArrayList<Triangle> triangles;

    private static boolean pointInTriangle(Point point, Triangle triangle)
    {
        Point[] trianglePoints = triangle.getPoints();
        Point v0 = trianglePoints[0];
        Point b = trianglePoints[1];
        Point c = trianglePoints[2];
        Point v1 = new Point(b.getX()-v0.getX(),b.getY()-v0.getY());
        Point v2 = new Point(c.getX()-v0.getX(),c.getY()-v0.getY());
        Point v = point;
        double s1 = (v.det(v2)-v0.det(v2))/(double)v1.det(v2);
        double s2 = -1*(v.det(v1)-v0.det(v1))/(double)v1.det(v2);
        return s1 > 0 && s2 > 0 && s1+s2 < 1;

    }
    private static boolean inCircle(Point point, Point oppositePoint, Point[]edge)
    {
        // check if opposite point is inside circle
        double ab = edge[0].getDistance(point);
        double ac = edge[0].getDistance(edge[1]);
        double ap = edge[0].getDistance(oppositePoint);
        double bc = point.getDistance(edge[1]);
        double bp = point.getDistance(oppositePoint);
        double cp = edge[1].getDistance(oppositePoint);
        double a1 = Math.acos((ab*ab + ac*ac - bc*bc)/(2*ab*ac));
        double a2 = Math.acos((ab*ab + bp*bp - ap*ap)/(2*ab*bp));
        double a3 = Math.acos((bc*bc + bp*bp - cp*cp)/(2*bc*bp));
        double a4 = Math.acos((ac*ac + bc*bc - ab*ab)/(2*ac*bc));
        double a5 = Math.acos((ac*ac + cp*cp - ap*ap)/(2*ac*cp));
        double a6 = Math.acos((cp*cp + bp*bp - bc*bc)/(2*cp*bp));
        double a7 = Math.acos((ap*ap + bp*bp - ab*ab)/(2*ap*bp));
        double a8 = Math.acos((ac*ac + ap*ap - cp*cp)/(2*ac*ap));
        double alpha = Math.max(a1,Math.max(a4,Math.max(a5,a8)));
        double beta = Math.max(a2,Math.max(a3,Math.max(a6,a7)));
        return alpha < beta;
    }
    private static Triangle updateAdjacency(Triangle triangle, ArrayList<Triangle> adjacent, ArrayList<Triangle> old)
    {
        //get all adjacent for new triangle
        adjacent = new ArrayList<>(adjacent.stream().filter(a->a.isAdjacent(triangle)).collect(Collectors.toList()));
        //update triangle
        triangle.setTriangles(adjacent.toArray(new Triangle[3]));
        //update adjacent to have new triangle instead of old triangles
        for(Triangle adj: adjacent)
        {
            ArrayList<Triangle> adjacentAdjacent = new ArrayList<>();
            Triangle[] temp = adj.getTriangles();
            for(int i = 0; i < temp.length;i++)
            {
                //get all non null non old triangles
                if(temp[i] != null && !old.contains(temp[i]))
                {
                    adjacentAdjacent.add(temp[i]);
                }
            }
            //add new triangle
            if (!adjacentAdjacent.contains(triangle))
            {
                adjacentAdjacent.add(triangle);
            }
            //update adjacent
            adj.setTriangles(adjacentAdjacent.toArray(new Triangle[3]));
        }
        return triangle;
    }
    public static  ArrayList<Triangle> triangulate(ArrayList<Point> map, int maxX, int maxY)
    {
        points = map;
        triangles = new ArrayList<>();
        // find min and max boundaries of point cloud
        /*double xmin=0,xmax=0,ymin=0,ymax=0;
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
        }*/

        Point bigTriangle1 = new Point(-1,-1);
        Point bigTriangle2 = new Point(maxX+maxY,0);
        Point bigTriangle3 = new Point(0,(maxX+maxY));
        points.add(bigTriangle1);
        points.add(bigTriangle2);
        points.add(bigTriangle3);

        triangles.add(new Triangle(bigTriangle1,bigTriangle2,bigTriangle3));

        Stack<Triangle> triangleStack = new Stack<>();

        for(int i = 0; i < points.size()-3;i++)
        {
            // find triangle that contains point
            for (int j = 0; j < triangles.size(); j++)
            {
                Point point = points.get(i);
                Triangle triangle = triangles.get(j);
                if(pointInTriangle(point,triangle))
                {

                    ArrayList<Triangle> allAdjacent = new ArrayList<>();
                    ArrayList<Triangle> oldTriangles = new ArrayList<>();
                    
                    triangles.remove(triangle);
                    oldTriangles.add(triangle);
                    
                    Point[] trianglePoints = triangle.getPoints();
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

                    allAdjacent.add(newTriangle1);
                    allAdjacent.add(newTriangle2);
                    allAdjacent.add(newTriangle3);


                    for(int k = 0; k < triangle.getTriangles().length ; k++)
                    {
                        if(triangle.getTriangles()[k] != null)
                        {
                            allAdjacent.add(triangle.getTriangles()[k]);
                        }
                    }
                    newTriangle1 = updateAdjacency(newTriangle1,allAdjacent,oldTriangles);
                    newTriangle2 = updateAdjacency(newTriangle2,allAdjacent,oldTriangles);
                    newTriangle3 = updateAdjacency(newTriangle3,allAdjacent,oldTriangles);

                    if (newTriangle1.getTriangles()[2] != null)
                    {
                        triangleStack.push(newTriangle1);
                    }
                    if (newTriangle2.getTriangles()[2] != null)
                    {
                        triangleStack.push(newTriangle2);
                    }
                    if (newTriangle3.getTriangles()[2] != null)
                    {
                        triangleStack.push(newTriangle3);
                    }
                    while (triangleStack.size()>0)
                    {
                        triangle = triangleStack.pop();
                        trianglePoints = triangle.getPoints();

                        for(Point p: trianglePoints)
                        {
                            Point[] oppositePoints = triangle.getOpposite(p);
                            if(oppositePoints != null)
                            {
                                Point oppositePoint = oppositePoints[0];

                                Triangle oppositeTriangle = null;
                                Triangle[] temp = triangle.getTriangles();
                                for (int k = 0; k < temp.length; k++)
                                {
                                    if(temp[k] != null)
                                    {
                                        temp[k].reorder();
                                        if (temp[k].equals(new Triangle(oppositePoints)))
                                        {
                                            oppositeTriangle = temp[k];
                                        }
                                    }
                                }
                                //System.out.println(p + "|" + oppositePoint + "|" + triangle + "|" + oppositeTriangle);
                                allAdjacent = new ArrayList<>();
                                for (int k = 0; k < triangle.getTriangles().length; k++)
                                {
                                    if (triangle.getTriangles()[k] != null && !triangle.getTriangles()[k].equals(oppositeTriangle))
                                    {
                                        allAdjacent.add(triangle.getTriangles()[k]);
                                    }
                                    if (oppositeTriangle.getTriangles()[k] != null && !oppositeTriangle.getTriangles()[k].equals(triangle))
                                    {
                                        allAdjacent.add(oppositeTriangle.getTriangles()[k]);
                                    }
                                }


                                if (inCircle(p, oppositePoint, new Point[]{oppositePoints[1], oppositePoints[2]}))
                                {
                                    Triangle swap1 = new Triangle(oppositePoint, p, oppositePoints[1]);
                                    Triangle swap2 = new Triangle(oppositePoint, p, oppositePoints[2]);

                                    swap1.setTriangles(new Triangle[]{swap2, null, null});
                                    swap2.setTriangles(new Triangle[]{swap1, null, null});

                                    allAdjacent.add(swap2);
                                    allAdjacent.add(swap1);

                                    oldTriangles = new ArrayList<>();
                                    oldTriangles.add(triangle);
                                    oldTriangles.add(oppositeTriangle);

                                    swap1 = updateAdjacency(swap1, allAdjacent, oldTriangles);
                                    swap2 = updateAdjacency(swap2, allAdjacent, oldTriangles);
                                    //add new
                                    triangles.add(swap1);
                                    triangles.add(swap2);

                                    triangleStack.push(swap1);
                                    triangleStack.push(swap2);
                                    //remove old
                                    triangles.removeAll(oldTriangles);
                                    triangleStack.remove(oppositeTriangle);
                                    break;
                                }
                            }
                        }

                        /*double cosa = (trianglePoints[0].getX()-trianglePoints[2].getX())*(trianglePoints[1].getX()-trianglePoints[2].getX())+(trianglePoints[0].getY()-trianglePoints[2].getY())*(trianglePoints[1].getY()-trianglePoints[2].getY());
                        double cosb = (trianglePoints[1].getX()-p.getX()             )*(trianglePoints[0].getX()-p.getX()             )+(trianglePoints[1].getY()-p.getY()             )*(trianglePoints[0].getY()-p.getY());
                        double sina = (trianglePoints[0].getX()-trianglePoints[2].getX())*(trianglePoints[1].getY()-trianglePoints[2].getY())-(trianglePoints[0].getY()-trianglePoints[2].getY())*(trianglePoints[1].getX()-trianglePoints[2].getX());
                        double sinb = (trianglePoints[1].getX()-p.getX()             )*(trianglePoints[0].getY()-p.getY()             )-(trianglePoints[1].getY()-p.getY()             )*(trianglePoints[0].getX()-p.getX());

                        if (((cosa<0)&&(cosb<0))||(-cosa*sinb)>(cosb*sina))
                        {
                        }*/

                    }
                    break;
                }
            }
        }
        for(int i = 0; i< triangles.size(); i++)
        {
            Triangle triangle = triangles.get(i);
            if(triangle.contains(bigTriangle1) || triangle.contains(bigTriangle2) || triangle.contains(bigTriangle3))
            {
                triangles.remove(triangle);
                i--;
                Triangle[] adjacent = triangle.getTriangles();
                for(int j = 0; j < adjacent.length; j++)
                {
                    if(adjacent[j] != null)
                    {
                        ArrayList<Triangle> adjacentAdjacent = new ArrayList<>();
                        Triangle[] temp = adjacent[j].getTriangles();
                        for(int k = 0; k < temp.length; k++)
                        {
                            adjacentAdjacent.add(temp[k]);
                        }
                        adjacentAdjacent.remove(triangle);
                        adjacent[j].setTriangles(adjacentAdjacent.toArray(new Triangle[3]));
                    }
                }

            }
        }
        return triangles;
    }
    public ArrayList<Triangle> step()
    {
        return triangles;
    }
}
