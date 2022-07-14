import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class PathFinding
{
    private PathPart[][] grid;
    private boolean [][] closed;
    private Entity start;
    private Entity end;
    private  PriorityQueue<PathPart> queue;
    public  ArrayList<Land> getPath(Entity s, Entity e, ArrayList<Land> map)
    {
        int size = map.get(map.size()-1).getX();
        start = s;
        end = e;
        grid = new PathPart[size+1][size+1];
        closed = new boolean[size+1][size+1];
        queue = new PriorityQueue<>((PathPart p1,PathPart p2)-> p1.getCost() < p2.getCost()? -1 : p1.getCost() < p2.getCost()? 1:0);
        for(int i = 0; i < grid.length;i++)
        {
            for(int j = 0; j < grid[i].length;j++)
            {
                grid[j][i] = new PathPart(j,i,getHeuristic(j, i,end.getX(), end.getY(),size+1));
            }
        }
        grid[start.getX()][start.getY()].setCost(0);
        for(Land l: map)
        {
            if(l.occupied() && l.getOccupiedBy().getType() != end.getType() && l.getX() != start.getX() && l.getY() != start.getY())
            {
                grid[l.getX()][l.getY()] = null;
            }
        }
        queue.add(grid[start.getX()][start.getY()]);
        PathPart current;
        boolean done = false;
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
                    if (x == 0) {
                        updateCost(current,grid[size][y],grid[size][y].getCost() + map.get(size+y*(size+1)).getMovement());
                        updateCost(current,grid[x+1][y],grid[x+1][y].getCost() + map.get((x+1)+y*(size+1)).getMovement());
                    } else//x!=0
                    {
                        updateCost(current,grid[x-1][y],grid[x-1][y].getCost() + map.get((x-1)+y*(size+1)).getMovement());
                        updateCost(current,grid[(x+1)%(size+1)][y],grid[(x+1)%(size+1)][y].getCost() + map.get(((x+1)%(size+1))+y*(size+1)).getMovement());
                    }
                    if (y == 0) {
                        updateCost(current,grid[x][size],grid[x][size].getCost() + map.get(x+size*(size+1)).getMovement());
                        updateCost(current,grid[x][y+1],grid[x][y+1].getCost() + map.get(x+(y+1)*(size+1)).getMovement());
                    } else//y!=0
                    {
                        updateCost(current,grid[x][y-1],grid[x][y-1].getCost() + map.get(x+(y-1)*(size+1)).getMovement());
                        updateCost(current,grid[x][(y+1)%(size+1)],grid[x][(y+1)%(size+1)].getCost() + map.get(x+((y+1)%(size+1))*(size+1)).getMovement());
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
    private void updateCost(PathPart current, PathPart t, int cost)
    {
        if(!(t==null || closed[t.getX()][t.getY()] ))
        {
            int tCost = t.getCost() + cost;
            boolean isOpen = queue.contains(t);
            if(!isOpen || tCost < t.getCost())
            {
                t.setCost(tCost);
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
                    System.out.print("BL   ");
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
                if(grid[j][i]!=null)
                {
                    System.out.printf("%-3d",grid[j][i].getCost());
                }
                else
                {
                    System.out.print("BL   ");
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
            PathPart current =grid[end.getX()][end.getY()];
            System.out.println(current);
            grid[current.getX()][current.getY()].setSolution(true);
            while (current.getFrom()!=null)
            {
                current = current.getFrom();
                System.out.println("->" + current);
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
                        System.out.print("BL   ");
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
