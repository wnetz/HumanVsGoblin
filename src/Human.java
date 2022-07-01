import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Human extends Entity implements activeEntity
{
    private BufferedImage p;
    public Human(int x,int y)
    {
        super(5, 2,x,y);
        type = ID.human;
        try
        {
            p = ImageIO.read(new File("./src/sprites/player.png"));
        }
        catch (IOException e)
        {

        }
    }
    public void turn(ID id)
    {

    }
    public void move(ID id)
    {
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
    public int attack() {
        return 0;
    }
    public void render(Graphics g, double squareSize)
    {
        g.drawImage(p,(int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize,null);
        //g.fillRect((int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize);
    }
}
