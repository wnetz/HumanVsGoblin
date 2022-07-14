import java.awt.*;

public class Goblin extends ActiveEntity
{
    private final  int MOVEMENT = 2;
    private boolean attacked;
    public Goblin(int x,int y) {
        super(2, 2,x,y);
        damage = 1;
        type = ID.goblin;
        attacked = false;
    }
    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        g.setColor(Color.yellow);
        g.fillRect((int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize);
    }
    public void stab(Entity e)
    {
        e.removeHealth(damage);
        attacked = true;
    }
    @Override
    public boolean endTurn()
    {
        if(availableMoves.size() <=0)
            movement = MOVEMENT;
        return true;
    }


}
