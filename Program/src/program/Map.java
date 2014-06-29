/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.Dimension;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Klasa Map przechowuje informacje i metody pozwalające na obsługę mapy.
 * Pozwala na pobranie mapy o danym numerze (zazwyczaj odpowiadającym poziomowi gracza) z serwera lub z lokalizacji lokalnej, gdy nie istnieje połaczenie sieciowe.
 */
public final class Map{
    TheGame currentGame; /** Pole przechowujące grę, która wykorzystuje mapę.*/
    Element[][] elements; /**Pole przechowujące tablicę elementów stałych mapy.*/
    Sprite[][] sprites; /**Pole przechowujące tablicę elementów dynamicznych mapy.*/
    Image W, X; /**Pole przechowujące obrazy elementów statycznych mapy.*/
    Image[] coin, heart, fire, flower, bomb, puchar; /**Pole przechowujące tablice obraców elementów dynamicznych mapy.*/
    Dimension size, startPosition;
    int coinSize, heartSize; /**Pomocnice pola określające ilość odczytanych z zasobów lokalnych grafik danego typu.*/
    int tileSize; /** Pole pomocnicze odpowiadające rozmiarowi płytki*/
    int offsetX; /** Pole przechowujące wartość przesunięcia mapy względem ekranu w osi X */
    int dispWidth; /**Pole przechowujące szerokość ekranu*/
    int bufforCounter; /** Pole pomocnicze używane przy renderowaniu*/
    boolean ableToScroll; /**Pole pomocnicze określające zdolność do przesuwania ekranu. Zazwyczaj równe true, gdy mapa dojdzie do końca - false.*/
    int mn;
    
    /**
     * Metoda odpowiedzialna za wysłanie żądania do serwera o konkretne mapy.
     * @param number_maps numer mapy, zazwyczaj wartość poziomu gracza.
     */
    public void reciveMapsFromServer(int number_maps){
        try{
            Socket socket = new Socket(currentGame.currentGame.ip , Integer.parseInt(currentGame.currentGame.port));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("REQUEST_MAPS "+number_maps);
            System.out.println("Request for maps sent");
            try{
                InputStream is = socket.getInputStream();
                BufferedReader bufor = new BufferedReader(new InputStreamReader(is));
                this.loadMaps(bufor); 
                bufor.close();
                is.close();
            }
            catch(IOException e){System.err.println("Server exception: " + e);}
            pw.close();
            os.close();
            socket.close();
        }
        catch(IOException e){
            System.out.println("Błąd nawiązania połączenia z serwerem. Mapa pobrana z lokalnych źródeł :)");
            System.out.println(number_maps);
            currentGame.currentGame.ip="OFFLINE";
            reciveMapsFromLocalArea(number_maps);
        }
    }
     
     
     /**
      * Metoda odpowiadająca za pobranie map z zasobów lokalnych, jeśli sieciowe są nie dostępne.
      * @param number_maps numer mapy, zazwyczaj wartość poziomu gracza
      */
     public void reciveMapsFromLocalArea(int number_maps){
         try{
            InputStream plik=getClass().getResourceAsStream("/maps/"+number_maps+".jjmap");
            InputStreamReader reader = new InputStreamReader(plik);
            BufferedReader bufor=new BufferedReader(reader);
            this.loadMaps(bufor);
            bufor.close();
            reader.close();
            plik.close();   
         }
         catch(Exception e){ 
            System.err.println("Nie znaleziono pliku :( "+e);
            currentGame.level=1;
            reciveMapsFromLocalArea(1);
         }
    }

    
    /**
     * Główna metoda klasy Map. Odpowiada za wczytywanie elementów z przekazanego jej bufora i dodawaniu elementów do tablic, na podstawie których są rysowane i obcługiwane.
     * @param bufor Zawiera ciąg znaków przekazanych z funkcji reciveMapsFromServer lub reciveMapsFromLocalArea, zawierających tekstową reprezentację mapy.
     */
    public void loadMaps(BufferedReader bufor){
        String line;

        size=new Dimension(0,0);
        startPosition=new Dimension(0,0);

        int lineNumber=0;
        try{
            String columns=bufor.readLine();
            String rows=bufor.readLine();
            elements=new Element[Integer.parseInt(rows)][Integer.parseInt(columns)];
            sprites=new Sprite[Integer.parseInt(rows)][Integer.parseInt(columns)];
            int i;
            while((line = bufor.readLine()) !=null ){
                System.out.println(line);
                elements[lineNumber]=new Element[line.length()];
                sprites[lineNumber]=new Sprite[line.length()];
                for(i=0;i<line.length();i++){
                    switch(line.charAt(i))
                    {
                        case 'S':
                            startPosition=new Dimension(tileSize*i, tileSize*lineNumber);
                            break;
                        case 'W':
                            elements[lineNumber][i]=new Element(W, i, lineNumber, tileSize, this);
                            break;
                        case 'X':
                            elements[lineNumber][i]=new Element(X, i, lineNumber, tileSize, this);
                            break;
                        case 'Q':
                            sprites[lineNumber][i]=new Sprite("Q", bomb, 1, i, lineNumber, tileSize, this);
                            break;  
                        case 'O':
                            sprites[lineNumber][i]=new Sprite("O", coin, coinSize, i, lineNumber, tileSize, this);
                            break;
                        case 'V':
                            sprites[lineNumber][i]=new Sprite("V", heart, heartSize, i, lineNumber, tileSize, this);
                            break;
                        case 'F':
                            sprites[lineNumber][i]=new Sprite("F", fire, 1, i, lineNumber, tileSize, this);
                            break;
                        case 'Y':
                            sprites[lineNumber][i]=new Sprite("Y", flower, 1, i, lineNumber, tileSize, this);
                            break;
                        case 'I':
                            sprites[lineNumber][i]=new Sprite("I", puchar, 1, i, lineNumber, tileSize, this);
                            break;
                    }
                }
                lineNumber++;
                size.width=line.length();
            }
            size.height=lineNumber;  
        }
        catch(IOException e){
            System.out.print("Błąd odczytu z pliku." +e);
            reciveMapsFromServer(mn);
        }  
    }        
    
