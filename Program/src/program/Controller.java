package program;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Klasa Controller odpowiada za zbierania informacji z klawiatury podczas gry.
 */
public class Controller implements KeyListener, Runnable{
    private boolean leftPressed; /**Zmienna logiczna dotycząca przysicku lewej strzałki, obecnie nie używana. */
    private boolean rightPressed, upPressed, escPressed; /** Zmienne logiczne dla stanu naciśnięcia klawiszy*/
    private TheGame controlledGame;/**Pole przechowujące kontrolowaną grę*/
    
    public Controller(Screen screen, Charact character, TheGame game){
        controlledGame=game;
        controlledGame.player=character;
        controlledGame.display.addKeyListener(this);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    escPressed = true;
            }
            
    } 
		
		
    @Override
    public void keyReleased(KeyEvent e) {			
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {

                    upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    escPressed = false;
            }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void run() {
        controlledGame.player.runright(controlledGame.scrollingSpeed);            

        /*
        if(leftPressed){
            controlledCharacter.moveleft();
        }*/

        
        /** 
        * Wywołanie ruchu w prawo
        */
        if(rightPressed){
            int temp=controlledGame.scrollingSpeed;
            controlledGame.scrollingSpeed=controlledGame.player.speed;
            controlledGame.player.moveright();     
            controlledGame.scrollingSpeed=temp;
        }

        /** 
        * Skok
        */
        if (upPressed&&!controlledGame.player.isJumping&&!controlledGame.player.isFalling) {
           controlledGame.player.isJumping=true;
           controlledGame.currentGame.sound.playSound("jump.wav");
        }

        /** 
        * Pauza gry
        */
        if(escPressed)
            controlledGame.pause();
        
        //controlledGame.player.validate();

        try {
            Thread.sleep(15/controlledGame.scrollingSpeed);
            controlledGame.player.repaint();
        }
        catch (InterruptedException e) { }
    }
}
