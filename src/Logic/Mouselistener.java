package Logic;

import GameObject.Entity;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouselistener implements MouseMotionListener, MouseListener {
    private int mouseX = 0,mouseY = 0;
    private int eventType = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        //eventType = e.getID();
        //System.out.println("drag: " + e.getID());
        //mouseX = e.getX();
        //mouseY = e.getY();
        //System.out.println("Drag mouseX: " + mouseX + " mouseY: " + mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //eventType = e.getID();
        //System.out.println("move: " + e.getID());
        //mouseX = e.getX();
        //mouseY = e.getY();
        //System.out.println("Move mouseX: " + mouseX + " mouseY: " + mouseY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        eventType = e.getButton();
        System.out.println("click: " + e.getButton());
        mouseX = e.getX();
        mouseY = e.getY();
        //System.out.println("click mouseX: " + mouseX + " mouseY: " + mouseY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //eventType = e.getID();
        //System.out.println("press: " + e.getID());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //eventType = e.getID();
        //System.out.println("relea: " + e.getID());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //eventType = e.getID();
        System.out.println("enter: " + e.getID());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //eventType = e.getID();
        System.out.println("exit: " + e.getID());
    }
    public boolean mouseOver(Entity e)
    {
        int x = e.getX()*e.getSquareSize();
        int y = e.getY()*e.getSquareSize();
        return mouseX>x && mouseX<x+e.getSquareSize() && mouseY>y && mouseY<y+e.getSquareSize();
    }
    public int getEventType() {
        int temp = eventType;
        eventType = 0;
        return temp;
    }
}