    /**
     * Metoda odpowiadająca za wczytanie grafik z przestrzeni lokalnej.
     * @return true jeśli wykonała się poprawnie
     */
    public boolean loadTextures(){
        ImageIcon iconW = new ImageIcon(getClass().getResource("/textures/W.png"));
        W = iconW.getImage();
        ImageIcon iconX = new ImageIcon(getClass().getResource("/textures/X.png"));
        X = iconX.getImage();
        ImageIcon iconF = new ImageIcon(getClass().getResource("/textures/F.png"));
        fire[0] = iconF.getImage();
        ImageIcon iconQ = new ImageIcon(getClass().getResource("/textures/Q.png"));
        bomb[0] = iconQ.getImage();
        ImageIcon iconY = new ImageIcon(getClass().getResource("/textures/Y.png"));
        flower[0] = iconY.getImage();
        ImageIcon iconI = new ImageIcon(getClass().getResource("/textures/I.png"));
        puchar[0] = iconI.getImage();
        
        coinSize=0;
        for (int i = 1; i < 8; i++) {
            String s="/textures/coin/coin"+i+".png";
            URL x=this.getClass().getResource(s);
            ImageIcon iconO;
            if(x!=null){
                coinSize++;
                iconO = new ImageIcon(getClass().getResource(s));
                coin[i-1] = iconO.getImage();
            }     
        }
        
        heartSize=0;
        for (int i = 1; i < 8; i++) {
            String s="/textures/heart/heart"+i+".png";
            URL x=this.getClass().getResource(s);
            ImageIcon iconV;
            if(x!=null){
                heartSize++;
                iconV = new ImageIcon(getClass().getResource(s));
                heart[i-1] = iconV.getImage();
            }     
        }
        return true;
    }
    
    
    /**
     * Metoda wywoływana przy inicjacji mapy. Odpowiada za zapełnienie ekranu (i 3 kolejnych kolumn) teksturami - elementami statycznymi i dynamicznymi.
     */
    public void putTextures(){
        //for(int i=0;i<(offsetX+dispWidth+3*tileSize)/tileSize&&i<size.width;i++){---------------Odkomentować dla dużych map.
        for(int i=0;i<elements[0].length;i++){ 
            for(int j=0; j<elements.length;j++){
                Element e=elements[j][i];
                if(e!=null){
                    e.setVisible(true);
                    currentGame.display.add(e);
                    currentGame.display.validate();
                }
            }
            for(int j=0; j<sprites.length;j++){
                Sprite e=sprites[j][i];
                if(e!=null){
                    e.setVisible(true);
                    currentGame.display.add(e);
                    currentGame.display.validate();
                }
            }
        }
    }
    
    
    /**
     * Metoda render służy do dodawania do ekranu kolejnych 5 kolumn za krawędzią prawą ekranu i usuwaniu wszystkich po lewej.
     * Pozwala zmniejszyć obciążenie przy bardzo dużych mapach. Obecnie nie jest używana.
     */
    public void render(){
        int start=(offsetX+dispWidth)/tileSize;
        for(int i=start;i<start+5;i++)
            if(i<size.width){
                for(int j=0; j<elements.length;j++){
                    Element e=elements[j][i];
                    if(e!=null){
                        currentGame.display.add(e);
                        currentGame.display.validate();
                    }
                }
                for(int j=0; j<sprites.length;j++){
                    Sprite e=sprites[j][i];
                    if(e!=null){
                        currentGame.display.add(e);
                        currentGame.display.validate();
                    }
                }
            }
                
        for(int i=(offsetX-tileSize)/tileSize;i<offsetX/tileSize;i++)
            if(i>=0){
                for(int j=0; j<elements.length;j++){
                    Element e=elements[j][i];
                    if(e!=null){
                        currentGame.display.remove(e);
                        currentGame.display.validate();
                    }
                }
                for(int j=0; j<sprites.length;j++){
                    Sprite e=sprites[j][i];
                    if(e!=null){
                        currentGame.display.remove(e);
                        currentGame.display.validate();
                    }
                }
            }
        currentGame.display.repaint();
        bufforCounter=0;
    }
    
    
    /**
     * Metoda odpowiadająca za przesuwanie ekranu. Jeśli przesunięcie jest większe niż 3 kratki, wywołuje metode służącą do renderowania.
     * Gdy cała mapa zostaje przesunięta, zmieniana jest wartość pola ableToScroll na fałsz. Dzięki temu użytkownik po dojściu do krawędzi będzie mógł przejść mapę.
     * @param speed Prędkość, z jaką przesuwa się ekran.
     */
    public void scrollRight(int speed){
        if(size.width*tileSize-offsetX>dispWidth){
            offsetX+=speed;
            //---------------------------Odkomentować dla dużych map
            //bufforCounter+=speed;
            //if(bufforCounter>3*tileSize)
                //render();
        }
        else
            ableToScroll=false;
    }
    
    
    /**
     * Metoda chowająca planszę na ekranie.
     */
    public void hide(){
        for(Element[] ex: elements){
            for(Element e:ex){
                if(e!=null){
                    e.setVisible(false);
                }
            } 
        }
        for(Sprite[] ex: sprites){
            for(Sprite e:ex){
                if(e!=null){
                    e.setVisible(false);
                }
            } 
        }
    }
    
    
    /**
     * Metoda pokazująca planszę na ekranie.
     */
    public void show(){
        for(Element[] ex: elements){
            for(Element e:ex){
                if(e!=null){
                    e.setVisible(true);
                }
            } 
        }
        for(Sprite[] ex: sprites){
            for(Sprite e:ex){
                if(e!=null){
                    e.setVisible(true);
                }
            } 
        }
    }
    
    
    /**
     * Konstruktor klasy Map inicjuje jej niezbędne elemnty.
     * Są to m.in. tablice na obrazy animacji, rozmiary wyświetlacza itp.
     * 
     * Ostatnią czynnością po zainincjowaniu mapy wykonywaną przez konstruktor jest ustawienia bohatera w oczekiwanym przez Twórcę mapy miejcu.
     * 
     * @param game Gra, dla której ma zostać załadowana mapa.
     * @param number_maps Numer żądanej mapy, zazwyczaj odpowiadający poziomowi gracza.
     */
    public Map(TheGame game, int number_maps){
        coin=new Image[7];
        fire=new Image[1];
        heart=new Image[7];
        flower=new Image[1];
        bomb=new Image[1];
        puchar=new Image[1];
        mn=number_maps;
        ableToScroll=true;
        offsetX=0;
        bufforCounter=0;
        dispWidth=game.display.getSize().width;
        tileSize=game.display.getSize().height/18;
        currentGame=game;
        if(loadTextures()){
            if(currentGame.currentGame.ip.equals("OFFLINE"))
                reciveMapsFromLocalArea(number_maps);
            else
                reciveMapsFromServer(number_maps);
            putTextures();
        }
        currentGame.player.setSize(tileSize);
        currentGame.player.setPosition(startPosition.width, startPosition.height-tileSize);
        show();
    }
    
    
}
