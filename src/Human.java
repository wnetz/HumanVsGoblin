import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Human extends ActiveEntity
{
    private BufferedImage p;
    private final  int MOVEMENT = 2;
    public Human(int x,int y)
    {
        super(5, 2,x,y,1);
        type = ID.human;
        try
        {
            p = ImageIO.read(new File("./src/sprites/player.png"));
        }
        catch (IOException e)
        {

        }
    }
    public void move(ID id)
    {
        movement--;
        switch (id)
        {
            case west:
                if(x == 0)
                {
                    x = 24;
                }
                else
                {
                    x--;
                }
                break;
            case north:
                if(y == 0)
                {
                    y = 24;
                }
                else
                {
                    y--;
                }
                break;
            case south:
                if(y == 24)
                {
                    y=0;
                }
                else
                {
                    y++;
                }
                break;
            case east:
                if(x == 24)
                {
                    x = 0;
                }
                else
                {
                    x++;
                }
                break;
        }
    }
    public int attack(ID id)
    {
        attacks --;
        return 0;
    }

    @Override
    void tick(int key) {

    }
    @Override
    void endTurn()
    {
        movement = MOVEMENT;
    }
    public void render(Graphics g, double squareSize)
    {
        g.drawImage(p,(int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize,null);
        //g.fillRect((int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize);
    }
}
