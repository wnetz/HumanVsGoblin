import GameObject.Goblin;
import GameObject.Human;
import GameObject.Land;
import Logic.Handler;
import Logic.Mouselistener;
import Logic.mapping.DelaunayTriangulations;
import Logic.mapping.Point;
import Logic.mapping.PoissonDisc;
import Logic.mapping.WaveCollapse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        ArrayList<Point> points = new ArrayList<>(PoissonDisc.generatePoints(2.75,handler.getLand(),COLUMNS,ROWS,10).stream()
                .map(l-> new Point(l.getX()*TILE_SIZE,l.getY()*TILE_SIZE,l)).collect(Collectors.toList()));
        triangles = new ArrayList<>(DelaunayTriangulations.triangulate(points,SCREEN_WIDTH,SCREEN_HEIGHT).stream()
                .map(triangle ->
                {
                    int[] x = java.util.List.of(triangle.getPoints()).stream().mapToInt(a-> a.getX()).toArray();
                    int[] y = List.of(triangle.getPoints()).stream().mapToInt(a-> a.getY()).toArray();
                    int size = triangle.getPoints().length;
                    return new Polygon(x,y,size);
                }).collect(Collectors.toList()));

        /*waveCollapse = new WaveCollapse(handler.getLand(),COLUMNS,ROWS);
        boolean mapBuilt = waveCollapse.collapse();
        while (!mapBuilt)
        {
            mapBuilt = waveCollapse.collaps();
            waveCollapse.reset();
        }*/


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
        for(Polygon triangle: triangles)
        {
            g.setColor(Color.blue);
            g.drawPolygon(triangle);
        }
        /*g.setColor(Color.black);
        for(int i = 0; i<=X;i++)//draw lines to better show tiles
        {
            g.drawLine((int)(i*(this.getWidth()/(double)(X))), 0, (int)(i*(this.getWidth()/(double)(X))), this.getHeight());
            g.drawLine(0, (int)(i*(this.getHeight()/(double)(X))), this.getWidth(), (int)(i*(this.getHeight()/(double)(X))));
        }*/
        g.dispose();
    }
}
