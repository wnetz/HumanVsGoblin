package Logic;

import GameObject.*;
import Logic.Pathing.Node;
import Logic.Pathing.PathFinding;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

import java.util.Random;

/**handles updating and drawing of all objects*/
public class Handler
{
    private int turn;
    protected int column, row;
    private int squareSize;
    public ArrayList<ActiveEntity> activeEntities = new ArrayList<>();
    public ArrayList<Land> availableMoves = new ArrayList<>();// make and object
    public ArrayList<Land> land = new ArrayList<>();
    private Mouselistener mouse;
    public PathFinding pathFinding;
    public Random rand;

    public Handler(Mouselistener m, int ss,int c, int r)
    {
        turn = 0;
        column = c;
        row = r;
        squareSize = ss;
        mouse = m;
        pathFinding = new PathFinding();
        rand = new Random();
    }

    public void tick()
    {
        land.forEach(l ->l.tick()); //update all land
        if(activeEntities.size() > 0)
        {
            activeEntities.get(turn).tick(land);
            for (int i = 0; i < activeEntities.size(); i++) {
                if (activeEntities.get(i).getHealth() <= 0 && activeEntities.get(i).getType() != ID.human)//kill active entity
                {
                    activeEntities.remove(i);
                    i--;
                }
            }
            if (activeEntities.get(turn).getType() == ID.goblin) {
                if (!((Goblin) activeEntities.get(turn)).getMoving()) {
                    goblinsTurn();
                }
            }
            if (activeEntities.get(turn).endTurn())//move down initiative
            {
                turn = (turn + 1) % activeEntities.size();
                System.out.println("initiative: " + turn);
            }
        }
    }
    public void render(Graphics g, int ss)
    {
        squareSize = ss;
        land.forEach(l -> l.render(g,ss));
        //availableMoves.forEach(m->m.render(g,ss));
        activeEntities.forEach(a -> a.render(g,ss));
    }

    /***
     *
     * @param activeEntity
     * @return true if e is successfully added false otherwise
     */
    public boolean addObject(@NotNull ActiveEntity activeEntity)
    {
        if(!land.get(activeEntity.getX()+activeEntity.getY()*column).occupied())//if no collision add object
        {
            activeEntities.add(activeEntity);
            land.get(activeEntity.getX()+activeEntity.getY()*(column)).setOccupiedBy(activeEntity);
            return true;
        }
        return false;
    }
    public void addLand(Land e)
    {
        land.add(e);
    }

    public void removeObject(Entity e)
    {
        activeEntities.remove(e);
    }

    private void goblinsTurn() //logic for a goblins turn
    {
        Human human = null;//needs to be checked
        Goblin goblin = (Goblin)activeEntities.get(turn);
        goblin.setNumber(turn);
        for(ActiveEntity e: activeEntities)
        {
            if(e.getType() == ID.human)
            {
                human = (Human)e;
                break;
            }
        }
        if(!goblin.nextTo(human))
        {
            ArrayList<Node> path;
            if (goblin.getPath() == null)
            {
                path = pathFinding.getPath(goblin, human, land,column,row,squareSize);
                if(path.size()>0)
                {
                    goblin.setPath(path);
                }


                //pathFinding.display();
                //pathFinding.displayScores();
                //pathFinding.displaySolution();
            }
            //System.out.println(goblin.getPath());
            //System.out.println(goblin);
        }
        else if(goblin.nextTo(human))
        {
            goblin.stab(human);
            System.out.println("human: " + human.getHealth());
        }

    }
    public ArrayList<Land> getLand()
    {
        return land;
    }
}
