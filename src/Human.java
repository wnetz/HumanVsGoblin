import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Human extends ActiveEntity
{

    private final int MOVEMENT = 5;
    private boolean attacked;
    private BufferedImage p;//used for sprite
    public Human(int x,int y)
    {
        super(5, 5,x,y);
        damage = 5;
        type = ID.human;
        attacked = false;
        try//read sprite
        {
            p = ImageIO.read(new File("./src/sprites/player.png"));
        }
        catch (IOException e)
        {

        }
    }
    @Override
    public void tick() {

    }
    @Override
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        availableMoves.forEach(l->l.render(g,ss));//draw all spaces human can move
        g.drawImage(p, (int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize, null);
    }
    public void stab(Entity e)
    {
        e.removeHealth(damage);
        attacked = true;
    }
    @Override
    public boolean endTurn()
    {
        boolean end = false;
        if(availableMoves.size() <= 1)
        {
            movement = MOVEMENT;
            attacked = false;
            end=true;
        }
        return end;
    }


}
