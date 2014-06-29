
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package program;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.*;

/**
 * Klasa Sounds odpowiada za odgrywanie dźwięku podczas gry.
 * @author Rafal
 */
public class Sounds {
    
    /**
     * Metoda playSoundtrack() odpowiada za odgrywanie ścieżki dźwiękowej w tle.
     */
    public synchronized void playSoundtrack() {
        final String url=ValueReader.SOUNDTRACK;
        new Thread(new Runnable() {
            public void run(){
                try {                    
                    InputStream soundFile=getClass().getResourceAsStream("/sound/"+url);
                    InputStream bufferedIn = new BufferedInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                    clip.open(inputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                    inputStream.close();
                    bufferedIn.close();
                    soundFile.close();
                }
                catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
    /**
     * Metoda playSound odpowiada za odgrywanie chwilowego dźwięku.
     * @param url ścieżka pliku do odtworzenia
     */
    public synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            public void run(){
                try {                    
                    InputStream soundFile=getClass().getResourceAsStream("/sound/"+url);
                    InputStream bufferedIn = new BufferedInputStream(soundFile);
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                    clip.open(inputStream);
                    clip.start();
                    inputStream.close();
                    bufferedIn.close();
                    soundFile.close();
                }
                catch (Exception e){
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}