package Logic.mapping;

import GameObject.Land;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        ArrayList<String> top = new ArrayList<>(u.stream().map(a->a.getDown()).collect(Collectors.toList()));
        ArrayList<String> bot = new ArrayList<>(d.stream().map(a->a.getUp()).collect(Collectors.toList()));
        ArrayList<String> lft = new ArrayList<>(l.stream().map(a->a.getRight()).collect(Collectors.toList()));
        ArrayList<String> rit = new ArrayList<>(r.stream().map(a->a.getLeft()).collect(Collectors.toList()));
        ArrayList<Tile> newOptions = new ArrayList<>();
        ArrayList<TileType> types = new ArrayList<>(List.of(TileType.values()));
        ArrayList<TileType> nontypes = new ArrayList<>();
        /*for(Tile up:u)
        {
            if(up.getDown().equals("A") && (up.getType() == TileType.cornerPath || up.getType() == TileType.sidePath))
            {
                nontypes.add(TileType.cornerPath);
                nontypes.add(TileType.sidePath);
            }
        }
        for(Tile right:r)
        {
            if(right.getLeft().equals("A") && (right.getType() == TileType.cornerPath || right.getType() == TileType.sidePath))
            {
                nontypes.add(TileType.cornerPath);
                nontypes.add(TileType.sidePath);
            }
        }
        for(Tile down:d)
        {
            if(down.getUp().equals("A") && (down.getType() == TileType.cornerPath || down.getType() == TileType.sidePath))
            {
                nontypes.add(TileType.cornerPath);
                nontypes.add(TileType.sidePath);
            }
        }
        for(Tile left:l)
        {
            if(left.getRight().equals("A") && (left.getType() == TileType.cornerPath || left.getType() == TileType.sidePath))
            {
                nontypes.add(TileType.cornerPath);
                nontypes.add(TileType.sidePath);
            }
        }*/
        nontypes = new ArrayList<>(nontypes.stream().distinct().collect(Collectors.toList()));
        types.removeAll(nontypes);
        for(Tile tile:allTiles)
        {
            if(types.contains(tile.getType()) && top.contains(tile.getUp()) && rit.contains(tile.getRight()) && bot.contains(tile.getDown()) && lft.contains(tile.getLeft()))
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
