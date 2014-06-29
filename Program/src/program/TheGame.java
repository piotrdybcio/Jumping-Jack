/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Rafal
 */
public class TheGame implements Runnable{
    Program currentGame;
    Timer timer;
    Charact player;
    Screen display;
    ScoreWindow scoreWindow;
    Controller controller;
    int time, score, lifes, scrollingSpeed, jumpMode, meantime, level;
    boolean run=true;
    Map map;
    
    public TheGame(Program game){
        scrollingSpeed=ValueReader.DEFAULT_SCROLLING_SPEED;
        jumpMode=ValueReader.DEFAULT_JUMP_MODE;
        currentGame=game;
        display=game.screen;
        display.setResizable(false); 
        player=new Charact(this, currentGame.name);
        level=currentGame.loadLevelFromFile();
                
        map=new Map(this, level);
        scoreWindow=new ScoreWindow(player.name,display);
        controller=new Controller(display, player,this);
        lifes=ValueReader.LIFE_NUMBER;
        score=0;
        time=0;
        meantime=0;
        timer=new Timer(1000, updateClockAction);
        timer.start();
        run();
    }
    public void resetMap(){
        timer.stop();
        run=false;
        player.setVisible(false);
        try{
            Thread.sleep(300);
        }catch(InterruptedException e){}
        player.setVisible(true);
        try{
            Thread.sleep(300);
        }catch(InterruptedException e){}
        player.setVisible(false);
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){}
        player.setVisible(true);
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){}
        map.hide();
        player.setVisible(false);
        
        
        
        if(lifes>=0){
            map=new Map(this, level);
            map.show();
            timer.start();
            scoreWindow.updateTime(time);
            player.setVisible(true);
            timer.start();
            run=true;
        }
        else{
            player.setVisible(false); 
            scoreWindow.setVisible(false);
            if(!currentGame.ip.equals("OFFLINE")){
                ScoreSend ss=new ScoreSend(this);
                ss.reciveScoreFromServer();
            }
            ScoreBoard scoreBoard=new ScoreBoard(this);
            display.add(scoreBoard);
            display.revalidate();
            scoreBoard.waitForEvent();
            currentGame.newGame();
        }
        
    }
    public void finish(){
        timer.stop();
        score+=500-(time-meantime);
        scoreWindow.updateScore(score);
        meantime=time;       
        timer.restart();
        run=false;
        player.setVisible(false);
        map.hide();
        scoreWindow.updateLevel(++level);
        map=new Map(this, level);
        map.show();
        timer.start();
        scoreWindow.updateTime(time);
        player.setVisible(true);
        timer.start();
        run=true;
    }
    
    public void pause(){
        timer.stop();
        run=false;
        player.setVisible(false);
        map.hide();
        Menu menu=new Menu(display.getSize(null));
        display.add(menu);
        display.revalidate();
        menu.setVisible(true);
        menu.waitForEvent();
        if(menu.result=="RESUME"){
            map.show();
            player.setVisible(true);
            timer.start();
            run=true;
        }
        if(menu.result=="NEW"){
            scoreWindow.setVisible(false);
            currentGame.newGame();
        }
        if(menu.result=="SAVE"){
            currentGame.saveLevelToFile(level);
            scoreWindow.setVisible(false);
            currentGame.newGame();
            System.exit(0);
        }
    }
    
    ActionListener updateClockAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            time++;
            scoreWindow.updateTime(time);
        }
    };
    
    @Override
    public void run() {
        while(run){
            controller.run();
            player.movedown();
            player.moveup();
            Thread.currentThread().yield();  
        }   
    } 
}
