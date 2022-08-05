package Logic.Pathing;

import GameObject.ActiveEntity;
import GameObject.Entity;
import GameObject.Land;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class PathFinding
{
    private boolean [][] closed;
    private Entity end;
    private Entity start;
    private Node[][] grid;
    private  PriorityQueue<Node> queue;

    /***
     * @param s
     * @param e
     * @param map
     * @return get the shortest path between two entities
     */
    public  ArrayList<Node> getPath(Entity s, Entity e, ArrayList<Land> map, int columns, int rows, int squareSize)
    {
        closed = new boolean[columns][rows];
        end = e;
        start = s;
        grid = new Node[columns][rows];
        queue = new PriorityQueue<>();

        for(int i = 0; i < grid.length;i++)//fill grid
        {
            for(int j = 0; j < grid[i].length;j++)
            {
                grid[j][i] = new Node(j,i,columns,rows,squareSize, map.get(j + i*columns).getMovement(), getHeuristic(j, i,end.getX(), end.getY(),columns,rows));
            }
        }
        grid[start.getX()][start.getY()].setPathCost(0);
        grid[start.getX()][start.getY()].setNodeCost(0);
        closed[start.getX()][start.getY()] = true;

        for(Land l: map)//block occupied land that is not start or end
        {
            if(l.occupied() && !(l.getX() == end.getX() && l.getY() == end.getY()) && !(l.getX() == start.getX() && l.getY() == start.getY()))
            {
                grid[l.getX()][l.getY()] = null;
            }
        }
        queue.add(grid[start.getX()][start.getY()]);
        Node current;
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
                    updateCost(current,grid[(x+1)%columns][y]);
                    updateCost(current,grid[x][(y+1)%rows]);
                    if (x == 0)
                        updateCost(current,grid[columns-1][y]);
                    else//x!=0
                        updateCost(current,grid[x-1][y]);

                    if (y == 0)
                        updateCost(current,grid[x][rows-1]);
                    else//y!=0
                        updateCost(current,grid[x][y-1]);
                }
            }

        }

        current = grid[end.getX()][end.getY()];
        ArrayList path = new ArrayList<>();
        path.add(0,current);
        while (current.getFrom() != null)
        {
            current=current.getFrom();
            path.add(0,current);
        }
        System.out.println(path);
        return path;
    }
    private  int getHeuristic(int x1, int y1, int x2, int y2, int columns, int rows)
    {
        //account for the looping of the map
        double q1 = Math.abs(x1-x2)+Math.abs(y1-y2);//00
        double q2 = Math.abs(x1-(x2+columns))+Math.abs(y1-y2);//-x
        double q3 = Math.abs(x1-x2)+Math.abs(y1-(y2+rows));//-y
        double q4 = Math.abs(x1-(x2+columns))+Math.abs(y1-(y2+rows));//-x-y
        double q5 = Math.abs(x1-(x2-columns))+Math.abs(y1-y2);//+x
        double q6 = Math.abs(x1-x2)+Math.abs(y1-(y2-rows));//+y
        double q7 = Math.abs(x1-(x2-columns))+Math.abs(y1-(y2-rows));//+x+y
        double q8 = Math.abs(x1-(x2+columns))+Math.abs(y1-(y2-rows));//-x+y
        double q9 = Math.abs(x1-(x2-columns))+Math.abs(y1-(y2+rows));//+x-y
        return (int)Math.min(q1,Math.min(q2,Math.min(q3,Math.min(q4,Math.min(q5,Math.min(q6,Math.min(q7,Math.min(q8,q9))))))));
    }
    public static double getdistance(int x1, int y1, int x2, int y2, int columns, int rows)
    {

        double pow = Math.pow(x1 - (x2 + columns), 2);
        double pow1 = Math.pow(y1 - (y2 + rows), 2);
        double pow2 = Math.pow(x1 - (x2 - columns), 2);
        double pow3 = Math.pow(y1 - (y2 - rows), 2);
        //account for the looping of the map
        double q1 = Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));//00
        double q2 = Math.sqrt(pow +Math.pow(y1-y2,2));//-x
        double q3 = Math.sqrt(Math.pow(x1-x2,2)+ pow1);//-y
        double q4 = Math.sqrt(pow + pow1);//-x-y
        double q5 = Math.sqrt(pow2 +Math.pow(y1-y2,2));//+x
        double q6 = Math.sqrt(Math.pow(x1-x2,2)+ pow3);//+y
        double q7 = Math.sqrt(pow2 + pow3);//+x+y
        double q8 = Math.sqrt(pow + pow3);//-x+y
        double q9 = Math.sqrt(pow2 + pow1);//+x-y
        return Math.min(q1,Math.min(q2,Math.min(q3,Math.min(q4,Math.min(q5,Math.min(q6,Math.min(q7,Math.min(q8,q9))))))));
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

    /***
     *
     * @param ae
     * @param land
     * @param columns
     * @param rows
     * @return get all spaces an active entity can move to
     */
    public @NotNull ArrayList<Node> getMovement(@NotNull ActiveEntity ae, @NotNull ArrayList<Land> land, int columns, int rows, int size)//get the squares any GameObject.ActiveEntity can move to
    {
        int x = ae.getX();
        int y = ae.getY();
        int movement = ae.getMovement();
        ArrayList<Node> moves = new ArrayList<>();
        moves.add(new Node(land.get(x+y*columns).getX(),land.get(x+y*columns).getY(),columns,rows,size,movement,0));

        if(movement != 0)//if active entity still has movement check all directors
        {
            moves.addAll(getMovement((x + 1)%columns, y, movement,land,columns,rows,size)); //x+1
            moves.addAll(getMovement(x, (y + 1)%rows, movement,land,columns,rows,size)); //y+1
            if (x == 0)
                moves.addAll(getMovement(columns-1, y, movement,land,columns,rows,size)); //x-1
            else
                moves.addAll(getMovement(x - 1, y, movement,land,columns,rows,size)); //x-1

            if (y == 0)
                moves.addAll(getMovement(x, rows-1, movement,land,columns,rows,size)); //y-1
            else
                moves.addAll(getMovement(x, y - 1, movement,land,columns,rows,size)); //y-1
        }


        for(int i = 0; i<moves.size();i++)//remove any duplicates;
        {
            moves.get(i).setSquareSize(land.get(0).getSquareSize());
            for(int j = i+1; j<moves.size();j++)
            {
                if(moves.get(i).getX() == moves.get(j).getX() && moves.get(i).getY() == moves.get(j).getY())
                {
                    if(moves.get(i).getMovement() > moves.get(j).getMovement())
                    {
                        moves.remove(j);
                        j--;
                    }
                    else
                    {
                        moves.remove(i);
                        i--;
                    }
                }
            }
        }
        return moves;
    }
    private @NotNull ArrayList<Node> getMovement(int x, int y, int movement,@NotNull ArrayList<Land> land, int columns, int rows, int size)//recursive call
    {
        ArrayList<Node> moves = new ArrayList<>();
        if(movement >= land.get(x+y*columns).getMovement() && !land.get(x+y*columns).occupied())// if active entity can move into space
        {
            movement -= land.get(x+y*columns).getMovement();//update movement left
            moves.add(new Node(land.get(x+y*columns).getX(),land.get(x+y*columns).getY(),columns,rows,size,movement,0));

            if(movement != 0)//if active entity still has movement check all directors
            {
                moves.addAll(getMovement((x + 1)%columns, y, movement,land,columns,rows,size)); //x+1
                moves.addAll(getMovement(x, (y + 1)%rows, movement,land,columns,rows,size)); //y+1
                if (x == 0)
                    moves.addAll(getMovement(columns-1, y, movement,land,columns,rows,size)); //x-1
                else
                    moves.addAll(getMovement(x - 1, y, movement,land,columns,rows,size)); //x-1

                if (y == 0)
                    moves.addAll(getMovement(x, rows-1, movement,land,columns,rows,size)); //y-1
                else
                    moves.addAll(getMovement(x, y - 1, movement,land,columns,rows,size)); //y-1
            }
        }
        return moves;
    }

}
