package GameObject;

import java.awt.*;

public abstract class Entity
{
    protected int health, movement;
    protected int column, row;
    protected double x, y;
    protected int squareSize;
    protected ID type;
    public Entity(int h, int m, int x, int y,int c,int r, int s)
    {
        health = h;
        movement = m;
        this.x = x;
        this.y = y;
        column = c;
        row = r;
        squareSize = s;
        type = ID.entity;
    }
    abstract public void tick();
    abstract public void render(Graphics g, int ss);
    public boolean nextTo(Entity e)//test if two entities are next to each other
    {
        boolean nextto = false;
        if(((e.x+1)%column == x || (x+1)%column == e.x)&& e.y==y)
        {
            nextto = true;
        }
        else if(((e.y+1)%row == y || (y+1)%row == e.y)&& e.x==x)
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
    public ID getType()
    {
        return type;
    }
    public int getSquareSize() {
        return squareSize;
    }
    public ID getDirection(Entity e)
    {
        System.out.println("("+x+","+y+")("+e.getX()+","+e.getY()+")");
        ID direction = ID.human;
        if(x == e.getX())
        {
            if(y > e.getY())
            {
                if (y-e.getY() > (row*squareSize) / 2.0)
                {
                    direction = ID.right;//need to loop
                }
                else
                {
                    direction = ID.left;
                }
            }
            else
            {
                if (e.getY()-y > (row*squareSize) / 2.0)
                {
                    direction = ID.left;//need to loop
                }
                else
                {
                    direction = ID.right;
                }
            }
        }
        else
        {
            if(x > e.getX())
            {
                if (x-e.getX() > (column*squareSize) / 2.0)
                {
                    direction = ID.down;//need to loop
                }
                else
                {
                    direction = ID.up;
                }
            }
            else
            {
                if (e.getX()-x > (column*squareSize) / 2.0)
                {
                    direction = ID.up;//need to loop
                }
                else
                {
                    direction = ID.down;
                }
            }
        }
        return direction;
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
    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof  Entity)
        {
            return ((Entity) obj).x == x && ((Entity) obj).y == y;
        }
        return super.equals(obj);
    }
}
