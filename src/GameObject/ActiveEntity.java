package GameObject;

import Logic.Pathing.Node;

import java.util.ArrayList;

public abstract class ActiveEntity extends Entity
{

    protected int damage;
    protected ArrayList<Node> availableMoves;
    public ActiveEntity(int h, int m, int x, int y, int c, int r, int s)
    {
        super(h, m, x, y, c, r, s);
        damage = 0;
        availableMoves = new ArrayList<>();
    }
    public abstract void tick(ArrayList<Land> Land);
    public int getDamage()
    {
        return damage;
    }
    public ArrayList<Node> getAvailableMoves() {
        return availableMoves;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setAvailableMoves(ArrayList<Node> availableMoves) {
        this.availableMoves = availableMoves;
    }
    public abstract boolean endTurn();



}
