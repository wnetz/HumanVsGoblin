package GameObject;

import java.awt.*;
import java.util.ArrayList;
import Graphics.Animation;
import Graphics.Frame;
import Graphics.SpriteSheet;
import Logic.Mouselistener;
import Logic.Pathing.Node;
import Logic.Pathing.PathFinding;

public class Human extends ActiveEntity
{
    private final int TILE_SIZE = 16;
    private final int MOVEMENT = 3;
    private int toX,toY;
    private boolean attacked,moving;
    private double speed;
    private ArrayList<Node> path;
    private Animation Left,Right,Up,Down,walkLeft,walkRight,walkUp,walkDown,animation;
    private Frame[] idleL,idleR,idleU,idleD,l,r,u,d;//used for animation
    private ID direction;
    private Mouselistener mouse;
    private SpriteSheet spriteSheet;
    public Human(int x, int y, Mouselistener m, int c, int ro,int s)
    {
        super(5, 3,x,y,c,ro,s);
        damage = 5;
        type = ID.human;
        attacked = false;
        moving = false;
        speed = .05;
        mouse = m;
        path = null;
        direction = ID.up;
        spriteSheet = new SpriteSheet();
        spriteSheet.loadSprite("player");
        idleL = new Frame[]{new Frame(spriteSheet.getSprite(1, 1), 1)};
        idleR = new Frame[]{new Frame(spriteSheet.getSprite(1, 0), 1)};
        idleU = new Frame[]{new Frame(spriteSheet.getSprite(0, 1), 1)};
        idleD = new Frame[]{new Frame(spriteSheet.getSprite(0, 0), 1)};
        l = new Frame[]{new Frame(spriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(spriteSheet.getSprite(2*2, 1), 5),//down
            new Frame(spriteSheet.getSprite(3*2, 1), 10),//down
            new Frame(spriteSheet.getSprite(4*2, 1), 5),//up
            new Frame(spriteSheet.getSprite(5*2, 1), 10),//contact
            new Frame(spriteSheet.getSprite(6*2, 1), 5),//down
            new Frame(spriteSheet.getSprite(7*2, 1), 10),//down
            new Frame(spriteSheet.getSprite(8*2, 1), 5)};//up
        r = new Frame[]{new Frame(spriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(spriteSheet.getSprite(2*2, 0), 5),//down
            new Frame(spriteSheet.getSprite(3*2, 0), 10),//down
            new Frame(spriteSheet.getSprite(4*2, 0), 5),//up
            new Frame(spriteSheet.getSprite(5*2, 0), 10),//contact
            new Frame(spriteSheet.getSprite(6*2, 0), 5),//down
            new Frame(spriteSheet.getSprite(7*2, 0), 10),//down
            new Frame(spriteSheet.getSprite(8*2, 0), 5)};//up
        u = new Frame[]{new Frame(spriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(spriteSheet.getSprite(2*2-1, 1), 5),//down
            new Frame(spriteSheet.getSprite(3*2-1, 1), 10),//down
            new Frame(spriteSheet.getSprite(4*2-1, 1), 5),//up
            new Frame(spriteSheet.getSprite(5*2-1, 1), 10),//contact
            new Frame(spriteSheet.getSprite(6*2-1, 1), 5),//down
            new Frame(spriteSheet.getSprite(7*2-1, 1), 10),//down
            new Frame(spriteSheet.getSprite(8*2-1, 1), 5)};//up
        d = new Frame[]{new Frame(spriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(spriteSheet.getSprite(2*2-1, 0), 5),//down
            new Frame(spriteSheet.getSprite(3*2-1, 0), 10),//down
            new Frame(spriteSheet.getSprite(4*2-1, 0), 5),//up
            new Frame(spriteSheet.getSprite(5*2-1, 0), 10),//contact
            new Frame(spriteSheet.getSprite(6*2-1, 0), 5),//down
            new Frame(spriteSheet.getSprite(7*2-1, 0), 10),//down
            new Frame(spriteSheet.getSprite(8*2-1, 0), 5)};//up
        Left = new Animation(idleL);
        Right = new Animation(idleR);
        Up = new Animation(idleU);
        Down = new Animation(idleD);
        walkLeft = new Animation(l);
        walkRight = new Animation(r);
        walkUp = new Animation(u);
        walkDown = new Animation(d);
        animation  = Down;
        animation.start();
    }
    public void tick(ArrayList<Land> land)
    {
        animation.update();
        if(path == null)
        {
            availableMoves = (new PathFinding()).getMovement(this,land,column,row,squareSize);
            if (mouse.getEventType() == 1)
            {
                availableMoves.forEach(l ->
                {
                    if (mouse.mouseOver(l))// set path to clicked space
                    {
                        PathFinding pathFinding = new PathFinding();
                        path = pathFinding.getPath(this, l, land,column,row,squareSize);
                        path.remove(0);
                        System.out.println(path);
                        land.get((int)(x+y*column)).setOccupiedBy(null);
                    }
                });
                land.forEach(l ->
                {
                    if (mouse.mouseOver(l) && l.occupied())//attack clicked square
                    {
                        if (l.nextTo(this))
                        {
                            stab(l.getOccupiedBy());
                        }
                    }
                });
            }
        }
        else if(!moving && path.size() > 0)
        {
            land.get(path.get(0).getX() + path.get(0).getY()*column).setOccupiedBy(this);
            direction  = getDirection(path.get(0));
            System.out.println(direction);
            animation = switch (direction)
            {
                case up -> walkUp;
                case left -> walkLeft;
                case right -> walkRight;
                case down -> walkDown;
                default -> walkUp;
            };
            animation.start();
            moving=true;
        }
        else if(!moving && path.size() == 0)
        {
            System.out.println("--------------------------------------------------");
            path = null;
            animation.stop();
            animation = switch (direction)
            {
                case left -> Left;
                case right -> Right;
                case up -> Up;
                case down -> Down;
                default -> Up;
            };
            animation.start();
        }
        else if(moving)
        {
            if (path.get(0).getX() == 0 && x >= column-1)
                x+=speed;
            else if(path.get(0).getX() == column-1 && x <= 0)
                x-=speed;
            else if(path.get(0).getY() == 0 && y >= row-1)
                y+=speed;
            else if(path.get(0).getY() == row-1 && y <= 0)
                y-=speed;
            else if(x < path.get(0).getX())
                x+=speed;
            else if(x > path.get(0).getX())
                x-=speed;
            else if(y < path.get(0).getY())
                y+=speed;
            else if(y > path.get(0).getY())
                y-=speed;
            if(Math.max(path.get(0).getX()-speed/2,Math.min(x,path.get(0).getX()+speed/2)) == x && Math.max(path.get(0).getY()-speed/2,Math.min(y,path.get(0).getY()+speed/2)) == y)
            {
                setMovement(movement - path.get(0).getMovement());
                x = path.get(0).getX();
                y = path.get(0).getY();
                land.get(path.get(0).getX() + path.get(0).getY()*column).setOccupiedBy(null);
                path.remove(0);
                moving=false;
            }
            else if(x <= -1)
                x = column;
            else if(x >= column)
                x = -1;
            else if(y <= -1)
                y = row;
            else if((int)y >= row)
                y = -1;
        }
        else
        {
            System.out.println(path);
        }
    }


    @Override
    public void render(Graphics g, int ss)
    {
        squareSize = ss;
        availableMoves.forEach(l->l.render(g,ss));//draw all spaces human can move
        g.drawImage(animation.getSprite(),  (int) (x * squareSize), (int) (y * squareSize), squareSize,  squareSize, null);
    }
    public void stab(Entity e)
    {
        e.removeHealth(damage);
        attacked = true;
    }
    @Override
    public boolean endTurn()
    {
        boolean end = false;
        if(availableMoves.size() <= 1)
        {
            movement = MOVEMENT;
            attacked = false;
            end=true;
        }
        return end;
    }


    @Override
    public void tick() {

    }
}
