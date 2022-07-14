public class PathPart
{
    private boolean solution;
    private int x,y;
    private int cost;
    private int heuristic;
    private PathPart from;

    public PathPart(int x, int y, int huristic) {
        this.solution = false;
        this.x = x;
        this.y = y;
        this.heuristic = huristic;
        cost = 0;
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
    public int getCost() {
        return cost + heuristic;
    }
    public int getHeuristic() {
        return heuristic;
    }
    public PathPart getFrom() {
        return from;
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
    public void setCost(int pathCost) {
        this.cost = pathCost;
    }
    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
    public void setFrom(PathPart from) {
        this.from = from;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof PathPart)
        {
            PathPart p = (PathPart)o;
            if(p.getX()==x && p.getY()==y)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
