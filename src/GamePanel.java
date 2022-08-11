import GameObject.Entity;
import GameObject.Goblin;
import GameObject.Human;
import GameObject.Land;
import Logic.Handler;
import Logic.Mouselistener;
import Logic.mapping.*;
import Logic.mapping.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import Graphics.SpriteSheet;

public class GamePanel extends JPanel implements Runnable
{
    public final int COLUMNS = 20, ROWS = 20; //number of squares
    public final int SPRITE_SIZE = 16;
    public final int SCALE = 3;
    public final int TILE_SIZE = SPRITE_SIZE*SCALE;
    public final int SCREEN_WIDTH = COLUMNS*TILE_SIZE, SCREEN_HEIGHT = ROWS*TILE_SIZE;
    private boolean running = false;
    private Handler handler;
    private Mouselistener mouse;
    private Thread gameThread;
    private WaveCollapse waveCollapse;
    private ArrayList<Polygon> triangles = null;
    private ArrayList<Point> points;
    public GamePanel()
    {
        mouse = new Mouselistener();
        handler = new Handler(mouse,TILE_SIZE,COLUMNS,ROWS);
        Random rand = new Random();

        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        for(int i = 0; i < ROWS;i++)//add land grid
        {
            for(int j = 0; j < COLUMNS;j++)
            {
                handler.addLand(new Land(j,i,rand.nextInt(1,2), COLUMNS, ROWS, TILE_SIZE));
            }
        }

        points = new ArrayList<>(PoissonDisc.generatePoints(rand.nextDouble(2.5,3.5),handler.getLand(),COLUMNS,ROWS,100).stream()
                .map(l-> new Point(l.getX()*TILE_SIZE,l.getY()*TILE_SIZE,l)).collect(Collectors.toList()));
        ArrayList<Triangle> tri = DelaunayTriangulations.triangulate(points,SCREEN_WIDTH,SCREEN_HEIGHT);
        triangles = new ArrayList<>(tri.stream()
                .map(triangle ->
                {
                    int[] x = java.util.List.of(triangle.getPoints()).stream().mapToInt(a-> a.getX()).toArray();
                    int[] y = List.of(triangle.getPoints()).stream().mapToInt(a-> a.getY()).toArray();
                    int size = triangle.getPoints().length;
                    return new Polygon(x,y,size);
                }).collect(Collectors.toList()));
        points = new ArrayList<>();
        for(Triangle triangle: tri)
        {
            Point[] trianglePoints = triangle.getPoints();
            for(int i = 0; i<trianglePoints.length;i++)
            {
                if(!points.contains(trianglePoints[i]))
                {
                    points.add(trianglePoints[i]);
                }
                points.get(points.indexOf(trianglePoints[i])).addAdjacent(trianglePoints[(i+1)%3]);
                points.get(points.indexOf(trianglePoints[i])).addAdjacent(trianglePoints[(i+2)%3]);
            }
        }
        SpriteSheet spriteSheet = new SpriteSheet();
        spriteSheet.loadSprite("land");
        for(Point point: points)
        {
            for(Point to: point.getAdjacent())
            {
                Line2D l1 = new Line2D.Double(point.getX()+TILE_SIZE/2,point.getY()+TILE_SIZE/2,to.getX()+TILE_SIZE/2,to.getY()+TILE_SIZE/2);
                for (Land land : handler.getLand())
                {
                    Rectangle2D tile = new Rectangle2D.Double(land.getX()*TILE_SIZE,land.getY()*TILE_SIZE,TILE_SIZE,TILE_SIZE);
                    if(l1.intersects(tile))
                    {
                        land.setImage(spriteSheet.getSprite(0,6));
                    }
                }
            }
        }


        Human human = new Human((COLUMNS/2),(ROWS-1), mouse, COLUMNS, ROWS, TILE_SIZE);
        handler.addObject(human);//add human

        for(int i = 0; i<1; i++)//add initial goblins
        {
            boolean added = false;
            int t = 0;
            do//make sure that goblins are not added on top of eachother
            {
                Goblin g = new Goblin(rand.nextInt(COLUMNS),rand.nextInt(ROWS),COLUMNS, ROWS, TILE_SIZE);
                g.setNumber(i+1);//initialize initiative
                added = handler.addObject(g);
            }
            while (!added);
        }
    }
    public synchronized void start()//start game
    {
        System.out.println("start");
        gameThread = new Thread(this);
        gameThread.start();
        running = true;
    }
    public synchronized void stop()//end game
    {
        try
        {
            gameThread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        gameThread = new Thread();
        gameThread.start();
    }
    public void run()//start game loop
    {
        System.out.println("run");

        long lastTime = System.nanoTime();
        double ticksPerSecond = 60.0; // ticks per second
        double interval = 1000000000 /  ticksPerSecond;
        double delta = 0.0;

        while(running)
        {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;//number of ticks due since last loop
            lastTime = currentTime;
            while(delta >= 1)//catch up on ticks if behind
            {
                tick();
                delta--;
            }
            if(running)
            {
                repaint();//render game
            }
        }
        stop();
    }
    private void tick()
    {
        handler.tick();
    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        handler.render(g,TILE_SIZE);
        /*for(Polygon triangle: triangles)
        {
            g.setColor(Color.blue);
            g.drawPolygon(triangle);
        }*/
        /*g.setColor(Color.black);
        for(int i = 0; i<=X;i++)//draw lines to better show tiles
        {
            g.drawLine((int)(i*(this.getWidth()/(double)(X))), 0, (int)(i*(this.getWidth()/(double)(X))), this.getHeight());
            g.drawLine(0, (int)(i*(this.getHeight()/(double)(X))), this.getWidth(), (int)(i*(this.getHeight()/(double)(X))));
        }*/
        g.dispose();
    }
}
