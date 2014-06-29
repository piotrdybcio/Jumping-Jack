package program;

import java.awt.*;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

import javax.swing.*;
 
public class Screen extends JFrame{
    static Sounds sound;
    
    public Screen() {
        setTitle(ValueReader.GAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imic = new ImageIcon(getClass().getResource("/images/char.png"));
        Image img = imic.getImage();
        setIconImage(img);
        
        setSize(ValueReader.DEFAULT_SCREEN_WIDTH,ValueReader.DEFAULT_SCREEN_HEIGHT);
        
        JLabel contentPane = new JLabel();
        contentPane.setIcon(new ImageIcon(getClass().getResource("/images/background.jpg")));
        contentPane.setLayout( new BorderLayout() );
        setContentPane(contentPane);

        setMinimumSize(new Dimension(600,400));
        setMaximumSize(new Dimension(1024,768));
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocusInWindow();
        
        
        getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener(){

            @Override
            public void ancestorMoved(HierarchyEvent he) {
            }

            @Override
            public void ancestorResized(HierarchyEvent he) {
                Dimension d = getSize();
                Dimension m = getMaximumSize();
                boolean resize = d.width > m.width || d.height > m.height;
                d.width = Math.min(m.width, d.width);
                d.height = Math.min(m.height, d.height);
                if (resize) {
                    Point p = getLocation();
                    setVisible(false);
                    setSize(d);
                    setLocation(p);
                    setVisible(true);
                }
            }
        });
        
        validate();
        setVisible(true);
    }     
}
