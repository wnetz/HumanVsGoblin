import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class MainPage extends Canvas
{
    public MainPage(int width, int height, String title, Game g)
    {
        JFrame frame = new JFrame(title);
        frame.setSize(new Dimension(width, height));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(g);
        frame.setVisible(true);
        g.start();
    }
}
