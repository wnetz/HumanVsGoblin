import GameObject.Land;
import Graphics.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Collectors;

public class WaveCollapse
{
    private final int spriteSheetLength = 6;
    private final int spriteSheetLeftOver = 1;
    private final int spriteSheetHeight = 5;
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
        tiles.add(new Tile(spriteSheet.getSprite(0,0),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 0
        tiles.add(new Tile(spriteSheet.getSprite(1,0),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 1
        tiles.add(new Tile(spriteSheet.getSprite(2,0),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 2
        tiles.add(new Tile(spriteSheet.getSprite(3,0),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 3
        tiles.add(new Tile(spriteSheet.getSprite(4,0),new ArrayList<>(Arrays.asList("A","B","B","A"))));//S:SW->NE path 0
        tiles.add(new Tile(spriteSheet.getSprite(5,0),new ArrayList<>(Arrays.asList("B","B","A","A"))));//N:NW->SE path 2
        tiles.add(new Tile(spriteSheet.getSprite(0,1),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 4
        tiles.add(new Tile(spriteSheet.getSprite(1,1),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 5
        tiles.add(new Tile(spriteSheet.getSprite(2,1),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 6
        tiles.add(new Tile(spriteSheet.getSprite(3,1),new ArrayList<>(Arrays.asList("A","A","A","A"))));//grass 7
        tiles.add(new Tile(spriteSheet.getSprite(4,1),new ArrayList<>(Arrays.asList("A","B","B","A"))));//S:SW->NE path 1
        tiles.add(new Tile(spriteSheet.getSprite(5,1),new ArrayList<>(Arrays.asList("B","B","A","A"))));//N:NW->SE path 1
        tiles.add(new Tile(spriteSheet.getSprite(0,2),new ArrayList<>(Arrays.asList("A","A","B","B"))));//S:NW->SE path 2
        tiles.add(new Tile(spriteSheet.getSprite(1,2),new ArrayList<>(Arrays.asList("A","A","B","B"))));//S:NW->SE path 1
        tiles.add(new Tile(spriteSheet.getSprite(2,2),new ArrayList<>(Arrays.asList("A","A","B","B"))));//S:NW->SE path 0
        tiles.add(new Tile(spriteSheet.getSprite(2,2),new ArrayList<>(Arrays.asList("B","B","B","B"))));//path
        tiles.add(new Tile(spriteSheet.getSprite(4,2),new ArrayList<>(Arrays.asList("A","B","B","A"))));//S:SW->NE path 2
        tiles.add(new Tile(spriteSheet.getSprite(5,2),new ArrayList<>(Arrays.asList("B","B","A","A"))));//N:NW->SE path 0
        tiles.add(new Tile(spriteSheet.getSprite(0,3),new ArrayList<>(Arrays.asList("B","A","A","B"))));//N:SW->NE path 2
        tiles.add(new Tile(spriteSheet.getSprite(1,3),new ArrayList<>(Arrays.asList("B","A","A","B"))));//N:SW->NE path 1
        tiles.add(new Tile(spriteSheet.getSprite(2,3),new ArrayList<>(Arrays.asList("B","A","A","B"))));//N:SW->NE path 0
        tiles.add(new Tile(spriteSheet.getSprite(3,3),new ArrayList<>(Arrays.asList("B","A","B","B"))));//right path
        tiles.add(new Tile(spriteSheet.getSprite(4,3),new ArrayList<>(Arrays.asList("A","B","B","B"))));//top path
        tiles.add(new Tile(spriteSheet.getSprite(5,3),new ArrayList<>(Arrays.asList("B","B","A","B"))));//bottom path
        tiles.add(new Tile(spriteSheet.getSprite(0,4),new ArrayList<>(Arrays.asList("B","B","B","A"))));//left path
        //spriteSheet.getSprite(x,y);
    }

    public void collaps(Graphics g, int ss)
    {
        boolean done = false;
        int times = 0;
        while (!done)
        {
            Cell cell = pickCell( g,  ss);
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
                                change = change || update(i, j);
                            }
                        }
                    }
                }
            }
            done = true;
            for(int i = 0; i < map.size() && done; i++)
            {
                if(map.get(i).getOptions().size() == 0)
                {
                    cell = null;
                }
                done = done && !map.get(i).isCollapsed();
            }
            if(cell == null)
            {
                System.out.println("FAILED MAP");
                done = true;
            }
            times++;
            int count = map.stream().mapToInt(a->a.isCollapsed()? 1:0).reduce(0,(a,b) -> a+b);
            System.out.println("Collapsed: " + count + " Interations: " + times);
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("Pause failed");
            }
        }
    }
    private Cell pickCell(Graphics g, int ss)
    {
        int minlength = map.stream()
                .mapToInt(a->a.getOptions().size())
                .filter(a -> a > 1)
                .min()
                .orElse(-1);
        ArrayList<Cell> temp = new ArrayList<>(map.stream().sorted(Comparator.comparingInt(a -> a.getOptions().size()))
                .filter(a -> !a.isCollapsed())
                .filter(a -> a.getOptions().size() == minlength)
                .collect(Collectors.toList()));
        Cell cell = null;
        if(temp.size()!=0)
        {
            cell = temp.get(rand.nextInt(temp.size()));
            cell.setCollapsed(true);
            cell.setOptions(new ArrayList<>(Arrays.asList(cell.getOptions().get(rand.nextInt(cell.getOptions().size())))));
            cell.getLand().setImage(cell.getOptions().get(0).getImage());
            cell.getLand().render(g,ss);
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
            neighbors.add(map.get(collumn-1 + y*collumn).getOptions());
        }
        else
        {
            neighbors.add(map.get(x-1 + y*collumn).getOptions());
        };
        return map.get(x+y*collumn).setOptions(neighbors.get(0),neighbors.get(1),neighbors.get(2),neighbors.get(3));
    }
}
