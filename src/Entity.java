import java.awt.*;

abstract class Entity {
    protected int health;
    protected int x, y;
    protected ID type;
    public Entity(int h, int x, int y)
    {
        health = h;
        this.x = x;
        this.y = y;
        type = ID.entity;
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
    public ID getType()
    {
        return type;
    }
}
