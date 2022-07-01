public class Goblin extends Entity implements activeEntity
{
    public Goblin() {
        super(2, 2,0,0);
        type = ID.goblin;
    }

    public void turn(ID id)
    {
    }
    public void move(ID id)
    {

    }
    public int attack() {
        return 0;
    }
}
