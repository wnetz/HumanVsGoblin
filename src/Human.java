import java.awt.*;
import java.util.ArrayList;

public class Human extends ActiveEntity
{
    private final int TILE_SIZE = 16;
    private final int MOVEMENT = 5;
    private int toX,toY;
    private boolean attacked,moving;
    private double speed;
    private ArrayList<Land> path;
    private Animation Left,Right,Up,Down,walkLeft,walkRight,walkUp,walkDown,animation;
    private Frame[] idleL,idleR,idleU,idleD,l,r,u,d;//used for animation
    private ID direction;
    private Mouselistener mouse;
    public Human(int x,int y, Mouselistener m)
    {
        super(5, 5,x,y);
        damage = 5;
        type = ID.human;
        attacked = false;
        moving = false;
        speed = .05;
        mouse = m;
        path = null;
        direction = ID.up;
        SpriteSheet.loadSprite("player");
        idleL = new Frame[]{new Frame(SpriteSheet.getSprite(1, 1), 1)};
        idleR = new Frame[]{new Frame(SpriteSheet.getSprite(1, 0), 1)};
        idleU = new Frame[]{new Frame(SpriteSheet.getSprite(0, 1), 1)};
        idleD = new Frame[]{new Frame(SpriteSheet.getSprite(0, 0), 1)};
        l = new Frame[]{new Frame(SpriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(2*2, 1), 5),//down
            new Frame(SpriteSheet.getSprite(3*2, 1), 10),//down
            new Frame(SpriteSheet.getSprite(4*2, 1), 5),//up
            new Frame(SpriteSheet.getSprite(5*2, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(6*2, 1), 5),//down
            new Frame(SpriteSheet.getSprite(7*2, 1), 10),//down
            new Frame(SpriteSheet.getSprite(8*2, 1), 5)};//up
        r = new Frame[]{new Frame(SpriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(2*2, 0), 5),//down
            new Frame(SpriteSheet.getSprite(3*2, 0), 10),//down
            new Frame(SpriteSheet.getSprite(4*2, 0), 5),//up
            new Frame(SpriteSheet.getSprite(5*2, 0), 10),//contact
            new Frame(SpriteSheet.getSprite(6*2, 0), 5),//down
            new Frame(SpriteSheet.getSprite(7*2, 0), 10),//down
            new Frame(SpriteSheet.getSprite(8*2, 0), 5)};//up
        u = new Frame[]{new Frame(SpriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(2*2-1, 1), 5),//down
            new Frame(SpriteSheet.getSprite(3*2-1, 1), 10),//down
            new Frame(SpriteSheet.getSprite(4*2-1, 1), 5),//up
            new Frame(SpriteSheet.getSprite(5*2-1, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(6*2-1, 1), 5),//down
            new Frame(SpriteSheet.getSprite(7*2-1, 1), 10),//down
            new Frame(SpriteSheet.getSprite(8*2-1, 1), 5)};//up
        d = new Frame[]{new Frame(SpriteSheet.getSprite(1*2, 1), 10),//contact
            new Frame(SpriteSheet.getSprite(2*2-1, 0), 5),//down
            new Frame(SpriteSheet.getSprite(3*2-1, 0), 10),//down
            new Frame(SpriteSheet.getSprite(4*2-1, 0), 5),//up
            new Frame(SpriteSheet.getSprite(5*2-1, 0), 10),//contact
            new Frame(SpriteSheet.getSprite(6*2-1, 0), 5),//down
            new Frame(SpriteSheet.getSprite(7*2-1, 0), 10),//down
            new Frame(SpriteSheet.getSprite(8*2-1, 0), 5)};//up
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
            int size = land.get(land.size() - 1).getX();
            availableMoves = (new PathFinding()).getMovement(this, size, land);
            availableMoves.forEach(l -> l.setColor(Color.RED));
            if (mouse.getEventType() == 1)
            {
                availableMoves.forEach(l ->
                {
                    if (mouse.mouseOver(l))// set path to clicked space
                    {
                        path = (new PathFinding()).getPath(this, l, land);
                        land.get((int)(x+y*(size+1))).setOccupiedBy(null);
                    }
                });
                land.forEach(l ->
                {
                    if (mouse.mouseOver(l) && l.occupied())//attack clicked square
                    {
                        if (l.nextTo(this, size + 1))
                        {
                            stab(l.getOccupiedBy());
                            if (l.getOccupiedBy().getHealth() <= 0)
                            {
                                l.setOccupiedBy(null);
                            }
                        }
                    }
                });
            }
        }
        else if(!moving && path.size() > 0)
        {
            path.get(0).setOccupiedBy(this);
            direction  = this.getDirection(path.get(0));
            System.out.println(direction);
            if(direction == ID.up)
            {
                animation = walkUp;
            }
            else if(direction == ID.down)
            {
                animation = walkDown;
            }
            else if(direction == ID.left)
            {
                animation = walkLeft;
            }
            else if(direction == ID.right)
            {
                animation = walkRight;
            }
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
            if (path.get(0).getX() == 0 && x >= 24)
                x+=speed;
            else if(path.get(0).getX() == 24 && x <= 0)
                x-=speed;
            else if(path.get(0).getY() == 0 && y >= 24)
                y+=speed;
            else if(path.get(0).getY() == 24 && y <= 0)
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
                path.get(0).setOccupiedBy(null);
                path.remove(0);
                moving=false;
            }
            else if((int)x <= -1)
                x = 25;
            else if((int)x >= 25)
                x = -1;
            else if((int)y <= -1)
                y = 25;
            else if((int)y >= 25)
                y = -1;
        }
        else
        {
            System.out.println(path);
        }
    }


    @Override
    public void render(Graphics g, double ss)
    {
        squareSize = ss;
        availableMoves.forEach(l->l.render(g,ss));//draw all spaces human can move
        g.drawImage(animation.getSprite(), (int) (x * squareSize), (int) (y * squareSize), (int) squareSize, (int) squareSize, null);
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
