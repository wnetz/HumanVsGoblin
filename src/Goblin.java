import java.awt.*;
import java.util.ArrayList;

public class Goblin extends ActiveEntity
{
    private final  int MOVEMENT = 2;
    private int number;
    private boolean attacked;
    private ArrayList<Land> path;
    public Goblin(int x,int y) {
        super(2, 2,x,y);
        damage = 1;
        type = ID.goblin;
        attacked = false;
        number = 0;
        path = null;
    }
    @Override
    public void tick() {
        if(path != null && path.size() > 1)
        {
            path.get(0).setOccupiedBy(null);
            path.remove(0);
            x = path.get(0).getX();
            y = path.get(0).getY();
            setMovement(movement - path.get(0).getMovement());
            path.get(0).setOccupiedBy(this);
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

    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
