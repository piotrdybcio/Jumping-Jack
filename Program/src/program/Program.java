/** 
 * @author:Piotr Dybcio, Rafal Świerbutowicz
 * @name: JumpingJack
 * @version: 1.000       
 */

package program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/** 
 * Główna klasa programu, w której zdefinowane sie instrukcje,
 *  dzięki którym następuje uruchomienie programu.
 */

public class Program{        
    Screen screen; /**Pole przechowujące główny ekran aplikacji*/
    TheGame game; /**Pole przechowujące aktualną rozgrywkę*/
    String name,ip,port; /**Pola przechowujące imię użytkownika, adres IP oraz port przez całe dzialanie programu.*/
    Sounds sound; /**Pole przechowujące obiekt służący do odtwarzania muzyki.*/
    
   
    /**
     * Funkcja pozwalająca zapisać zdobyty poziom do pliku lokalnego. Dzięki temu po ponownym uruchomieniu mapy gry nie będą się powtarzać.
     */
    public void saveLevelToFile(int n){
        FileWriter zapis;
        try {
            zapis = new FileWriter("levelInfo.co");
            BufferedWriter out = new BufferedWriter(zapis);
            out.write(n);
            System.out.println(ValueReader.SAVE_SUCCESS);
            out.close();
            zapis.close();
        } catch (IOException ex) {
            System.out.println(ValueReader.LEVEL_SAVE_FAILURE);
        }         
    }
    
    /**
     * Funkcja pozwalająca wczytać zdobyty poziom z pliku lokalnego. Dzięki temu unikamy ponownego przechodzenia tych samych map.
     */
    public int loadLevelFromFile(){
        FileInputStream file;
        try {
            file=new FileInputStream("scores.txt");
            DataInputStream in = new DataInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String linia = br.readLine();            
            br.close();
            in.close();
            file.close();
            return Integer.parseInt(linia);
        } 
        catch (FileNotFoundException ex) {
            System.out.println(ValueReader.LEVEL_SAVE_FAILURE+" "+ex);
        }
        catch(IOException e){
            System.out.println(ValueReader.LEVEL_SAVE_FAILURE+" "+e);
        }
        return 1;
    }   
        
    /**
     * Metoda pozwalająca na rozpoczęcie nowej gry. Wywołuje metodę służącą pobraniu parametów, a następnie inicjuje nową rozgrywkę.
     */
    public void newGame(){
        this.getName();
        this.game=new TheGame(this);
        screen.revalidate();
    }
        
    /**
     * Metoda pozwalająca na uzyskanie od użytkownika imienia, adresu IP oraz portu wykorzystywanego do komunikacji z aplikacją JumpingJackServer.
     * Inicjuje okno nowego użytkownika, dodaje je do głównego ekranu oraz przypisuje wartości imienia.
     * Ponadto, jeśli wybrany zostanie tryb on-line inicjuje wczytanie danych konfiguracyjnych z serwera.
     */
    void getName(){
        NewUser newUser=new NewUser(this.screen.getSize());
        this.screen.add(newUser);
        this.screen.revalidate();
        newUser.waitForEvent();
        name=newUser.name;
        ip=newUser.ip;
        port=newUser.port;
        if(!ip.equals("OFFLINE"))
            new ValueReader(this);/** Wczytywanie wartosci z pliku*/
    }
    
    /**
     * Funkcja główna programu. Inicjuje początkowe komponenty - obraz, dźwięk i ekran nowej gry.
     */
    public static void main(String[] args){
        Program program=new Program();
        program.screen=new Screen();
        program.sound=new Sounds();
        program.sound.playSoundtrack();
        program.newGame();        
    }
}
