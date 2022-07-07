import java.awt.*;
import java.util.ArrayList;

import java.util.Random;

/**handles updating and drawing of all objects*/
public class Handler
{
    public listener keyBoard;
    public ArrayList<ActiveEntity> activeEntities = new ArrayList<ActiveEntity>();
    public ArrayList<Entity> land = new ArrayList<Entity>();
    public Random rand;
    public Handler(listener kb)
    {
        keyBoard = kb;
        rand = new Random();
    }
    public int tick(ID gameState)
    {
        int rtn = 0;
        for(int i = 0; i < activeEntities.size(); i++)//update all objects befor looking for collision
        {
            if(activeEntities.get(i).getType() == gameState && gameState == ID.human)
            {
                if(activeEntities.get(i).getMovement() <= 0)
                {
                    activeEntities.get(i).endTurn();
                    rtn = 1;
                }
            }
            else if(activeEntities.get(i).getType() == gameState && gameState == ID.goblin)
            {
                activeEntities.get(i).tick(rand.nextInt(4)+37);
                rtn = 1;
            }
        }
        return rtn;
    }
    public void render(Graphics g, double squareSize)
    {
        for(int i = 0; i < land.size(); i++)
        {
            land.get(i).render(g, squareSize);
        }
        for(int i = 0; i < activeEntities.size(); i++)
        {
            activeEntities.get(i).render(g, squareSize);
        }
    }
    public void addObject(ActiveEntity e)
    {
        activeEntities.add(e);
    }
    public void addLand(Entity e)
    {
        land.add(e);
    }
    public void removeObject(Entity e)
    {
        activeEntities.remove(e);
    }
}
