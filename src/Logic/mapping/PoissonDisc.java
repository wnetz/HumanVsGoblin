package Logic.mapping;

import GameObject.Land;
import Graphics.SpriteSheet;
import Logic.Pathing.PathFinding;

import java.util.ArrayList;
import java.util.Random;

public class PoissonDisc
{
    static int columns;
    static int rows;
    static Random rand;
    static double cellsize;
    static double radius;
    static int gridWidth;
    static int gridHeight;
    static int[][] grid;
    static ArrayList<Land> points;
    static ArrayList<Land> spawnPoints;
    public static ArrayList<Land> generatePoints(double radi, ArrayList<Land> land, int c, int r, int trys)
    {
        columns = c;
        rows = r;
        radius = radi;
        rand = new Random();
        cellsize = radius/Math.sqrt(2);
        gridWidth = (int)Math.ceil((columns-1)/cellsize);
        gridHeight = (int)Math.ceil((rows-1)/cellsize);
        grid = new int[gridWidth][gridHeight];
        points = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        spawnPoints.add(land.get(columns/2 + (rows/2)*columns));
        while(spawnPoints.size()>0)
        {
            int spawnIndex = rand.nextInt(spawnPoints.size());
            Land spawnCenter = spawnPoints.get(spawnIndex);
            boolean accepted = false;
            int x = spawnCenter.getX();
            int y = spawnCenter.getY();

            for(int i = 0; i < trys; i++)
            {
                double angle = rand.nextDouble() * Math.PI * 2;
                double ran = rand.nextDouble(radius,2*radius);
                x = (int)(x + Math.sin(angle) * ran);
                y = (int)(y + Math.cos(angle) * ran);
                x = x >= columns ? x%columns : x < 0 ? columns+x : x;
                y = y >= rows ? y%rows : y < 0 ? rows+y : y;
                Land candidate = land.get(x + y * columns);
                if(isValid(candidate))
                {
                    points.add(candidate);
                    spawnPoints.add(candidate);
                    grid[(int)(x/cellsize)][(int)(y/cellsize)] = points.size();
                    i = trys;
                    accepted = true;
                }
            }
            if(!accepted)
            {
                spawnPoints.remove(spawnIndex);
            }
        }
        SpriteSheet spriteSheet = new SpriteSheet();
        spriteSheet.loadSprite("land");
        points.forEach(p->p.setImage(spriteSheet.getSprite(5,5)));
        return points;
    }

    private static boolean isValid(Land candidate)
    {
        if(candidate.getX() >= 0 && candidate.getX() < columns && candidate.getY() >= 0 && candidate.getY() < rows)
        {
            int x = candidate.getX();
            int y = candidate.getY();
            int cellX = (int)(x/cellsize);
            int cellY = (int)(y/cellsize);
            int searchStartX = cellX-2;
            int searchStartY = cellY-2;
            int searchEndX = cellX+2;
            int searchEndY = cellY+2;

            for(int i = searchStartX; i <= searchEndX; i++)
            {
                for(int j = searchStartY; j <= searchEndY; j++)
                {
                    int pointIndex = grid[i>=gridWidth? i%gridWidth : i < 0 ? gridWidth+i : i][j>=gridHeight? j%gridHeight : j < 0 ? gridHeight+j : j]-1;
                    //int pointIndex = grid[i][j]-1;
                    if(pointIndex != -1)
                    {
                        double distance = PathFinding.getdistance(cellX,cellY,i,j,columns,rows);
                        if(distance < radius)
                        {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
