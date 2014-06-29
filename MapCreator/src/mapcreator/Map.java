/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapcreator;

import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author Rafal
 */
public final class Map{
    Element[][] elements;
    FileWriter zapis;
    Image W, X, S, V, O, Y, F, Q, K, I;
    Dimension size, startPosition;
    MapCreator mc;
    int tileSize, offsetX, dispWidth;
    boolean setStart=false;
    String s;
    
    public void saveMap() throws IOException{
        zapis = new FileWriter(s);
        try (BufferedWriter out = new BufferedWriter(zapis)){
            String lineNumbers=size.width+"\n";
            out.write(lineNumbers);
            lineNumbers=size.height+"\n";
            out.write(lineNumbers);
            for(int j=0; j<size.height;j++)
            {
                for(int i=0;i<size.width;i++){
                    Element e=elements[i][j];
                    if(e==null){
                        out.write("."); 
                    }    
                    else if(e.img==W){
                        out.write("W");
                    }
                    else if(e.img==X){
                        out.write("X");
                    }
                    else if(e.img==S){
                        out.write("S");
                        System.out.print("S");
                    }
                    else if(e.img==V){
                        out.write("V");
                    }
                    else if(e.img==O){
                        out.write("O");
                    }
                    else if(e.img==Y){
                        out.write("Y");
                    }
                    else if(e.img==Q){
                        out.write("Q");
                    }
                    else if(e.img==F){
                        out.write("F");
                    }
                    else if(e.img==K){
                        out.write("K");
                    }
                    else if(e.img==I){
                        out.write("I");
                    }
                }
                if(j!=size.height-1)
                    out.write("\n");                    
            }
            out.close();
        }
    }
    public void closeFile() throws IOException{
        zapis.close();
    }
            
    public void saveMap(String st) throws IOException{
        s=st;
        saveMap();
                    
    }
    
    public void addElement(int x, int y, int z){
        x+=offsetX/tileSize;
        if(z==1&&elements[x][y]!=null){
            if(elements[x][y].img==S)
                setStart=false;
            mc.screen.getPan().remove(elements[x][y]);
            elements[x][y]=null;
        }
        else if(z==2&&setStart==false){
            setStart=true;
            elements[x][y]=new Element(S,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
             
        else if(z==3&&elements[x][y]==null){
            elements[x][y]=new Element(X,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
           
        else if(z==4&&elements[x][y]==null){
            elements[x][y]=new Element(W,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==6&&elements[x][y]==null){
            elements[x][y]=new Element(F,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==7&&elements[x][y]==null){
            elements[x][y]=new Element(Q,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        
        else if(z==8&&elements[x][y]==null){
            elements[x][y]=new Element(O,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==9&&elements[x][y]==null){
            elements[x][y]=new Element(V,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==10&&elements[x][y]==null){
            elements[x][y]=new Element(Y,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==11&&elements[x][y]==null){
            elements[x][y]=new Element(K,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        else if(z==12&&elements[x][y]==null){
            elements[x][y]=new Element(I,x-offsetX/tileSize,y,tileSize);
            elements[x][y].setVisible(true);
            mc.screen.getPan().add(elements[x][y]);
            mc.screen.getPan().validate();
        }
        
    }
    public void loadTextures(){
        ImageIcon iconW = new ImageIcon(getClass().getResource("/textures/W.png"));
        W = iconW.getImage();
        tileSize=W.getWidth(null);
        ImageIcon iconX = new ImageIcon(getClass().getResource("/textures/X.png"));
        X = iconX.getImage();
        ImageIcon iconS = new ImageIcon(getClass().getResource("/textures/S.png"));
        S = iconS.getImage();
        ImageIcon iconV = new ImageIcon(getClass().getResource("/textures/V.png"));
        V = iconV.getImage();
        ImageIcon iconO = new ImageIcon(getClass().getResource("/textures/O.png"));
        O = iconO.getImage();
        ImageIcon iconY = new ImageIcon(getClass().getResource("/textures/Y.png"));
        Y = iconY.getImage();
        ImageIcon iconF = new ImageIcon(getClass().getResource("/textures/F.png"));
        F = iconF.getImage();
        ImageIcon iconQ = new ImageIcon(getClass().getResource("/textures/Q.png"));
        Q = iconQ.getImage();
        ImageIcon iconK = new ImageIcon(getClass().getResource("/textures/K.png"));
        K = iconK.getImage();
        ImageIcon iconI = new ImageIcon(getClass().getResource("/textures/I.png"));
        I = iconI.getImage();
    }
    
    public void putTextures(){
        for(int i=0;i<elements.length;i++)
            for(int j=0; j<elements[0].length;j++){
                Element e=elements[i][j];
                if(e!=null){
                    e.setVisible(true);
                    mc.screen.getPan().add(e);
                    mc.screen.getPan().validate();
                }
            }
    }

    public void scroll(int i){
        System.out.println(offsetX);
        int t=i*tileSize;
        if(size.width*(tileSize+1)-offsetX-t>dispWidth&&((offsetX>=0&&i>=0)||(offsetX>0&&i<0)))
        {
            for(Element[] ex: elements){
                for(Element e:ex){
                    if(e!=null){
                        e.x-=t;
                        e.revalidate();
                    }
                } 
            }
            offsetX+=t;
            mc.screen.getPan().repaint();
            
            
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException e) { }
        }

    }

    public void show(){
        for(Element[] ex: elements){
            for(Element e:ex){
                if(e!=null){
                    e.setVisible(true);
                }
            } 
        }
        
    }
    
    public Map(int x, int y, MapCreator m){
        mc=m;
        elements=new Element[x][y];        
        offsetX=0;
        dispWidth=mc.screen.getPan().getWidth();
        tileSize=mc.tileSize;
        size=new Dimension(x, y);
        loadTextures();
    }    
        
}
    
    

