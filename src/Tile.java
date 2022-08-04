import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Tile
{
    private BufferedImage image;
    private ArrayList<String> edges;
    private int index;

    public Tile(BufferedImage i, ArrayList<String> e, int in)
    {
        image = i;
        edges = e;
        index = in;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }
    public String getUp()
    {
        return edges.get(0);
    }
    public String getRight()
    {
        return edges.get(1);
    }
    public String getDown()
    {
        return edges.get(2);
    }
    public String getLeft()
    {
        return edges.get(3);
    }

    public ArrayList<String> getEdges()
    {
        return edges;
    }

    public void setEdges(ArrayList<String> edges)
    {
        this.edges = edges;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
