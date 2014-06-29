/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapcreator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;

/**
 *
 * @author Rafal
 */
public class MapCreator {

    Map map;
    Scr screen;
    int mouseOption, tileSize=30;
    JPanel jpanel;
    HelpLine linesV[],linesH[];
    
    
    public class HelpLine extends Component{
        Dimension start, stop;
        public HelpLine(int x1, int y1, int x2, int y2){
            start=new Dimension(x1,y1);
            stop=new Dimension(x2,y2);
            setSize(4000,2000);
            setLocation(x1,y1);
            setVisible(true);
            jpanel.add(this);   
        }
        @Override
        public void paint(Graphics g){
            g.setColor(Color.black);
            g.drawLine(start.width, start.height, stop.width, stop.height);
            super.repaint();
        }
    }
    
    
    public void openMap(String s) throws FileNotFoundException{
        jpanel.removeAll();
        jpanel.validate();
        jpanel.repaint();
        FileReader f=new FileReader(s);
        BufferedReader bufor=new BufferedReader(f);
        try{
            String line;
            int w =Integer.parseInt(bufor.readLine());
            int h =Integer.parseInt(bufor.readLine());
            
            linesV=new HelpLine[w+2];
            linesH=new HelpLine[h+2];
            for(int i=0;i<w+2;i++)
                linesV[i]=new HelpLine(i*tileSize/2,0,i*tileSize/2,h*tileSize);
            for(int i=0;i<h+2;i++)
                linesH[i]=new HelpLine(0,i*tileSize/2,2000,i*tileSize/2);
            map=new Map(w,h,this);
            map.elements=new Element[w][h];
            int lineNumber=0;
            while((line = bufor.readLine()) !=null ){
                for(int i=0;i<w;i++){
                    switch(line.charAt(i))
                    {
                        case 'S':
                            map.elements[i][lineNumber]=new Element(map.S, i, lineNumber, tileSize);
                            break;
                        case 'W':
                            map.elements[i][lineNumber]=new Element(map.W, i, lineNumber, tileSize);
                            break;
                        case 'X':
                            map.elements[i][lineNumber]=new Element(map.X, i, lineNumber, tileSize);
                            break;
                        case 'Y':
                            map.elements[i][lineNumber]=new Element(map.Y, i, lineNumber, tileSize);
                            break;
                        case 'F':
                            map.elements[i][lineNumber]=new Element(map.F, i, lineNumber, tileSize);
                            break;
                        case 'Q':
                            map.elements[i][lineNumber]=new Element(map.Q, i, lineNumber, tileSize);
                            break;
                        case 'V':
                            map.elements[i][lineNumber]=new Element(map.V, i, lineNumber, tileSize);
                            break;
                        case 'O':
                            map.elements[i][lineNumber]=new Element(map.O, i, lineNumber, tileSize);
                            break;
                        case 'K':
                            map.elements[i][lineNumber]=new Element(map.K, i, lineNumber, tileSize);
                            break;
                        case 'I':
                            map.elements[i][lineNumber]=new Element(map.I, i, lineNumber, tileSize);
                            break;
                        default:
                            map.elements[i][lineNumber]=null;
                            break;
                    }
                }
                lineNumber++;
                map.size.width=w;  
            }
            jpanel.setVisible(true);
            map.size.height=lineNumber;
            map.putTextures();
            f.close();
            map.s=s;
            screen.enablejMenuItem3();  
        }
        catch(NumberFormatException e){
            System.out.println("Zły standard pliku.");
            System.exit(-1);
        }
        catch(IOException e){
            System.out.println("Błąd odczytu z pliku.");
        }
    }
    
    
    public void createMap(int x, int y){
        linesV=new HelpLine[x+1];
        linesH=new HelpLine[y+1];
        map=new Map(x,y, this);
        map.elements=new Element[x][y];
        for(int i=0;i<x+1;i++)
            linesV[i]=new HelpLine(i*tileSize/2,0,i*tileSize/2,y*tileSize);
        for(int i=0;i<y+1;i++)
            linesH[i]=new HelpLine(0,i*tileSize/2,x*tileSize,i*tileSize/2);
    }
    
    
    public MapCreator(){
        screen=new Scr(this);
        jpanel=this.screen.getPan();
    }
    
    
    public static void main(String[] args) {
        MapCreator mc=new MapCreator();    
    }
}
