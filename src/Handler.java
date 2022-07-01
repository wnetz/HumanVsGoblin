import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
/**handles updating and drawing of all objects*/
public class Handler
{
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    /**updates all objects */
    public void tick(ID gameState)
    {
        for(int i = 0; i < entities.size(); i++)//update all objects befor looking for collision
        {
            if(entities.get(i).getType() == gameState)
            {
                entities.get(i).tick();
            }
        }
    }
    public void render(Graphics g, double squareSize)
    {
        for(int i = 0; i < entities.size(); i++)
        {
            entities.get(i).render(g, squareSize);
        }
    }
    public void addObject(Entity e)
    {
        entities.add(e);
    }
    public void removeObject(Entity e)
    {
        entities.remove(e);
    }
}
