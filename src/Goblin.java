public class Goblin extends ActiveEntity
{
    private final  int MOVEMENT = 2;
    public Goblin(int x,int y) {
        super(2, 2,x,y,1);
        type = ID.goblin;
    }
    public void move(ID id)
    {
        while(movement > 0)
        {
            movement--;
            switch (id)
            {
                case west:
                    if(x == 0)
                    {
                        x = 24;
                    }
                    else
                    {
                        x--;
                    }
                    break;
                case north:
                    if(y == 0)
                    {
                        y = 24;
                    }
                    else
                    {
                        y--;
                    }
                    break;
                case south:
                    if(y == 24)
                    {
                        y=0;
                    }
                    else
                    {
                        y++;
                    }
                    break;
                case east:
                    if(x == 24)
                    {
                        x = 0;
                    }
                    else
                    {
                        x++;
                    }
                    break;
            }
        }
        movement = MOVEMENT;
    }
    public int attack(ID id)
    {
        attacks--;
        return 0;
    }

    @Override
    void tick(int key) {

    }

    @Override
    void endTurn()
    {
        movement = MOVEMENT;
    }
}
