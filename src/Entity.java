import java.awt.*;

public class Entity {
    protected int health;
    protected int movement;
    protected int x, y;
    protected ID type;
    public Entity(int h, int m, int x, int y)
    {
        health = h;
        movement = m;
        this.x = x;
        this.y = y;
        type = ID.entity;
    }
    public void tick()
    {

    }
    public void render(Graphics g, double squareSize)
    {
        g.setColor(Color.pink);
        g.fillRect((int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize);
    }
    public boolean setHealth(int h)
    {
        health = h;
        return health == 0;
    }
    public int getHealth()
    {
        return health;
    }
    public int getMovement()
    {
        return movement;
    }
    public ID getType()
    {
        return type;
    }
}
