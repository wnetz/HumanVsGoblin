import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

public class Game extends Canvas implements Runnable
{
    public final int X = 25, Y = 25; //number of squares
    public final int squareSize = 33;
    public final int WIDTH = X*squareSize, HEIGHT = Y*squareSize+23;// give buffer for top bar
    private boolean running = false;
    private Handler handler;
    private Mouselistener mouse;
    private Thread mainThread;

    public Game()
    {
        mouse = new Mouselistener();
        handler = new Handler(mouse,squareSize);
        Random rand = new Random();

        //System.setProperty("sun.awt.noerasebackground", "true");
        for(int i = 0; i < Y;i++)//add land grid
        {
            for(int j = 0; j < X;j++)
            {
                handler.addLand(new Land(j,i,rand.nextInt(1,2)));
            }
        }

        Human human = new Human(12,24, mouse);
        handler.addObject(human);//add human

        for(int i = 0; i<10; i++)//add initial goblins
        {
            boolean added = false;
            int t = 0;
            do//make sure that goblins are not added on top of eachother
            {
                Goblin g = new Goblin(rand.nextInt(X),rand.nextInt(Y));
                g.setNumber(i+1);//initialize initiative
                added = handler.addObject(g);
            }
            while (!added);

        }
        JFrame frame = new JFrame("human vs goblin");
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        start();
    }
    public synchronized void start()//start game
    {
        System.out.println("start");
        mainThread = new Thread(this);
        mainThread.start();
        running = true;
    }
    public synchronized void stop()//end game
    {
        try
        {
            mainThread.join();
            running = false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        mainThread = new Thread();
        mainThread.start();
    }
    @Override
    public void run()//start game loop
    {
        System.out.println("run");

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0; // ticks per second
        double ns = 1000000000 /  amountOfTicks;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;//number of ticks due since last loop
            lastTime = now;
            while(delta >= 1)//catch up on ticks if behind
            {
                tick();
                delta--;
            }
            if(running)
            {
                render();//render game
            }
            frames++;

            if(System.currentTimeMillis() - timer > 1000)
            {
                timer += 1000;
                //System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }
    private void tick()
    {
        handler.tick();
    }
    private void render()
    {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null)
        {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.gray);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());//make background
        handler.render(g,this.getWidth()/(double)(X));
        /*g.setColor(Color.black);
        for(int i = 0; i<=X;i++)//draw lines to better show tiles
        {
            g.drawLine((int)(i*(this.getWidth()/(double)(X))), 0, (int)(i*(this.getWidth()/(double)(X))), this.getHeight());
            g.drawLine(0, (int)(i*(this.getHeight()/(double)(X))), this.getWidth(), (int)(i*(this.getHeight()/(double)(X))));
        }*/
        g.dispose();
        bs.show();
    }
    public static void main(String[] args) {
        new Game();
    }
}
