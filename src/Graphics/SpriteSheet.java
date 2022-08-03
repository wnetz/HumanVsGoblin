package Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

    private BufferedImage spriteSheet;
    private final int TILE_SIZE = 16;

    public void loadSprite(String file)
    {
        spriteSheet = null;
        File in = new File("./src/Graphics/sprites/" + file + ".png");
        try
        {
            spriteSheet = ImageIO.read(in);
        } catch (IOException e)
        {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        }
    }

    public BufferedImage getSprite(int x, int y)
    {

        if (spriteSheet == null)
        {
            loadSprite("player");
        }

        return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
