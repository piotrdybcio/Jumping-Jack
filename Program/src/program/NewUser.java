package program;

import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Rafal
 */
public class NewUser extends Container implements ActionListener{
    JLabel label1=new JLabel(ValueReader.GET_NAME);
    JLabel label2=new JLabel(ValueReader.GET_IP);
    JLabel label3=new JLabel(ValueReader.GET_PORT);
    JButton button1=new JButton(ValueReader.EXIT);
    JButton button2=new JButton(ValueReader.CONNECT);
    JButton button3=new JButton(ValueReader.PLAY_OFFLINE);
    Dimension size;
    Container buttons,center,data, main;
    String name, ip, port;
    JTextField nameField=new JTextField(1);
    JTextField ipField=new JTextField(1);
    JTextField portField=new JTextField(1);
    boolean isReady=false;
    Image banner;
    Dimension bannerSize;
    
    public NewUser(Dimension size1){
        ImageIcon imic = new ImageIcon(getClass().getResource("/images/banner.png"));
        banner = imic.getImage();
        bannerSize=new Dimension(imic.getIconWidth(),imic.getIconHeight());
        size=new Dimension(Math.max(size1.width/4, bannerSize.width+40),size1.height/2);
        
        data=new Container();
        data.setLayout(new FlowLayout());
        data.setLocation(0, bannerSize.height+40);
        data.setSize(size.width, size.height-bannerSize.height-40);
        
        center=new Container();
        center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        nameField.setSize(10, 60);
        nameField.setText(System.getProperty("user.name"));
        ipField.setSize(10, 60);
        ipField.setText(ValueReader.DEFAULT_IP);
        portField.setSize(10, 60);
        portField.setText(ValueReader.DEFAULT_PORT);             
        
        center.add(label1);
        center.add(nameField);
        center.add(label2);
        center.add(ipField);
        center.add(label3);
        center.add(portField);
        data.add(center);
        
        buttons=new Container();
        buttons.setLayout(new FlowLayout());
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);       
        buttons.add(button2);
        buttons.add(button3);
        data.add(buttons);
        data.add(button1);
        
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
        
        if(e.getSource()==button3){
            if(nameField.getText().length()>0){
                name=nameField.getText(); 
                ip="OFFLINE";
                port="OFFLINE";
            }
            else{
                name=ValueReader.NAME;
            }
            isReady=true;
            this.setVisible(false);
        }
        if(e.getSource()==button2){
            if(nameField.getText().length()>0){
                name=nameField.getText(); 
                ip=ipField.getText();
                port=portField.getText();
            }
            else{
                name=ValueReader.NAME;
            }
            isReady=true;
            this.setVisible(false);
        }
        if(e.getSource()==button1){
            System.exit(0);
        }
    }   
}
