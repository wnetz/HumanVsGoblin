package Logic.mapping;

import GameObject.Land;
import Graphics.SpriteSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class WaveCollapse
{
    private ArrayList<Cell> map;
    private ArrayList<Tile> tiles;
    private SpriteSheet spriteSheet;
    private Random rand;
    private ArrayList<Land> re;
    private int collumn, row;
    public WaveCollapse(ArrayList<Land> m, int col, int ro)
    {
        collumn = col;
        row = ro;
        rand = new Random();
        tiles = new ArrayList<>();
        loadImages();
        re = m;
        map = new ArrayList<>(m.stream().map(e-> new Cell(e,tiles)).collect(Collectors.toList()));
    }
    public void reset()
    {
        map = new ArrayList<>(re.stream().map(e-> new Cell(e,tiles)).collect(Collectors.toList()));
    }

    private void loadImages()
    {
        spriteSheet = new SpriteSheet();
        spriteSheet.loadSprite("land");
        tiles.add(new Tile(spriteSheet.getSprite(0,0),new ArrayList<>(Arrays.asList("A","A","A","A")),0,TileType.grass));//grass 0
        tiles.add(new Tile(spriteSheet.getSprite(1,0),new ArrayList<>(Arrays.asList("A","A","A","A")),1,TileType.grass));//grass 1
        tiles.add(new Tile(spriteSheet.getSprite(2,0),new ArrayList<>(Arrays.asList("A","A","A","A")),2,TileType.grass));//grass 2
        tiles.add(new Tile(spriteSheet.getSprite(3,0),new ArrayList<>(Arrays.asList("A","A","A","A")),3,TileType.grass));//grass 3
        tiles.add(new Tile(spriteSheet.getSprite(4,0),new ArrayList<>(Arrays.asList("A","A","A","A")),4,TileType.grass));//grass 4
        tiles.add(new Tile(spriteSheet.getSprite(5,0),new ArrayList<>(Arrays.asList("A","A","A","A")),5,TileType.grass));//grass 5
        tiles.add(new Tile(spriteSheet.getSprite(6,0),new ArrayList<>(Arrays.asList("A","A","A","A")),6,TileType.grass));//grass 6
        tiles.add(new Tile(spriteSheet.getSprite(7,0),new ArrayList<>(Arrays.asList("A","A","A","A")),7,TileType.grass));//grass 7

        tiles.add(new Tile(spriteSheet.getSprite(0,1),new ArrayList<>(Arrays.asList("A","A","B","B")),10,TileType.cornerPath));//S:NW->SE path 0
        //tiles.add(new Tile(spriteSheet.getSprite(1,1),new ArrayList<>(Arrays.asList("A","A","B","B")),11,TileType.cornerPath));//S:NW->SE path 1
        //tiles.add(new Tile(spriteSheet.getSprite(2,1),new ArrayList<>(Arrays.asList("A","A","B","B")),12,TileType.cornerPath));//S:NW->SE path 2

        tiles.add(new Tile(spriteSheet.getSprite(0,2),new ArrayList<>(Arrays.asList("B","A","A","B")),20,TileType.cornerPath));//N:SW->NE path 0
        //tiles.add(new Tile(spriteSheet.getSprite(1,2),new ArrayList<>(Arrays.asList("B","A","A","B")),21,TileType.cornerPath));//N:SW->NE path 1
        //tiles.add(new Tile(spriteSheet.getSprite(2,2),new ArrayList<>(Arrays.asList("B","A","A","B")),22,TileType.cornerPath));//N:SW->NE path 2

        tiles.add(new Tile(spriteSheet.getSprite(0,3),new ArrayList<>(Arrays.asList("B","B","A","A")),30,TileType.cornerPath));//N:NW->SE path 0
        //tiles.add(new Tile(spriteSheet.getSprite(1,3),new ArrayList<>(Arrays.asList("B","B","A","A")),31,TileType.cornerPath));//N:NW->SE path 1
        //tiles.add(new Tile(spriteSheet.getSprite(2,3),new ArrayList<>(Arrays.asList("B","B","A","A")),32,TileType.cornerPath));//N:NW->SE path 2

        tiles.add(new Tile(spriteSheet.getSprite(0,4),new ArrayList<>(Arrays.asList("A","B","B","A")),40,TileType.cornerPath));//S:SW->NE path 0
        //tiles.add(new Tile(spriteSheet.getSprite(1,4),new ArrayList<>(Arrays.asList("A","B","B","A")),41,TileType.cornerPath));//S:SW->NE path 0
        //tiles.add(new Tile(spriteSheet.getSprite(2,4),new ArrayList<>(Arrays.asList("A","B","B","A")),42,TileType.cornerPath));//S:SW->NE path 0

        tiles.add(new Tile(spriteSheet.getSprite(0,5),new ArrayList<>(Arrays.asList("A","B","B","B")),50,TileType.sidePath));//top path
        tiles.add(new Tile(spriteSheet.getSprite(1,5),new ArrayList<>(Arrays.asList("B","A","B","B")),51,TileType.sidePath));//right path
        tiles.add(new Tile(spriteSheet.getSprite(2,5),new ArrayList<>(Arrays.asList("B","B","A","B")),52,TileType.sidePath));//bottom path
        tiles.add(new Tile(spriteSheet.getSprite(3,5),new ArrayList<>(Arrays.asList("B","B","B","A")),53,TileType.sidePath));//left path

        //tiles.add(new Logic.mapping.Tile(spriteSheet.getSprite(0,6),new ArrayList<>(Arrays.asList("B","B","B","B")),60,TileType.path));//path
        //spriteSheet.getSprite(x,y);
    }

    public boolean collapse()
    {
        boolean done = false;
        boolean success = true;
        int times = 0;
        while (!done)
        {
            Cell cell = pickCell();
            if(cell != null)
            {
                boolean change = true;
                while (change)
                {
                    change = false;
                    for (int i = 0; i < collumn; i++)
                    {
                        for (int j = 0; j < row; j++)
                        {
                            if (!map.get(i + j * collumn).isCollapsed())
                            {
                                change = update(i, j) || change;
                            }
                            /*if(map.get(i + j * collumn).getOptions().size()==0)
                            {
                                map.get(i + j * collumn).setOptions(tiles);
                            }*/
                        }
                    }
                }
                times++;
                int count = map.stream().mapToInt(a->a.isCollapsed()? 1:0).reduce(0,(a,b) -> a+b);
                //System.out.println("Collapsed: " + count + " Interations: " + times + " " + cell.getLand());
            }
            for(int i = 0; i < map.size() && done; i++)
            {
                if(map.get(i).getOptions().size() == 0)
                {
                    cell = null;
                }
            }
            if(cell == null)
            {
                System.out.println("End");
                done = true;
            }
            /*try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Pause failed");
            }*/
        }

        for(Cell c: map)
        {
            success = success && c.isCollapsed();
            if(c.getOptions().size()==0)
            {
                System.out.println(c.getLand());
                c.getLand().setImage(spriteSheet.getSprite(10,5));
            }
        }
        if(!success)
        {
            System.out.println("FAILED MAP");
        }
        return success;
    }
    private Cell pickCell()
    {
        int minlength = map.stream()
                .filter(a-> !a.isCollapsed())
                .mapToInt(a->a.getOptions().size())
                .filter(a -> a != 0)
                .min()
                .orElse(-1);
        if(minlength == -1)
        {
            System.out.println(-1);
        }
        ArrayList<Cell> temp = new ArrayList<>(map.stream().sorted(Comparator.comparingInt(a -> a.getOptions().size()))
                .filter(a -> !a.isCollapsed())
                .filter(a -> a.getOptions().size() == minlength)
                .collect(Collectors.toList()));
        Cell cell = null;
        if(temp.size()!=0)
        {
            ArrayList<TileType> types = new ArrayList<>();
            for(Cell c: temp)
            {
                for(Tile t: c.getOptions())
                {
                    if (!types.contains(t.getType())) {
                        types.add(t.getType());
                    }
                }
            }
            cell = temp.get(rand.nextInt(temp.size()));
            cell.setCollapsed(true);
            if(types.size()>1 && types.contains(TileType.grass) && rand.nextDouble() <= .95)
            {
                cell.setOptions(new ArrayList<>(cell.getOptions().stream().filter(a->a.getType()==TileType.grass).collect(Collectors.toList())));
            }
            cell.setOptions(new ArrayList<>(Arrays.asList(cell.getOptions().get(rand.nextInt(cell.getOptions().size())))));
            cell.getLand().setImage(cell.getOptions().get(0).getImage());
            if(cell.getOptions().get(0).getType() == TileType.grass)
            {
                cell.getLand().setMovement(2);
            }
            if(cell.getOptions().get(0).getType() == TileType.path)
            {
                cell.getLand().setMovement(1);
            }
        }
        return cell;
    }
    private boolean update(int x, int y)
    {
        ArrayList<ArrayList<Tile>> neighbors = new ArrayList<>();

        if(y == 0)//up
        {
            neighbors.add(map.get(x + (row-1)*collumn).getOptions());
        }
        else
        {
            neighbors.add(map.get(x + (y-1)*collumn).getOptions());
        }
        neighbors.add(map.get((x + 1)%collumn+y*collumn).getOptions());//right
        neighbors.add(map.get(x+((y+1)%row)*collumn).getOptions());//down
        if(x == 0)//left
        {
            neighbors.add(map.get((collumn-1) + y*collumn).getOptions());
        }
        else
        {
            neighbors.add(map.get((x-1) + y*collumn).getOptions());
        };
        return map.get(x+y*collumn).setOptions(neighbors.get(0),neighbors.get(1),neighbors.get(2),neighbors.get(3));
    }
}
