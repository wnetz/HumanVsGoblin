package Logic.Pathing;

import GameObject.Entity;

import java.awt.*;

public class Node extends Entity implements Comparable
{
    private boolean solution;
    private int x,y;
    private int pathCost;
    private int heuristic;
    private Node from;

    public Node(int x, int y,int c,int r,int s, int pathCost, int huristic) {
        super(-1,pathCost,x,y,c,r,s);
        this.solution = false;
        this.x = x;
        this.y = y;
        this.pathCost = Integer.MAX_VALUE;
        this.heuristic = huristic;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g, int ss)
    {
        squareSize = ss;
        g.setColor(new Color(225,0,0,100));
        /*if(occupied())
        {
            g.setColor(Color.MAGENTA);
        }*/
        //g.drawImage(image, (int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize, null);
        g.fillRect((int) (x * squareSize), (int) (y * squareSize), squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawString("(" + (int)x + "," + (int)y + ")" + movement,(int) (x * squareSize), (int) (y * squareSize+squareSize/2));
    }

    public boolean isSolution() {
        return solution;
    }
    public int getPathCost() {
        return pathCost;
    }
    public int getHeuristic() {
        return heuristic;
    }
    public int getNodeCost() {
        return movement;
    }
    public Node getFrom() {
        return from;
    }
    public int getTotalCost()
    {
        return pathCost+heuristic;
    }
    public void setSolution(boolean solution) {
        this.solution = solution;
    }
    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    public void setNodeCost(int nodeCost) {
        setMovement(nodeCost);
    }
    public void setFrom(Node from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Node)
        {
            Node p = (Node)o;
            if(p.getX()==x && p.getY()==y && p.getMovement() == movement)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        //return "(" + x + "," + y + ") node: " + nodeCost + " path: "  + pathCost + " heuristic: "  + heuristic + " total: " + getTotalCost() + (from == null? "" : " from: (" + from.getX() + "," + from.getY() + ")");
        return "(" + x + "," + y + ") " + movement;

    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Node)
        {
            return getTotalCost() - ((Node)o).getTotalCost();
        }
        return 1;
    }
}
