package program;

import javax.swing.*;
import java.awt.*;

public class Charact extends Component{
    int sizeX, sizeY, prop, jumpSpeed, defaultJumpSpeed, speed, fallingSpeedValue,fallingSpeed;
    String name;
    private Image img;
    private TheGame currentGame;
    private Rectangle rect, temp;
    boolean isFalling, isJumping;
    private int tileSize;
    
    
    /**
     * Konstruktor klasy bohatera. Zawiera instrukcje inicjujące jego obraz, kształt
     * @param game Gra w której ma uczestniczyć bohater.
     * @param imie 
     */
    public Charact(TheGame game, String imie){
        currentGame=game;
        ImageIcon imic = new ImageIcon(getClass().getResource("/images/char.png"));
        img = imic.getImage();
        prop=imic.getIconWidth()/imic.getIconHeight();
        name=imie;
        rect=new Rectangle(0,0,0,0);
        isFalling=true;
        isJumping=false;
        jumpSpeed=defaultJumpSpeed=ValueReader.JUMP_SPEED;
        speed=ValueReader.SPEED;
        fallingSpeed=fallingSpeedValue=ValueReader.FALLING_SPEED;
        

        game.display.add(this);
        game.display.validate();
    }
    
   /**
    * Metoda pozwalająca na ustawienie mocy skoku.
    * @param i Planowana moc skoku.
    */
    public void setJumpSpeed(int i){
        jumpSpeed=defaultJumpSpeed=i;
    }
    
    /**
     * Metoda pozwalająca na ustawienie rozmiaru bohatera.
     * @param i Wysokość płytki, względem której ustalane są rozmiary bohatera.
     */
    public void setSize(int i){
        tileSize=i;
        rect.height=sizeY=2*tileSize;
        rect.width=sizeX=prop*sizeY;
        rect.width-=20;
        rect.height-=10;
    }
    
    
    /**
     * Metoda pozwalająca na ustawienie bohatera
     * @param x wartość w osi X
     * @param y wartość w osi Y
     */
    public void setPosition(int x, int y){
        rect.x=x+10;
        rect.y=y-(sizeY/2)+5;        
    }
    
    
    /**
     * Metoda odpowiadająca za wykrywanie kolizjii z otoczeniem. Zwraca prawdę, jeśli wykryte jest zderzenie z elementem statycznym, przez który nie może przejść.
     * Jeśli wykryte zostanie zderzenie z obiektem dynamicznym następuje obsługa zdarzenia związanego z tym obiektem.
     * @param i Planowany ruch w osi X
     * @param j Planowany ruch w osi Y
     * @return Prawda jeśli ulegnie zderzeniu, fałsz jeśli nie.
     */
    private boolean testForColisions(int i, int j){
        float tempX=new Float((rect.x+i+currentGame.map.offsetX)/(float)tileSize);
        float tempY=new Float((rect.y+j)/(float)tileSize);
        
        if(rect.y+j>=currentGame.map.elements.length*tileSize){
            currentGame.lifes--;
            currentGame.scoreWindow.updateLifes(currentGame.lifes);
            currentGame.resetMap();
        }
        
        Rectangle temp=new Rectangle(rect);
        temp.y+=j;
        temp.x+=i+currentGame.map.offsetX;
        for(int y=Math.max((int)tempY-1,0);y<Math.min((int)tempY+3+j/tileSize,currentGame.map.elements.length-1);y++)
            for(int x=Math.max((int)tempX-2,0);x<Math.min((int)tempX+3,currentGame.map.elements[0].length-1)+x/tileSize;x++){
                if(currentGame.map.sprites[y][x]!=null)
                    if(temp.intersects(currentGame.map.sprites[y][x].rect))
                        switch(currentGame.map.sprites[y][x].type){
                            case "O":
                                currentGame.score+=10;
                                currentGame.map.sprites[y][x].setVisible(false);
                                currentGame.map.sprites[y][x]=null;
                                currentGame.scoreWindow.updateScore(currentGame.score);
                                break;
                            case "Q":
                            case "F":
                                currentGame.lifes--;
                                currentGame.scoreWindow.updateLifes(currentGame.lifes);
                                currentGame.resetMap();
                                break;
                            case "V":
                                currentGame.lifes++;
                                currentGame.map.sprites[y][x].setVisible(false);
                                currentGame.map.sprites[y][x]=null;
                                currentGame.scoreWindow.updateLifes(currentGame.lifes);
                                break;
                            case "I":
                                currentGame.finish();
                                break;
                        }
                if(currentGame.map.elements[y][x]!=null)
                    if(temp.intersects(currentGame.map.elements[y][x].rect))
                        return true;
            }
        return false;
    }
    
    /**
     * Metoda obsługująca wymuszony ruch w prawo.
     * @param i to prędkość ruchu
     */
    public void runright(int i){
        if(!testForColisions(i,0)){
            currentGame.map.scrollRight(i);
        }
        else if(rect.x-i<0){
            currentGame.lifes--;
            currentGame.scoreWindow.updateLifes(currentGame.lifes);
            currentGame.resetMap();
        }
        else
            rect.x-=i;
        
    }
    
    /**
     * Metoda obsługująca ruch w prawo.
     */
    public void moveright(){
        if(!(rect.x<currentGame.display.getSize().width*0.8)){
            if(currentGame.map.ableToScroll)
                currentGame.map.scrollRight(speed);
            else
                currentGame.finish();
        }
        else if(!testForColisions(speed,0)){
            rect.x+=speed;
        }   
    }
    
    /** 
     * Metoda obsługująca ruch bohatera w lewo. Nie jest używana w tej koncepcji gry.
     */
    public void moveleft(){
        if((!testForColisions(-2*speed,0))&&(rect.x-speed>currentGame.display.getSize().width*0.2)){
            rect.x-=speed;
        }
    }
    
    /**
     * Metoda obsługująca grawitacyjne opadanie.
     */
    public void movedown(){
        if(!isJumping){
            if(!testForColisions(0,fallingSpeed)){
                isFalling=true;
                rect.y+=fallingSpeed;
                fallingSpeed++;
            }
            else{
                isFalling=false;
                fallingSpeed=fallingSpeedValue;
            }
        }      
    }
    
    
    /**
     * Metoda obsługująca skok.
     */
    public void moveup(){
        if(isJumping&&jumpSpeed>0){
            if(!testForColisions(0,-jumpSpeed/5)){
                rect.y-=jumpSpeed/5;
                jumpSpeed-=2;               
            }
            else
                jumpSpeed=0;
        }
        else if(jumpSpeed==0){
            jumpSpeed=defaultJumpSpeed;
            isFalling=true;
            isJumping=false;
        }
    }

    @Override
    public void paint(Graphics g) {        
        g.drawImage(img, rect.x-10, rect.y-5, sizeX, sizeY, null);
    }
}
