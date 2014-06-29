/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapcreator;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author Rafal
 */
public class Element extends Component {
    Image img;
    int x,y,size;
    
    public Element(Image i, int locx, int locy, int s){
        setVisible(true);
        setSize(1000,800);
        size=s-1;
        img=i;
        x=locx*s;
        y=locy*s;
        img=i;
    }
    
    
    public void paint(Graphics g) {
        g.drawImage(img, x+1,y+1, size, size, null);
        super.repaint();
    }
}
