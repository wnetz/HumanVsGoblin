import java.awt.*;

abstract class Entity
{
    protected int health, movement;
    protected double x, y;
    protected double squareSize;
    protected ID type;
    public Entity(int h, int m, int x, int y)
    {
        health = h;
        movement = m;
        this.x = x;
        this.y = y;
        squareSize = 25;
        type = ID.entity;
    }
    abstract public void tick();
    abstract public void render(Graphics g, double ss);
    public boolean nextTo(Entity e, int gridSize)//test if two entities are next to each other
    {
        boolean nextto = false;
        if(((e.x+1)%gridSize == x || (x+1)%gridSize == e.x)&& e.y==y)
        {
            nextto = true;
        }
        else if(((e.y+1)%gridSize == y || (y+1)%gridSize == e.y)&& e.x==x)
        {
            nextto = true;
        }
        return nextto;
    }
    public void removeHealth(int damage)
    {
        health -= damage;
    }
    public int getHealth()
    {
        return health;
    }
    public int getMovement() {
        return movement;
    }
    public int getX() {
        return (int)x;
    }
    public int getY() {
        return (int)y;
    }
    public double getSquareSize() {
        return squareSize;
    }
    public ID getType()
    {
        return type;
    }
    public boolean setHealth(int h)
    {
        health = h;
        return health == 0;
    }
    public void setMovement(int movement) {
        this.movement = movement;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setSquareSize(double squareSize) {
        this.squareSize = squareSize;
    }

}
