/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpingjackserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *Klasa pozwalająca na sieciową implementację.
 */
public class JumpingJackServer{
    private Screen screen;/** Pole przechowujące aktualnie używany obiekt klasy Screen - umożliwiający komunikację aplikacji z urzytkownikiem.*/
    private String[] scoreList; /** Pole przechowujące 5 najlepszych wyników.*/
    
    
    /**
     * Metoda umożliwiająca wczytanie zalecanej konfiguracji Klienta z przestrzeni dyskowej.
     */
    private void loadConfiguration(PrintWriter pr){
        String linia=null;
        InputStream plik=getClass().getResourceAsStream("/data/params.txt");
        InputStreamReader reader = new InputStreamReader(plik);
        BufferedReader bufor=new BufferedReader(reader);
        try{
            while((linia = bufor.readLine()) !=null ){
                pr.println(linia);
            }
            plik.close();
        }
        catch(IOException e){
            System.out.println("Błąd odczytu/zapisu z pliku.");
        }
    }
    
    /**
     * Metoda odpowiedzialna za wysyłanie tablicy 5 najlepszych wyników.
     */
    private void sendScore(PrintWriter pr){
        for(int x=0; x<5;x++){
            if(scoreList[x]!=null)
                pr.println(scoreList[x]);
            else
                pr.println("Gracz 0");
        }
            
    }
    
    /**
     * Metoda umożliwiająca wczytanie tablicy 5 najlepszych wyników z przestrzeni dyskowej.
     */
    private int loadScore(){
        scoreList=new String[5];
        try{
            FileInputStream file;
            file=new FileInputStream("scores.txt");
            try (DataInputStream in = new DataInputStream(file); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                String linia;
                for(int i=0;i<5;i++){
                    linia = br.readLine();
                    if(linia!=null)
                        scoreList[i]=linia;
                    else
                        scoreList[i]="";
                }
            }
            file.close();
            System.out.println("Wczytana tablica");
            for(int i=0;i<5;i++)
                System.out.println(scoreList[i]);
            System.out.println("Return 0");
            return 0;
        }
        catch(IOException e){return 1;}
    }
    
    /**
     * Metoda pozwalająca na dodanie nowego wyniku do tablicy najlepszych 5 wyników.
     */
    private void saveScore(String s){
        System.out.println("Scorelist1"+scoreList[0]);
        System.out.println("analyzing");
        int index=s.indexOf("#");
        boolean success=false;
        String[] temp=new String[5];
        String name=s.substring(0,index);
        String score=s.substring(index+1);
        System.out.println("entering");
        for(int i=0, j=0;i<5;i++){
            System.out.println("i: "+i+", j:"+j);
            System.out.println("Scorelist"+scoreList[i]);
            if(scoreList[i]!=null){
                int tempIndex=scoreList[i].indexOf(" ");
                String tempName=scoreList[i].substring(0,tempIndex);
                String tempScore=scoreList[i].substring(tempIndex+1);
                System.out.println("name: "+tempName+", score: "+tempScore);
                if(Integer.parseInt(tempScore)>Integer.parseInt(score)){
                   temp[i]=scoreList[i];
                   j++;
                }    
                else if((Integer.parseInt(tempScore)<=Integer.parseInt(score))&&!success){
                    temp[i]=name+" "+score;
                    success=true;
                }
                else{
                    temp[i]=scoreList[j];
                    j++;
                }
            }
            else if (i==0){
                temp[0]=name+" "+score;
            }
        }
        scoreList=temp;

        try{
            try (FileOutputStream file2 = new FileOutputStream("scores.txt"); DataOutputStream out = new DataOutputStream(file2)) {
                BufferedWriter br2=new BufferedWriter(new OutputStreamWriter(out));
                for(int i=0;i<5;i++){
                    if(scoreList[i]!=null)
                        br2.write(scoreList[i]+"\n");
                }
                br2.close();
            }
        }
        catch(IOException e){}        
    }
    
    
    /*
     * Metoda wczytująca mapy z przestrzeni dyskowej.
     */
    private void loadMaps(PrintWriter pr, String s){//, String s){
        String linia=null;
        System.out.println(s);
        int maps = Integer.parseInt(s);
        System.out.println(maps);
        InputStream plik=getClass().getResourceAsStream("/maps/"+maps+".jjmap");
        InputStreamReader reader = new InputStreamReader(plik);
        BufferedReader bufor=new BufferedReader(reader);
        try{
            while((linia = bufor.readLine()) !=null ){
                pr.println(linia);     
            }
            System.out.println("przesłano plik z mapą z lokalizacji:  /maps/"+maps+".jjmap");
        }
        catch(IOException e){
            System.out.println("Błąd odczytu z pliku.");
        }
        try{
            plik.close();
        }
        catch(IOException e){
            System.out.println("Błąd zamykania pliku.");
        }
    }
    
    /**
     * Metoda inicjująca serwer.
     * @throws UnknownHostException 
     */
    public JumpingJackServer() throws UnknownHostException{
        screen=new Screen(this);
        loadScore();
    }    
    
    /**
     * Główna metoda klasy JumpingJackServer, odpowiadająca za komunikację sieciową z klientami sieciowymi.
     */
    public void run() {
            ServerSocket serverSocket;
            String port = screen.jTextField1.getText();
            try{
                serverSocket = new ServerSocket(Integer.parseInt(port));
                serverSocket.setSoTimeout(500);
                try{
                    Socket socket = serverSocket.accept();
                    System.out.println("Połączono");
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String fromClient = br.readLine();
                    OutputStream os = socket.getOutputStream();
                    int index=fromClient.indexOf(" ");
                    PrintWriter pw = new PrintWriter(os, true);
                    if(index!=-1){
                        String request=fromClient.substring(0,index);
                        String parametr=fromClient.substring(index+1);
                        System.out.println(request);
                        if(request.equals("REQUEST_MAPS")){
                            System.out.println("Załaduje mape nr "+parametr);
                            this.loadMaps(pw,parametr);
                        }
                        else if(request.equals("SEND_SCORE")){
                            this.saveScore(parametr); 
                            System.out.println("Scored: "+ parametr);
                        }
                    }
                    if(fromClient.equals("REQUEST_VALUES"))
                        this.loadConfiguration(pw); //Przesyłanie konfiguracji

                    if(fromClient.equals("REQUEST_SCORE"))
                        this.sendScore(pw); //Przesyłanie tablicy wyników  

                    System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

                    socket.close();
                }
                catch (Exception e) {
                    System.err.println("Server exception: " + e);
                }


            }
            catch (Exception e) {}
    }
    /**
     *Metoda inicjująca serwer.
     */
    public static void main(String[] args) throws UnknownHostException {
        JumpingJackServer serverJJ=new JumpingJackServer();
    }
}
    

