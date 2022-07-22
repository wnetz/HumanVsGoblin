import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {

    private static BufferedImage spriteSheet;
    private static final int TILE_SIZE = 16;

    public static BufferedImage loadSprite(String file)
    {
        BufferedImage sprite = null;

        try
        {
            sprite = ImageIO.read(new File("./src/sprites/" + file + ".png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return sprite;
    }

    public static BufferedImage getSprite(int x, int y)
    {

        if (spriteSheet == null)
        {
            spriteSheet = loadSprite("player");
        }

        return spriteSheet.getSubimage(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}
