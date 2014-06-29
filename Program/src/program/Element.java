/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author Rafal
 */
public class Element extends Component {
    Image img;
    int size;
    Rectangle rect;
    Map map;
    
    public Element(Image i, int locx, int locy, int s, Map mp){
        setVisible(false);
        map=mp;
        size=s+1;
        img=i;
        rect=new Rectangle(locx*s,locy*s,s,s);
        img=i;
    }
    
    public void paint(Graphics g) {        
        g.drawImage(img, rect.x-map.offsetX, rect.y, size, size, null);
    }
}
