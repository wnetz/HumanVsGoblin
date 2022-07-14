public class Node implements Comparable
{
    private boolean solution;
    private int x,y;
    private int pathCost;
    private int heuristic;
    private int nodeCost;
    private Node from;

    public Node(int x, int y, int pathCost, int huristic) {
        this.solution = false;
        this.x = x;
        this.y = y;
        this.pathCost = Integer.MAX_VALUE;
        this.heuristic = huristic;
        nodeCost = pathCost;
    }

    public boolean isSolution() {
        return solution;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getPathCost() {
        return pathCost;
    }
    public int getHeuristic() {
        return heuristic;
    }
    public int getNodeCost() {
        return nodeCost;
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
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    public void setNodeCost(int nodeCost) {
        this.nodeCost = nodeCost;
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
            if(p.getX()==x && p.getY()==y)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        //return "(" + x + "," + y + ") node: " + nodeCost + " path: "  + pathCost + " heuristic: "  + heuristic + " total: " + getTotalCost() + (from == null? "" : " from: (" + from.getX() + "," + from.getY() + ")");
        return "(" + x + "," + y + ") " + getTotalCost();

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
