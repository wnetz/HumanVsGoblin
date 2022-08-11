package GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import Graphics.SpriteSheet;

public class Land extends Entity
{
    private final Color base = Color.green;
    private Color color;
    private Entity occupiedBy;
    private BufferedImage image;
    private SpriteSheet spriteSheet;

    public Land(int x, int y, int movement, int c, int r,int s)
    {
        super(-1,movement,x,y,c,r,s);
        this.x=x;
        this.y=y;
        color = base;
        occupiedBy = null;

        spriteSheet = new SpriteSheet();
        spriteSheet.loadSprite("land");
        Random rand = new Random();
        image = spriteSheet.getSprite(rand.nextInt(8),0);
    }
    @Override
    public void tick() {
        color = new Color(0,225/1,0);
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
    public void render(Graphics g, int ss)
    {
        squareSize = ss;
        g.setColor(color);
        /*if(occupied())
        {
            g.setColor(Color.MAGENTA);
        }*/
        g.drawImage(image, (int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize, null);
        //g.fillRect((int) (x * squareSize), (int) (y * squareSize), squareSize, squareSize);
        g.setColor(Color.BLACK);
        g.drawString("(" + (int)x + "," + (int)y + ")",(int) (x * squareSize), (int) (y * squareSize+squareSize/2));
        if(occupied())//render children if they exist
        {
            //occupiedBy.render(g,ss);
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
    public void setImage(BufferedImage i)
    {
        image = i;
    }
    @Override
    public String toString() {
        return "(" + x + "," + y  + ")" + movement;
    }
}
