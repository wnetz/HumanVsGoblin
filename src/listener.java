import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class listener extends KeyAdapter
{
    private ID gameState;
    Handler handler;
    public listener(ID gs,Handler h)
    {
        gameState = gs;
        handler = h;
    }
    public void setGameState(ID gs)
    {
        gameState = gs;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        for(ActiveEntity ae:handler.activeEntities)
        {
            if(ae.getType() == ID.human && gameState == ID.human)
            {
                switch (key)
                {
                    case KeyEvent.VK_LEFT: ae.move(ID.west);break;
                    case KeyEvent.VK_UP: ae.move(ID.north);break;
                    case KeyEvent.VK_RIGHT: ae.move(ID.east);break;
                    case KeyEvent.VK_DOWN: ae.move(ID.south);break;
                    case KeyEvent.VK_A: ae.attack(ID.west);break;
                    case KeyEvent.VK_W: ae.attack(ID.north);break;
                    case KeyEvent.VK_D: ae.attack(ID.east);break;
                    case KeyEvent.VK_S: ae.attack(ID.south);break;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
