import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;

/**
 * Represents on RAIDA server
 * 
 * @author Sean Worthington
 * @version 1/1/2017
 */
public class DetectionAgent
{
    // instance variables
    public int readTimeout;//how many mili seconds before saying fuck it, we are done waiting. 
    public int RAIDANumber;
    public String fullUrl;

    //Detection Status
    public String lastDetectStatus = "notdetected";//error, notdetected, pass, fail
    public long dms = 0; //ms to detect
    
    //Ticket Status
    public String lastTicket = "empty";
    public String lastTicketStatus = "empty";//ticket, fail, error
    
    //Fix it status
    public String lastFixStatus = "empty";//ticket, fail, error
    
    //General 
    public String lastRequest = "empty";//Last GET command sent to RAIDA
    public String lastResponse = "empty";//LAST JSON recieved from the RAIDA

  
    /**
     * DetectionAgent Constructor
     *
     * @param readTimeout A parameter that determines how many milliseconds each request will be allowed to take
     * @param RAIDANumber The number of the RAIDA server 0-24
     */
    public DetectionAgent( int RAIDANumber, int readTimeout  )
    {
        this.RAIDANumber = RAIDANumber;
        this.fullUrl = "https://RAIDA"+ RAIDANumber + ".cloudcoin.global/service/";
        this.readTimeout = readTimeout;
    }//Detection Agent Constructor

    /**
     * Method detect
     *
     * @param nn An int thatis the coin's Network Number 
     * @param sn An int that is the coin's Serial Number
     * @param an A String that is the coin's Authenticity Number (GUID)
     * @param pan A String that is the Proposed Authenticity Number to replace the AN.
     * @param d An int that is the Denomination of the Coin
     * @return Returns pass, fail or error. 
     */
    public String detect( int nn, int sn, String an, String pan, int d){
            this.lastRequest = this.fullUrl + "detect?nn="+nn+"&sn="+sn+"&an="+an+"&pan="+pan+"&denomination="+d;
           // System.out.println(this.lastRequest);
            Instant before = Instant.now();
            try{
                this.lastResponse = getHtml( this.lastRequest );
            }catch( IOException ex ){
                lastDetectStatus = "error";
                return "error";
            }
            Instant after = Instant.now(); this.dms = Duration.between(before, after).toMillis();
            if( this.lastResponse.contains("pass") ){ 
                lastDetectStatus = "pass";
                return "pass";
            }else if( this.lastResponse.contains("fail") && this.lastResponse.length() < 200 )//less than 200 incase their is a fail message inside errored page
            {  lastDetectStatus = "fail"; 
               return "fail";
            }else{ 
                lastDetectStatus = "error"; 
                return "error";
            }
    }//end detect

    public String get_ticket( int nn, int sn, String an, int d )throws MalformedURLException, IOException  { //Will only use ans to fix
        this.lastRequest = fullUrl + "get_ticket?nn="+nn+"&sn="+sn+"&an="+an+"&pan="+an+"&denomination="+d; 
       
            Instant before = Instant.now();
            this.lastResponse = getHtml( this.lastRequest );
            
             //System.out.println(this.lastRequest);
        //System.out.println(this.lastResponse);
            if ( this.lastResponse.contains("ticket") ){
                String[] KeyPairs = this.lastResponse.split(",");
                String message = KeyPairs[3];      
                int startTicket = ordinalIndexOf( message, "\"", 3);
                int endTicket = ordinalIndexOf( message, "\"", 4);
                this.lastTicket = message.substring(startTicket + 1, endTicket);
                this.lastTicketStatus = "ticket";
                Instant after = Instant.now(); this.dms = Duration.between(before, after).toMillis();
                return this.lastTicket;
            }//end if
         
        return "error";
    }//end get ticket

    public String fix( int[] triad, String m1, String m2, String m3, String pan ){
 
        this.lastFixStatus = "error"; 
        int f1 = triad[0];
        int f2 = triad[1];
        int f3 = triad[2];
        this.lastRequest = fullUrl+"fix?fromserver1="+f1+"&message1="+m1+"&fromserver2="+f2+"&message2="+m2+"&fromserver3="+f3+"&message3="+m3+"&pan="+pan;
        //System.out.println(this.lastRequest);

        try{
            Instant before = Instant.now();
            this.lastResponse = getHtml( this.lastRequest );
            
            Instant after = Instant.now(); this.dms = Duration.between(before, after).toMillis();
           // System.out.println(this.lastResponse + " " + this.dms );
        }catch( MalformedURLException ex ){//quit
          //  System.out.println(ex + " " +this.lastResponse);
            return "error";
        } catch( IOException ex ){
          //  System.out.println(ex + " " +this.lastResponse);
            return "error" ;
        }
    
        if( this.lastResponse.contains("success") ){ 
            this.lastFixStatus = "success"; 
            return "success"; 
        }
        
        return "error"; 
    }//end fixit

    
    //Helper Methods
    private String getHtml(String url_in) throws MalformedURLException, IOException {
        
        URL cloudCoinGlobal = new URL(url_in);
        URLConnection conn = cloudCoinGlobal.openConnection();
        conn.setReadTimeout( readTimeout ); //set for two seconds
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        InputStream input = conn.getInputStream();

        StringBuilder sb = new StringBuilder();
        int c;
        while((( c = input.read()) != -1))
        {
            sb.append((char)c); 
        }//end while   
        input.close();
        return sb.toString();
    }//end get url
    
    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

}
