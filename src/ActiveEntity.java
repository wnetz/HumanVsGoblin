import java.awt.*;
import java.util.ArrayList;

abstract class ActiveEntity extends Entity
{

    protected int damage;
    protected ArrayList<Land> availableMoves;
    public ActiveEntity(int h, int m, int x, int y)
    {
        super(h, m, x, y);
        damage = 0;
        availableMoves = new ArrayList<>();
    }
    abstract void tick(ArrayList<Land> Land);
    public int getDamage()
    {
        return damage;
    }
    public ArrayList<Land> getAvailableMoves() {
        return availableMoves;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setAvailableMoves(ArrayList<Land> availableMoves) {
        this.availableMoves = availableMoves;
    }
    abstract boolean endTurn();



}
