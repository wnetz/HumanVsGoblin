package GameObject;

import Logic.Pathing.Node;

import java.awt.*;
import java.util.ArrayList;

public class Goblin extends ActiveEntity
{
    private final  int MOVEMENT = 3;
    private int number;
    private boolean attacked,moving;
    private double speed;
    private ArrayList<Node> path;
    public Goblin(int x,int y, int c, int r, int s) {
        super(2, 2,x,y,c,r,s);
        movement = MOVEMENT;
        damage = 1;
        type = ID.goblin;
        attacked = false;
        number = 0;
        path = null;
        moving = false;
        speed = .2;
    }
    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g, int ss)
    {
        squareSize = ss;
        g.setColor(Color.yellow);
        g.fillRect((int) (x * squareSize), (int) (y * squareSize),  squareSize,  squareSize);
        g.setColor(Color.BLACK);
        g.drawString("(" + number + ")",(int) (x * squareSize), (int) (y * squareSize+squareSize/2));
    }
    public void stab(Entity e)
    {
        if(!attacked)
        {
            e.removeHealth(damage);
            attacked = true;
            movement = 0;
        }
    }

    @Override
    public void tick(ArrayList<Land> land) {
        if(path != null && path.size() > 1 && !moving && movement >= path.get(1).getMovement())
        {
            land.get(path.get(0).getX() + path.get(0).getY()*column).setOccupiedBy(null);
            path.remove(0);
            land.get(path.get(0).getX() + path.get(0).getY()*column).setOccupiedBy(this);
            moving=true;

        }
        if(moving)
        {
            if (path.get(0).getX() == 0 && x >= column-1)
                x+=speed;
            else if(path.get(0).getX() == column-1 && x <= 0)
                x-=speed;
            else if(path.get(0).getY() == 0 && y >= row-1)
                y+=speed;
            else if(path.get(0).getY() == column-1 && y <= 0)
                y-=speed;
            else if(x < path.get(0).getX())
                x+=speed;
            else if(x > path.get(0).getX())
                x-=speed;
            else if(y < path.get(0).getY())
                y+=speed;
            else if(y > path.get(0).getY())
                y-=speed;
            if(Math.max(path.get(0).getX()-speed/2,Math.min(x,path.get(0).getX()+speed/2)) == x && Math.max(path.get(0).getY()-speed/2,Math.min(y,path.get(0).getY()+speed/2)) == y)
            {
                setMovement(movement - path.get(0).getMovement());
                x = path.get(0).getX();
                y = path.get(0).getY();
                moving=false;
            }
            else if((int)x <= -1)
                x = column;
            else if((int)x >= column)
                x = -1;
            else if((int)y <= -1)
                y = row;
            else if((int)y >= row)
                y = -1;

        }
    }

    @Override
    public boolean endTurn()
    {
        if(path== null || path.size() <= 0 ||movement < path.get(0).getMovement())
        {
            movement = MOVEMENT;
            path = null;
            attacked = false;
            return true;
        }
        return false;
    }
    public void setNumber(int n)
    {
        number = n;
    }

    public void setPath(ArrayList<Node> path) {
        this.path = path;
    }

    public ArrayList<Node> getPath() {
        return path;
    }
    public boolean getMoving()
    {
        return moving;
    }
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
