import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class listener implements KeyListener
{
    private Human human;
    private ID gameState;
    public listener(Human h, ID gs)
    {
        human = h;
        gameState = gs;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(gameState == ID.human)
        {
            switch (e.getKeyCode())
            {
                case 37: human.move(ID.west); break;
                case 38: human.move(ID.north); break;
                case 39: human.move(ID.east); break;
                case 40: human.move(ID.south); break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
