import GameObject.Land;

import java.util.ArrayList;

public class Cell
{
    private boolean collapsed;
    private ArrayList<Tile> options;
    private ArrayList<Tile> allTiles;
    private  Land land;
    public Cell(Land l, ArrayList<Tile> o)
    {
        collapsed = false;
        land = l;
        options = o;
        allTiles = o;
    }

    public boolean isCollapsed() {
        return collapsed;
    }
    public ArrayList<Tile> getOptions() {
        return options;
    }
    public Land getLand() {
        return land;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }
    public void setOptions(ArrayList<Tile> options) {
        this.options = options;
    }
    public boolean setOptions(ArrayList<Tile> u,ArrayList<Tile> r,ArrayList<Tile> d,ArrayList<Tile> l)
    {
        ArrayList<String> top = new ArrayList<>();
        ArrayList<String> bot = new ArrayList<>();
        ArrayList<String> lft = new ArrayList<>();
        ArrayList<String> rit = new ArrayList<>();
        ArrayList<Tile> newOptions = new ArrayList<>();
        for(Tile up:u)
        {
            top.add(up.getDown());
        }
        for(Tile right:r)
        {
            rit.add(right.getLeft());
        }
        for(Tile down:d)
        {
            bot.add(down.getUp());
        }
        for(Tile left:l)
        {
            lft.add(left.getRight());
        }
        for(Tile tile:allTiles)
        {
            if(top.contains(tile.getUp()) && rit.contains(tile.getRight()) && bot.contains(tile.getDown()) && lft.contains(tile.getLeft()))
            {
                newOptions.add(tile);
            }
        }
        boolean change = options.size() != newOptions.size();
        options = newOptions;
        return change;
    }
    public void setLand(Land land) {
        this.land = land;
    }



}
