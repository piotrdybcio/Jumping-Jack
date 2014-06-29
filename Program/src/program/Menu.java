/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.omg.CORBA.Current;

/**
 *
 * @author Rafal
 */
public class Menu extends Container implements ActionListener{
    JButton button1=new JButton(ValueReader.RESUME);
    JButton button2=new JButton(ValueReader.NEW_GAME);
    JButton button3=new JButton(ValueReader.EXIT);
    JButton button4=new JButton(ValueReader.SAVE);
    Dimension size;
    Container buttons,center, data;
    Image banner;
    Dimension bannerSize;
    String result;
    boolean isReady=false;
    
    public Menu(Dimension size1){
        setVisible(false);
        ImageIcon imic = new ImageIcon(getClass().getResource("/images/banner.png"));
        banner = imic.getImage();
        bannerSize=new Dimension(imic.getIconWidth(),imic.getIconHeight());
        size=new Dimension(Math.max(size1.width/4, bannerSize.width+40),size1.height/2);
        
        data=new Container();
        data.setLayout(new FlowLayout());
        data.setLocation(0, bannerSize.height+100);
        data.setSize(size.width, size.height-bannerSize.height-100);
        
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this); 
        button3.addActionListener(this); 
        
        center=new Container();
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        center.add(button1);
        center.add(button2);
        center.add(button3);
        center.add(button4);
                
        data.add(center);
        
        data.validate();
        add(data);
        data.setVisible(true);
        validate();
        setVisible(true);

    }
    
    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Dimension size2=getParent().getSize();
        setSize(size.width, size.height);
        setLocation((size2.width-this.getSize().width)/2,(size2.height-this.getSize().height)/2); 
        
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, 0, size.width, size.height, 20, 20);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0, 0, size.width, size.height, 20, 20);
        g.drawRoundRect(1, 1, size.width-2, size.height-2, 16, 16);
        g.drawRoundRect(2, 2, size.width-4, size.height-4, 12, 12);
        g2d.drawImage(banner, 20, 30, this);
   
        super.paint(g);
    }

    public void waitForEvent(){
        //getParent().validate();
        while(!isReady){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException w){}
        }
    }
    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==button1){
            result="RESUME";
            isReady=true;
            this.setVisible(false);
        }
        if(e.getSource()==button2){
            result="NEW";
            isReady=true;
            this.setVisible(false);
        }
        if(e.getSource()==button3){
            System.exit(0);
        }
        if(e.getSource()==button4){
            //ValueReader.saveTimeToFile();
            result="SAVE";
            isReady=true;
            this.setVisible(false);
        }
    }
}
