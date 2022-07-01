import java.awt.*;

public class Land extends Entity{
    private String displayChar;

    public Land(int x, int y)
    {
        super(-1,-1,x,y);
        this.x=x;
        this.y=y;
        displayChar = "\u25AF";
    }
    @Override
    public void render(Graphics g, double squareSize)
    {
        g.setColor(Color.green);
        g.fillRect((int)(x*squareSize),(int)(y*squareSize),(int)squareSize,(int)squareSize);
    }
}
