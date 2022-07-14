import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.Random;
import java.util.stream.Collectors;

/**handles updating and drawing of all objects*/
public class Handler
{
    private int turn;
    private double squareSize;
    public ArrayList<ActiveEntity> activeEntities = new ArrayList<>();
    public ArrayList<Land> availableMoves = new ArrayList<>();
    public ArrayList<Land> land = new ArrayList<>();
    private Mouselistener mouse;
    public Random rand;
    public PathFinding pathFinding;

    public Handler(Mouselistener m)
    {
        turn = 0;
        squareSize = 25;
        mouse = m;
        rand = new Random();
        pathFinding = new PathFinding();
    }
    public int tick()
    {
        for(int i = 0; i< activeEntities.size();i++)// kill dead entities
        {
            activeEntities.get(i).tick();
            if(activeEntities.get(i).getHealth() <= 0 && activeEntities.get(i).getType() != ID.human)
            {
                activeEntities.remove(i);
                i--;
            }
        }
        land.forEach(l ->{

            if(mouse.mouseOver(l))
            {
                l.setColor(Color.blue);
            }
            else
            {
                l.tick();
            }
        });
        if(activeEntities.get(turn).getType() == ID.human)
        {
            playersTurn();
        }
        if(activeEntities.get(turn).getType() == ID.goblin)
        {
            golinsTurn();
        }
        if(activeEntities.get(turn).endTurn())
        {
            turn = (turn+1)%activeEntities.size();
            System.out.println(turn + " " + activeEntities.size());
        }
        return 0;
    }
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        ArrayList<Land> temp = land.stream().collect(Collectors.toCollection(ArrayList<Land>::new));
        for(int i = 0; i<temp.size();i++)
        {
            if(!temp.get(i).occupied())
            {
                temp.get(i).render(g, squareSize);
                temp.remove(temp.get(i));
                i--;
            }
        }
        for(int i = 0; i<temp.size();i++)
        {
            if(temp.get(i).getOccupiedBy().getType() == ID.goblin)
            {
                temp.get(i).render(g, squareSize);
                temp.remove(temp.get(i));
                i--;
            }
        }
        temp.get(0).render(g, squareSize);
    }
    public boolean addObject(ActiveEntity e)
    {
        int size = land.get(land.size()-1).getX();
        if(!land.get(e.getX()+e.getY()*(size+1)).occupied())
        {
            activeEntities.add(e);
            land.get(e.getX()+e.getY()*(size+1)).setOccupiedBy(e);
            //System.out.println(land.get(e.getX()+e.getY()*(size+1)).getOccupiedBy().getType());
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
    private ArrayList<Land> getMovement(ActiveEntity ae, int size)//get the squares any ActiveEntity can move to
    {
        int x = ae.getX();
        int y = ae.getY();
        int movement = ae.getMovement();
        ArrayList<Land> moves = new ArrayList<>();
        moves.add(new Land(land.get(x+y*(size+1)).getX(),land.get(x+y*(size+1)).getY(),movement));
        if(movement != 0)
        {
            if (x == 0)
            {
                moves.addAll(getMovement(size, y, movement, size)); //x-1
                moves.addAll(getMovement(x + 1, y, movement, size)); //x+1
                if (y == 0)
                {
                    moves.addAll(getMovement(x, size, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
                else if (y == size)
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, 0, movement, size)); //y+1
                }
                else
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
            }
            else if (x == size)
            {
                moves.addAll(getMovement(x - 1, y, movement, size)); //x-1
                moves.addAll(getMovement(0, y, movement, size)); //x+1
                if (y == 0)
                {
                    moves.addAll(getMovement(x, size, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
                else if (y == size)
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, 0, movement, size)); //y+1
                }
                else
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
            }
            else
            {
                moves.addAll(getMovement(x - 1, y, movement, size)); //x-1
                moves.addAll(getMovement(x + 1, y, movement, size)); //x+1
                if (y == 0)
                {
                    moves.addAll(getMovement(x, size, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
                else if (y == size)
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, 0, movement, size)); //y+1
                }
                else
                {
                    moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                    moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                }
            }
        }
        ArrayList<Land> remove = new ArrayList<>();
        for(int i = 0; i<moves.size();i++)
        {
            moves.get(i).setSquareSize(land.get(0).getSquareSize());
            for(int j = i+1; j<moves.size();j++)
            {
                if(moves.get(i).getX() == moves.get(j).getX() && moves.get(i).getY() == moves.get(j).getY())
                {
                    if(moves.get(i).getMovement() > moves.get(j).getMovement())
                    {
                        remove.add(moves.get(j));
                    }
                    else
                    {
                        remove.add(moves.get(i));
                    }
                }
            }
        }
        moves.removeAll(remove);
        //System.out.println("end x:" + x + " y: " + y + " m:" + movement);
        //moves.forEach(e -> System.out.print(e + ", "));
        //System.out.println();
        return moves;
    }
    private ArrayList<Land> getMovement(int x, int y, int movement, int size)//recursive call
    {
        ArrayList<Land> moves = new ArrayList<>();
        if(movement >= land.get(x+y*(size+1)).getMovement() && !land.get(x+y*(size+1)).occupied())
        {
            movement -= land.get(x+y*(size+1)).getMovement();
            moves.add(new Land(land.get(x+y*(size+1)).getX(),land.get(x+y*(size+1)).getY(),movement));
            if(movement != 0)
            {
                if (x == 0)
                {
                    moves.addAll(getMovement(size, y, movement, size)); //x-1
                    moves.addAll(getMovement(x + 1, y, movement, size)); //x+1
                    if (y == 0)
                    {
                        moves.addAll(getMovement(x, size, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                    else if (y == size)
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, 0, movement, size)); //y+1
                    }
                    else
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                }
                else if (x == size)
                {
                    moves.addAll(getMovement(x - 1, y, movement, size)); //x-1
                    moves.addAll(getMovement(0, y, movement, size)); //x+1
                    if (y == 0)
                    {
                        moves.addAll(getMovement(x, size, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                    else if (y == size)
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, 0, movement, size)); //y+1
                    }
                    else
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                }
                else
                {
                    moves.addAll(getMovement(x - 1, y, movement, size)); //x-1
                    moves.addAll(getMovement(x + 1, y, movement, size)); //x+1
                    if (y == 0)
                    {
                        moves.addAll(getMovement(x, size, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                    else if (y == size)
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, 0, movement, size)); //y+1
                    }
                    else
                    {
                        moves.addAll(getMovement(x, y - 1, movement, size)); //y-1
                        moves.addAll(getMovement(x, y + 1, movement, size)); //y+1
                    }
                }
            }
        }
        return moves;
    }
    private void playersTurn()//logic for the players turn
    {
        Human human = (Human)activeEntities.get(turn);
        int size = land.get(land.size()-1).getX();
        availableMoves = getMovement(human,size);
        human.setAvailableMoves(availableMoves);
        availableMoves.forEach(l->l.setColor(Color.RED));
        if(mouse.getEventType() == 1)
        {
            availableMoves.forEach(l-> {
                if(mouse.mouseOver(l))
                {
                    land.get(human.getX()+human.getY()*(size+1)).setOccupiedBy(null);
                    human.setX(l.getX());
                    human.setY(l.getY());
                    human.setMovement(l.getMovement());
                    land.get(l.getX()+l.getY()*(size+1)).setOccupiedBy(human);
                }
            });
            land.forEach(l ->{
                if(mouse.mouseOver(l) && l.occupied())
                {
                    //System.out.println("("+l.getX()+ " "+l.getY()+")");
                    if(l.nextTo(human,size+1))
                    {
                        human.stab(l.getOccupiedBy());
                        if(l.getOccupiedBy().getHealth()<=0)
                        {
                            l.setOccupiedBy(null);
                        }
                    }
                }
            });
        }
    }
    private void golinsTurn() //logic for a goblins turn
    {
        Human human = new Human(0,0);
        Goblin goblin = (Goblin)activeEntities.get(turn);
        goblin.setNumber(turn);
        int size = land.get(land.size()-1).getX();
        for(ActiveEntity e: activeEntities)
        {
            if(e.getType() == ID.human)
            {
                human = (Human)e;
                break;
            }
        }
        if(!goblin.nextTo(human,size+1))
        {
            ArrayList<Land> path;
            if (goblin.getPath() == null)
            {
                path = pathFinding.getPath(goblin, human, land);
                if(path.size()>0)
                {
                    goblin.setPath(path);
                }


                //pathFinding.display();
                //pathFinding.displayScores();
                //pathFinding.displaySolution();}
            }
            //System.out.println(goblin.getPath());
            //System.out.println(goblin);
            goblin.tick();
        }
        else if(goblin.nextTo(human,size+1))
        {
            goblin.stab(human);
            System.out.println("human: " + human.getHealth());
        }

    }
}
