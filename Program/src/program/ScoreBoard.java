/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Rafal
 */
public class ScoreBoard extends Container implements ActionListener{
    JButton button1=new JButton(ValueReader.EXIT);
    JButton button2=new JButton(ValueReader.OK);
    JLabel empty=new JLabel(" ");
    Dimension size;
    Container buttons,center,data, main;
    String name, ip, port;
    boolean isReady=false;
    Image banner;
    Dimension bannerSize;
    TheGame currentGame;
    
    public ScoreBoard(TheGame game){       
        currentGame=game;
        ImageIcon imic = new ImageIcon(getClass().getResource("/images/banner.png"));
        banner = imic.getImage();
        bannerSize=new Dimension(imic.getIconWidth(),imic.getIconHeight());
        size=new Dimension(Math.max(currentGame.display.getSize().width/4, bannerSize.width+40),currentGame.display.getSize().height/2);
                
        data=new Container();
        BoxLayout b1=new BoxLayout(data, BoxLayout.Y_AXIS);
        data.setLayout(b1);
        data.setLocation(0, bannerSize.height+40);
        data.setSize(size.width, size.height-bannerSize.height-40);

        
        //center=new Container();
        //BoxLayout b2=new BoxLayout(center,BoxLayout.Y_AXIS);
        //center.setLayout(b2);
        
        JLabel label0=new JLabel(currentGame.player.name+": "+currentGame.score);
        
        JLabel label1=new JLabel(ValueReader.SCORE1);
        JLabel label2=new JLabel(ValueReader.SCORE2);
        JLabel label3=new JLabel(ValueReader.SCORE3);
        JLabel label4=new JLabel(ValueReader.SCORE4);
        JLabel label5=new JLabel(ValueReader.SCORE5);
        if(label1.getText().equals(ValueReader.SCORED)){
            JLabel labeld1=new JLabel(ValueReader.COMMUNICATE_IS_OFFLINE_PT1);
            JLabel labeld2=new JLabel(ValueReader.COMMUNICATE_IS_OFFLINE_PT2);
            label0.setFont(new Font("sansserif", Font.BOLD, 32));
            data.add(labeld1);
            data.add(labeld2);
            data.add(empty);
            data.add(label0);
        }
        else{
            data.add(new JLabel(ValueReader.SCORES));
            if(label0.getText().equals(ValueReader.SCORE1))
                label1.setFont(new Font("sansserif", Font.BOLD, 32));
            data.add(label1);
            data.add(label2);
            data.add(label3);
            data.add(label4);
            data.add(label5);
        }
            
        
        
        //data.add(center);
        
        buttons=new Container();
        buttons.setLayout(new FlowLayout());
        button1.addActionListener(this);
        button2.addActionListener(this);      
        
        buttons.add(button1);
        buttons.add(button2);
        data.add(buttons);
        
        data.validate();
        data.setVisible(true);
        add(data);
        validate();
        setVisible(true);
    }
    
    public void waitForEvent(){
        //getParent().revalidate();
        while(!isReady){
            try{
                Thread.sleep(100);
            }
            catch(InterruptedException w){}
        }
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

    @Override
    public synchronized void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==button2){
            isReady=true;
            this.setVisible(false);
        }
        if(e.getSource()==button1){
            System.exit(0);
        }
    }
}
