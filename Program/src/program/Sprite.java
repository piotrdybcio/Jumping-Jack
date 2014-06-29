/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author Rafal
 */
public class Sprite extends Component implements Runnable {
    Image[] picture;
    Thread runner;
    int pause, totalPictures, size=33,current = 0;
    String type;
    Rectangle rect;
    Map map;
    
    public Sprite(String s, Image[] imi,int numb, int x,int y, int sz, Map mp){
        picture=new Image[numb];
        map=mp;
        picture=imi;
        totalPictures=numb;        
        type=s;
        rect=new Rectangle(x*sz,y*sz,sz,sz);
        rect.x+=5;
        rect.y+=5;
        rect.height=rect.width=size-10;
        pause=720/numb;
        size=sz;
        start();
    }
    
    public void paint(Graphics g) {
        Graphics2D screen2D = (Graphics2D) g;
        if (picture[current] != null){
            screen2D.drawImage(picture[current], rect.x-5-map.offsetX, rect.y-5,size, size,this);
        }
        
    }

    public void start() {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();
        }
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        while (runner == thisThread) {
            repaint();
            current++;
            if(current >= totalPictures)
                current = 0;
            try {
                Thread.sleep(pause);
            }
            catch (InterruptedException e) { }
        }
    }

    public void stop() {
        if (runner != null) {
            runner = null;
        }
    }
}
