import java.awt.*;

public class Land extends Entity
{
    private final Color base = Color.green;
    private Color color;
    private Entity occupiedBy;

    public Land(int x, int y, int movement)
    {
        super(-1,movement,x,y);
        this.x=x;
        this.y=y;
        color = base;
        occupiedBy = null;
    }
    @Override
    public void tick() {
        color = new Color(0,225/movement,0);
        if(occupied() && occupiedBy.getType() == ID.human && occupiedBy.getHealth()<=0)
        {
            occupiedBy.setHealth(10000);
        }
        else if(occupied() && occupiedBy.getHealth()<=0)
        {
            occupiedBy=null;
        }
    }
    @Override
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        g.setColor(color);
        if(occupied())
        {
            g.setColor(Color.MAGENTA);
        }
        g.fillRect((int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize);
        g.setColor(Color.BLACK);
        g.drawString("(" + x + "," + y + ")",(int) (x * squareSize), (int) (y * squareSize+squareSize/2));
        if(occupied())//render children if they exist
        {
            occupiedBy.render(g,ss);
        }
    }
    public boolean occupied()//determine if tile has a child
    {
        return occupiedBy != null;
    }
    public Entity getOccupiedBy() {
        return occupiedBy;
    }
    public void setColor(Color c) {
        color = c;
    }
    public void setOccupiedBy(Entity occupiedBy) {
        this.occupiedBy = occupiedBy;
    }
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
