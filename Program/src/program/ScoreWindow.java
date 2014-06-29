/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JLabel;

/**
 *
 * @author Rafal
 */
public class ScoreWindow extends Container {
    JLabel label0; 
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    Screen screen;
    
    public ScoreWindow(String s, Screen scr){
        setVisible(false);
        screen=scr;
        setLayout(new FlowLayout());
        label0=new JLabel(ValueReader.NAME+"! "+s+"   ");
        label1=new JLabel(ValueReader.TIME+": 0"+"   ");
        label2=new JLabel(ValueReader.SCORE+": 0"+"   ");
        label3=new JLabel(ValueReader.LIFE+": 0"+"   ");
        label4=new JLabel(ValueReader.LEVEL+": 1"+"   ");
        try{
            Font customFont1 = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/data/PrimeLight.otf")).deriveFont(27f);
            Font customFont2 = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/data/PrimeLight.ttf")).deriveFont(27f);
            Font customFont3 = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/data/PrimeRegular.otf")).deriveFont(27f);

            label0.setFont(customFont1);
            label1.setFont(customFont2);
            label2.setFont(customFont3);
            label3.setFont(customFont2);
            label4.setFont(customFont1);
        }
        catch(IOException e){}
        catch(FontFormatException e){}
        label0.setForeground(Color.WHITE);
        label1.setForeground(Color.YELLOW);
        label2.setForeground(Color.LIGHT_GRAY);
        label3.setForeground(Color.GREEN);
        label4.setForeground(Color.CYAN);
        add(label0);
        add(label1);
        add(label2);
        add(label3);
        add(label4);
        setVisible(true);
        screen.add(this);
    }
    
    
    /**
     * Metoda pozwalająca na wyświetlenie aktualnej ilości żyć. Wartość ilości żyć pobierana jest z argumentu.
     */
    public void updateLifes(int i){
        label3.setText(ValueReader.LIFE+": "+i+"   ");
        repaint();
    }
    
    
    /**
     * Metoda pozwalająca na wyświetlenie aktualnego wyniku gry. Wartość wyniku pobierana jest z argumentu.
     */
    public void updateScore(int i){
        label2.setText(ValueReader.SCORE+": "+i+"   ");
        repaint();
    }
    
    
    /**
     * Metoda pozwalająca na wyświetlanie aktualnego czasu. Wartość czasu pobierana jest z argumentu.
     */
    public void updateTime(int i){
       label1.setText(ValueReader.TIME+": "+i+"   ");
       repaint();
    }
    
    
    /**
     * Metoda pozwalająca na wyświetlanie aktualnego poziomu. Wartość poziomu pobierana jest z argumentu.
     */
    public void updateLevel(int i){
       label4.setText(ValueReader.LEVEL+": "+i+"   ");
       repaint();
    }
    
    
    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0, 0, screen.getSize().width, 50);        
        super.paint(g);
    }
}
