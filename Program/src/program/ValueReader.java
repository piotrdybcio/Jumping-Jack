package program;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**Klasa umożliwjająca pobieranie danych konfiguracyjnych z serwera lub z lokalnych zasobów*/
public final class ValueReader {
    
    public static int DEFAULT_SCREEN_HEIGHT=600;
    public static int DEFAULT_SCREEN_WIDTH=800;
    public static int DEFAULT_STARTLOCX=10; 
    public static int DEFAULT_STARTLOCY=500; 
    public static int BACKGROUND_COLOR1=100;
    public static int BACKGROUND_COLOR2=100;
    public static int BACKGROUND_COLOR3=255;
    public static int DEFAULT_SCROLLING_SPEED=1;
    public static int DEFAULT_JUMP_MODE=60;
    public static int JUMP_SPEED=60;
    public static int FALLING_SPEED=6;
    public static int BLOCK_WIDTH=DEFAULT_SCREEN_WIDTH/18;
    public static int BLOCK_HEIGHT=DEFAULT_SCREEN_WIDTH/24;
    public static int TILE_SIZE=30;
    public static int TIME_START=0;
    public static int LIFE_NUMBER=5;
    public static int SPEED=4;
    
    public static String LEVEL="Poziom";
    public static String GAME_TITLE="JumpingJack - Piotr Dybcio, Rafał Świerbutowicz";
    public static String CHARACTER_PICTURE="img/char.png";
    public static String OK="OK";
    public static String EXIT="      Wyjdź      ";
    public static String TIME="Czas";
    public static String SCORE="Wynik";
    public static String NAME="Witaj";
    public static String LIFE="Życia";
    public static String GUEST="Gość";
    public static String PLAY_OFFLINE="Graj off-line";
    public static String CONNECT="Połącz i graj!";
    public static String DEFAULT_IP="127.0.0.1";
    public static String DEFAULT_PORT="1219";
    public static String GET_NAME="Podaj imię";
    public static String GET_IP="Podaj IP serwera";
    public static String GET_PORT="Podaj port";
    public static String RESUME="Wróć do gry";
    public static String NEW_GAME="   Nowa gra  ";
    public static String SAVE="   Zapisz    ";
    public static String SOUNDTRACK="soundtrack.wav";
    public static String SCORE_BOARD="Najlepsze wyniki";
    public static String YOUR_SCORE="Twój wynik";
    public static String SCORES="Wyniki 5. najlepszych:";
    public static String SCORED="Gracz: 0";
    public static String SCORE1="Gracz: 0";
    public static String SCORE2="Gracz: 0";
    public static String SCORE3="Gracz: 0";
    public static String SCORE4="Gracz: 0";
    public static String SCORE5="Gracz: 0";
    public static String COMMUNICATE_IS_OFFLINE_PT1="Utracono połączenie!";
    public static String COMMUNICATE_IS_OFFLINE_PT2="Wynik off-line:";
    public static String LEVEL_SAVE_FAILURE="Nie udało się zapisać poziomu do pliku";
    public static String SAVE_SUCCESS="Zapis zakończony sukcesem";
    
    public String linia;
    public String parametr;
    public String wartosc;
    public char znak;
    public int index;
    FileWriter zapis;
    String s;
    
            
    /**Metoda do wysyłania zapytania i odbierania plików z serwera*/
    public void reciveConfigurationFromServer(Program prog){
        try{
            Socket socket = new Socket(prog.ip , Integer.parseInt(prog.port));

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("REQUEST_VALUES");
            System.out.println("Request for parameters sent");

            InputStream is = socket.getInputStream();
            BufferedReader bufor = new BufferedReader(new InputStreamReader(is));
            this.loadConfiguration(bufor);
            try{
                socket.close();
                System.out.println("Socet close :)");
            }
            catch(Exception e){System.out.println("Błąd zamykania połączenia.");}  
        }
        catch(IOException e){
            System.out.println("Błąd nawiązania połączenia z serwerem. Pliki konfiguracyjne pobrane z lokalnych źródeł :)");
            this.reciveConfigurationFromLocalArea();  
        } 
    }
    
    
    
    /**Metoda pobierająca dane z lokalnych zasobów (gdy serwer jest niedostępny)*/
    public void reciveConfigurationFromLocalArea(){
        InputStream plik=getClass().getResourceAsStream("/data/params.txt");
        InputStreamReader reader = new InputStreamReader(plik);
        BufferedReader bufor=new BufferedReader(reader);
        this.loadConfiguration(bufor);  
        try{
           plik.close();
           System.out.println("File close :)");
        }
        catch(Exception e){System.out.println("Błąd zamykania pliku.");}     
    }
    
    
    
    /**Zamykanie gniazda Klienta*/
    public void closeConection(Socket socket){
        
    }

        
    /**
     * Metoda ładująca pobraną konfiguracje z serwera lub z lokalnych źródeł
     */
    public void loadConfiguration(BufferedReader bufor){
        try{
            while((linia = bufor.readLine()) !=null ){
                linia.toUpperCase();
                znak=linia.charAt(0);
                if(znak!='#'&&znak!='\n'){
                    index=linia.indexOf("=");
                    parametr=linia.substring(0,index);
                    wartosc=linia.substring(index+1);
                    
                    switch(parametr){

                        case "DEFAULT_SCREEN_HEIGHT":
                            DEFAULT_SCREEN_HEIGHT=Integer.parseInt(wartosc);
                            break;
                        case "DEFAULT_SCREEN_WIDTH":
                            DEFAULT_SCREEN_WIDTH=Integer.parseInt(wartosc);
                            break;
                        case "DEFAULT_STARTLOCX":
                            DEFAULT_STARTLOCX=Integer.parseInt(wartosc);
                            break;
                        case "DEFAULT_STARTLOCY":
                            DEFAULT_STARTLOCY=Integer.parseInt(wartosc);
                            break;
                        case "BACKGROUND_COLOR1":
                            BACKGROUND_COLOR1=Integer.parseInt(wartosc);
                            break;
                        case "BACKGROUND_COLOR2":
                            BACKGROUND_COLOR2=Integer.parseInt(wartosc);
                            break;
                        case "BACKGROUND_COLOR3":
                            BACKGROUND_COLOR3=Integer.parseInt(wartosc);
                            break;
                        case "JUMP_SPEED":
                            JUMP_SPEED=Integer.parseInt(wartosc);
                            break;
                        case "FALLING_SPEED":
                            FALLING_SPEED=Integer.parseInt(wartosc);
                            break;
                        case "SPEED":
                            SPEED=Integer.parseInt(wartosc);
                            break;
                        case "LIFE_NUMBER":
                            LIFE_NUMBER=Integer.parseInt(wartosc);
                            break;
                    }
                }
            }
        }
        catch(IOException e){System.out.println("Błąd odczytu z pliku.");}      
    }
    
    
    /**
     * Metoda służąca do zmiany ustawień domyślnych, na ustawnienia pobrane z serwera lub z lokalnych źródeł
     */
    public ValueReader(Program prog){
        reciveConfigurationFromServer(prog);        
    }
                    
}
