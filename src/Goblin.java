import java.awt.*;
import java.sql.Struct;
import java.util.ArrayList;

public class Goblin extends ActiveEntity
{
    private final  int MOVEMENT = 3;
    private int number;
    private boolean attacked,moving;
    private double speed;
    private ArrayList<Land> path;
    public Goblin(int x,int y) {
        super(2, 2,x,y);
        movement = MOVEMENT;
        damage = 1;
        type = ID.goblin;
        attacked = false;
        number = 0;
        path = null;
        moving = false;
        speed = .5;
    }
    @Override
    public void tick() {
        if(path != null && path.size() > 1 && !moving && movement >= path.get(1).getMovement())
        {
            path.get(0).setOccupiedBy(null);
            path.remove(0);
            path.get(0).setOccupiedBy(this);
            moving=true;

        }
        if(moving)
        {
            if (path.get(0).getX() == 0 && x >= 24)
                x+=speed;
            else if(path.get(0).getX() == 24 && x <= 0)
                x-=speed;
            else if(path.get(0).getY() == 0 && y >= 24)
                y+=speed;
            else if(path.get(0).getY() == 24 && y <= 0)
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
                x = 25;
            else if((int)x >= 25)
                x = -1;
            else if((int)y <= -1)
                y = 25;
            else if((int)y >= 25)
                y = -1;

        }
    }

    @Override
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        g.setColor(Color.yellow);
        g.fillRect((int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize);
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

    public void setPath(ArrayList<Land> path) {
        this.path = path;
    }

    public ArrayList<Land> getPath() {
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
