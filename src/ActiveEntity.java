import java.awt.*;

abstract class ActiveEntity extends Entity{

    protected int movement, attacks;
    public ActiveEntity(int h, int m, int x, int y, int a)
    {
        super(h, x, y);
        attacks = a;
        movement = m;
    }
    abstract void move(ID id);
    abstract int attack(ID id);
    abstract void tick(int key);
    public int getMovement()
    {
        return movement;
    }
    public int getAttacks()
    {
        return attacks;
    }
    abstract void endTurn();
}
