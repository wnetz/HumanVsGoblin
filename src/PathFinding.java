import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class PathFinding
{
    private Node[][] grid;
    private boolean [][] closed;
    private Entity start;
    private Entity end;
    private  PriorityQueue<Node> queue;
    public  ArrayList<Land> getPath(Entity s, Entity e, ArrayList<Land> map)
    {
        int size = map.get(map.size()-1).getX();
        start = s;
        end = e;
        grid = new Node[size+1][size+1];
        closed = new boolean[size+1][size+1];
        queue = new PriorityQueue<>();//(Node p1, Node p2)-> p1.getTotalCost() < p2.getTotalCost()? -1 : p1.getTotalCost() < p2.getTotalCost()? 1:0);
        for(int i = 0; i < grid.length;i++)
        {
            for(int j = 0; j < grid[i].length;j++)
            {
                grid[j][i] = new Node(j,i, map.get(j + i*(size+1)).getMovement(), getHeuristic(j, i,end.getX(), end.getY(),size+1));
            }
        }
        grid[start.getX()][start.getY()].setPathCost(0);
        grid[start.getX()][start.getY()].setNodeCost(0);
        closed[start.getX()][start.getY()] = true;
        for(Land l: map)
        {
            if(l.occupied() && !(l.getX() == end.getX() && l.getY() == end.getY()) && !(l.getX() == start.getX() && l.getY() == start.getY()))
            {
                grid[l.getX()][l.getY()] = null;
            }
        }
        queue.add(grid[start.getX()][start.getY()]);
        Node current;
        boolean done = false;
        PrintStream o = null;
        while (!done)
        {
            current = queue.poll();
            if(current==null)
            {
                done = true;
            }
            else
            {
                int x = current.getX();
                int y = current.getY();
                closed[x][y] = true;
                if (current.equals(grid[end.getX()][end.getY()]))
                {
                    done = true;
                }
                else
                {
                    updateCost(current,grid[(x+1)%(size+1)][y]);
                    updateCost(current,grid[x][(y+1)%(size+1)]);
                    if (x == 0) {
                        updateCost(current,grid[size][y]);
                    } else//x!=0
                    {
                        updateCost(current,grid[x-1][y]);
                    }
                    if (y == 0) {
                        updateCost(current,grid[x][size]);
                    } else//y!=0
                    {
                        updateCost(current,grid[x][y-1]);
                    }
                }
            }

        }
        current = grid[end.getX()][end.getY()];
        ArrayList path = new ArrayList<>();
        while (current.getFrom() != null)
        {
            current=current.getFrom();
            path.add(0,map.get(current.getX() + current.getY()*(size+1)));
        }
        return path;
    }
    private  int getHeuristic(int x1, int y1, int x2, int y2, int size)
    {
        //account for the looping of the map
        double q1 = Math.abs(x1-x2)+Math.abs(y1-y2);//00
        double q2 = Math.abs(x1-(x2+size))+Math.abs(y1-y2);//-x
        double q3 = Math.abs(x1-x2)+Math.abs(y1-(y2+size));//-y
        double q4 = Math.abs(x1-(x2+size))+Math.abs(y1-(y2+size));//-x-y
        double q5 = Math.abs(x1-(x2-size))+Math.abs(y1-y2);//+x
        double q6 = Math.abs(x1-x2)+Math.abs(y1-(y2-size));//+y
        double q7 = Math.abs(x1-(x2-size))+Math.abs(y1-(y2-size));//+x+y
        double q8 = Math.abs(x1-(x2+size))+Math.abs(y1-(y2-size));//-x+y
        double q9 = Math.abs(x1-(x2-size))+Math.abs(y1-(y2+size));//+x-y
        return (int)Math.min(q1,Math.min(q2,Math.min(q3,Math.min(q4,Math.min(q5,Math.min(q6,Math.min(q7,Math.min(q8,q9))))))));
    }
    private void updateCost(Node current, Node t)
    {
        if(!(t==null || closed[t.getX()][t.getY()]))
        {
            int newTCost = t.getNodeCost() + current.getPathCost();
            boolean isOpen = queue.contains(t);
            if(!isOpen || newTCost < t.getPathCost())
            {
                t.setPathCost(current.getPathCost() + t.getNodeCost());
                t.setFrom(current);
            }
            if(!isOpen)
            {
                queue.add(t);
            }
        }
    }
    public void display()
    {
        for(int i = 0; i < grid.length;i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if(j == start.getX() && i == start.getY())
                {
                    System.out.print("SO ");
                }
                else if (j == end.getX() && i == end.getY())
                {
                    System.out.print("DE ");
                }
                else if(grid[j][i]!=null)
                {
                    System.out.printf("%-3d",0);
                }
                else
                {
                    System.out.print("BL ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    public void displayScores()
    {
        System.out.println("\nScores");
        for(int i = 0; i < grid.length;i++)
        {
            for (int j = 0; j < grid[i].length; j++)
            {
                if(j == start.getX() && i == start.getY())
                {
                    System.out.print("SO ");
                }
                else if (j == end.getX() && i == end.getY())
                {
                    System.out.print("DE ");
                }
                else if(grid[j][i]!=null)
                {
                    if(grid[j][i].getTotalCost() < 0)
                    {
                        System.out.print("#  ");
                    }
                    else
                    {
                        System.out.printf("%-3d",grid[j][i].getTotalCost());
                    }
                }
                else
                {
                    System.out.print("BL ");
                }
            }
            System.out.println("  " + i);
        }
        for(int i = 0; i < grid.length;i++)
        {
            System.out.printf("%-3d",i);
        }
        System.out.println();
    }
    public void displaySolution()
    {
        if(closed[end.getX()][end.getY()])
        {
            System.out.println("Path: ");
            Node current =grid[end.getX()][end.getY()];
            //System.out.println(current);
            grid[current.getX()][current.getY()].setSolution(true);
            while (current.getFrom()!=null)
            {
                current = current.getFrom();
                //System.out.println("->" + current);
                grid[current.getX()][current.getY()].setSolution(true);
            }
            System.out.println();

            for(int i = 0; i < grid.length;i++)
            {
                for (int j = 0; j < grid[i].length; j++)
                {
                    if(j == start.getX() && i == start.getY())
                    {
                        System.out.print("SO ");
                    }
                    else if (j == end.getX() && i == end.getY())
                    {
                        System.out.print("DE ");
                    }
                    else if(grid[j][i]!=null)
                    {
                        System.out.printf("%-3s",grid[j][i].isSolution() ? "X" : "0");
                    }
                    else
                    {
                        System.out.print("BL ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
        else
        {
            System.out.println("no path found");
        }
    }

}
