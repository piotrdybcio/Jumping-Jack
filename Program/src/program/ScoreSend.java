
package program;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa ScoreSend odpowiada za transmisje wyników między klientem i serwerem.
 */
public final class ScoreSend {
    TheGame currentGame; /** Gra, której dotyczy zapis wyniku.*/
    
    /**
     * Metoda odpowiadająca za wysłanie wyniku do serwera.
     */
    public void sendScoredToServer()
    {
        try{
            Socket socket = new Socket(currentGame.currentGame.ip , Integer.parseInt(currentGame.currentGame.port));

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("SEND_SCORE "+ currentGame.player.name+"#"+currentGame.score);
            System.out.println("Send Score: "+"SEND_SCORE "+ currentGame.player.name+"#"+currentGame.score);         
        }
        catch(IOException e){
            System.out.println("Błąd nawiązania połączenia z serwerem. Pliki zapisany zostanie lokalnie :)");
            saveScoreInLocalArea();
            
        }
    }
    
    
    /**
     * Zapisywanie wyniku lokalnie.
     */
    private void saveScoreInLocalArea(){
        FileWriter zapis;
        try {
            zapis = new FileWriter("score_table.txt");
            BufferedWriter out = new BufferedWriter(zapis);
            out.write(currentGame.player.name+" "+currentGame.score+" "+currentGame.level);
            System.out.println(ValueReader.SAVE_SUCCESS);    
            out.close();                     
            }
        catch(Exception e){System.out.println("Błąd z zapisem do pliku" + e);}        
    }
    
    
    /**
     * Metoda odpowiadająca za pobranie tablicy wyników z serwera.
     */
    public void reciveScoreFromServer(){
        try{
            Socket socket = new Socket(currentGame.currentGame.ip , Integer.parseInt(currentGame.currentGame.port));

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("REQUEST_SCORE");
            System.out.println("Request Score");
            
            InputStream is = socket.getInputStream();
            BufferedReader bufor = new BufferedReader(new InputStreamReader(is));
            for(int i=0;i<5;i++){
                String s=bufor.readLine();
                System.out.println(s);
                if(s!=null&&i==0)
                    ValueReader.SCORE1=s;
                if(s!=null&&i==1)
                    ValueReader.SCORE2=s;
                if(s!=null&&i==2)
                    ValueReader.SCORE3=s;
                if(s!=null&&i==3)
                    ValueReader.SCORE4=s;
                if(s!=null&&i==4)
                    ValueReader.SCORE5=s;
            }
            socket.close();
        }
        catch(IOException e){}
    }
    
    /**
     * Inicjacja obiektu pozwalającego na przesyłanie wyników gry.
     * @param gm to gra, od której pochodzi przesyłany wynik
     */
    public ScoreSend(TheGame gm)
    {
        currentGame=gm;
        sendScoredToServer();
    }
    
    
}
