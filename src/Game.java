import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

    private Thread mainThread;
    public final int x = 25, y = 25;
    public int squareSize = 40;
    public final int WIDTH = x*squareSize, HEIGHT = y*squareSize+22;
    private boolean running = false;
    private Handler handler;
    private static ID gameState;

    public Game()
    {
        handler = new Handler();
        gameState = ID.human;

        System.setProperty("sun.awt.noerasebackground", "true");
        for(int i = 0; i < x;i++)
        {
            for(int j = 0; j < y;j++)
            {
                handler.addObject(new Land(i,j));
            }
        }
        Human human = new Human(12,24);
        handler.addObject(human);
        //new MainPage(WIDTH, HEIGHT, "human vs goblin", this);
        JFrame frame = new JFrame("human vs goblin");
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
        addKeyListener(new listener(human,gameState));
        start();
    }

    public void start()
    {
        System.out.println("start");
        mainThread = new Thread(this);
        mainThread.start();
        running = true;
    }
    public void stop()
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
    public void run()
    {
        System.out.println("run");
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 /  amountOfTicks;
        double delta = 0.0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1)
            {
                tick();
                delta--;
            }
            if(running)
            {
                render();
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
        handler.tick(gameState);
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
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.RED);
        handler.render(g,(this.getWidth()/(double)(x)));
        System.out.println((this.getWidth()/(double)(x)) + " " + (this.getHeight()/(double)(y)));
        g.setColor(Color.black);
        for(int i = 0; i<=x;i++)
        {
            g.drawLine((int)(i*(this.getWidth()/(double)(x))), 0, (int)(i*(this.getWidth()/(double)(x))), this.getHeight());
            g.drawLine(0, (int)(i*(this.getHeight()/(double)(x))), this.getWidth(), (int)(i*(this.getHeight()/(double)(x))));
        }
        //System.out.println(this.getSize());
        g.dispose();
        bs.show();
    }
    public static void main(String[] args) {
        new Game();
    }
}
