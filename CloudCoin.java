import java.security.SecureRandom;
import java.io.*;
//import java.util.Scanner;
import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Scanner;

/**
 * Creats a CloudCoin
 * 
 * @author Sean H. Worthington
 * @version 1/8/2016
 */
public class CloudCoin
{
    // instance variables - replace the example below with your own
    public int nn;//Network Numbers
    public int sn;//Serial Number
    public String[] ans = new String[25] ;//Authenticity Numbers
    public String[] pans = new String[25];
    public String[] pastStatus = new String[25];//The results of past detection -fail, pass, notdetected (could not connect to raida)
    public String ed; //Expiration Date expressed as a hex string like 97e2 Sep 2016
    public String edHex;//ed in hex form. 
    public int hp;//HitPoints (1-25, One point for each server not failed)
    public String aoid;//Account or Owner ID
    public String fileName;
    public String json;
    public byte[] jpeg;
    public static final int YEARSTILEXPIRE = 2;
    public String extension; //"suspect", "bank", "lost", "fracked", "counterfeit"
    public String[] gradeStatus = new String[3];//What passed, what failed, what was undetected

    /**
     * CloudCoin Constructor
     * This is used for importing new coins from the outside
     * @param nn Network Number
     * @param sn Serial Number
     * @param ans Authenticity Numbers
     * @param ed Expiration Date
     * @param aoid A String like:Imported On: 6-12-2017 c:\fdkjfdkjfd\ffilename From Note: kjj From File: " >pppppppfpppppfffppppppfpp<" 
     */
    public CloudCoin(int nn, int sn, String[] ans, String ed, String aoid, String extension  )
    { // initialise instance variables
        this.nn = nn;
        this.sn = sn;     
        this.ans = ans;
        this.ed = ed;
        this.hp = 25;
        this.aoid = aoid;
        this.fileName = getDenomination() +".CloudCoin." + this.nn +"."+ this.sn + ".";
        this.json = "";
        this.jpeg = null;
        for(int i = 0; i< 25; i++){
            pans[i] = generatePan();
            pastStatus[i] = "undetected";
        }//end for each pan
    }

    /**
     * CloudCoin Constructor
     *
     * @param loadFilePath Loads files internally so they can be moved around. 
     * Throws file not found exception or ioexception
     */
    public CloudCoin( String loadFilePath ) throws FileNotFoundException, IOException { //If loading from inside
        /*SEE IF FILE IS JPEG OR JSON*/
        int indx = loadFilePath.lastIndexOf('.');
        if (indx > 0) {
            extension = loadFilePath.substring(indx+1);
        }
        extension = extension.toLowerCase();

        //System.out.println("Loading file: " + loadFilePath);
        if( extension.equals("jpeg") || extension.equals("jpg")){//JPEG
            FileInputStream fis;
            int y = 0;
            char c;
            byte[] jpegHeader = new byte[455];
            String wholeString ="";

            try {
                fis = new FileInputStream( loadFilePath );
                y=fis.read(jpegHeader);// read bytes to the buffer
                wholeString = toHexadecimal( jpegHeader );// System.out.println(wholeString);
                fis.close(); 
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            int startAn, endAn;
            startAn = 40; endAn = 72;
            for(int i = 0; i< 25; i++){
                ans[i] = wholeString.substring( startAn +(i*32), endAn +(i*32) ); // System.out.println(i +": " +ans[i]);
            }//end for

            this.aoid = wholeString.substring( 840, 895 );
            this.hp = 25;//Integer.parseInt(wholeString.substring( 896, 896 ), 16);
            this.ed = wholeString.substring( 898, 902   );
            this.nn = Integer.parseInt(wholeString.substring( 902, 904 ), 16);
            this.sn = Integer.parseInt(wholeString.substring( 904, 910 ), 16);

            for(int i = 0; i< 25; i++){
                this.pans[i] = generatePan();
                this.pastStatus[i] = "undetected";
            }//end for each pan
        }else{//json image

            String jsonData = "";
            BufferedReader br = null;
            try {
                String line;
                br = new BufferedReader(new FileReader( loadFilePath ));
                while ((line = br.readLine()) != null) {
                    jsonData += line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }//end try to read Buffered reader
       for(int i = 0; i< 25; i++){
                this.pans[i] = generatePan();
                this.pastStatus[i] = "undetected";
            }//end for each pan
            String[] stringParts = jsonData.split("\""); 

           //  for(int i = 0; i< stringParts.length; i++){
             //    System.out.println("Part " + i + ": " +stringParts[i] );
            // }//for each jsonData part
            this.nn = Integer.parseInt(stringParts[5]);
            this.sn = Integer.parseInt(stringParts[9]);
            for(int i = 0; i < 25; i++){
                this.ans[i] = stringParts[ i*2+13 ];
            }//end of each an
            this.ed = stringParts[65];
            int indexOfFirstAoidSquareBracket = ordinalIndexOf( jsonData, "[", 3);
            int indexOfLastAoidSquareBracket = ordinalIndexOf( jsonData, "]", 2);
            this.aoid = jsonData.substring( indexOfFirstAoidSquareBracket+1, indexOfLastAoidSquareBracket );
            //AOID will be wrtten in human readabiltyif there is a status it will be written like:
            //"From File: c:\fdkjfdkjfd\ffilename"
            //"From Note:  kjj"
            //Imported On: 6-12-2017"
            this.hp = 25;
            //Check to see if the CloudCoin's last status was saved
            if( this.aoid.contains(">") &&  this.aoid.contains("<")){//aoid has some past status info
                //"Status" >pppppppfpppppfffppppppfpp<" where p are pass and f are fail.
                int indexStartOfStatus = ordinalIndexOf( this.aoid, ">", 0);
                int indexEndOfStatus = ordinalIndexOf( this.aoid, "<", 0);
                String rawStatus = aoid.substring(indexStartOfStatus +1, indexEndOfStatus );
                this.hp = 25;
                if(rawStatus.length() == 25){}//end if status length is 25
                for(int j = 0; j<25; j++){
                    if( rawStatus.charAt(j) == 'p') { 
                        this.pastStatus[j] = "pass";
                    }else if(rawStatus.charAt(j) == 'f'){
                        this.pastStatus[j] = "fail";
                        this.hp--;
                    }else if(rawStatus.charAt(j) == 'e'){
                        this.pastStatus[j] = "error";
                    }else{
                        this.pastStatus[j] = "notdetected";
                    }
                }//end for each status code 
            }else{
                for(int j = 0; j<25; j++){
                    this.pastStatus[j] = "notdetected";
                }//end for each status code 
            }//end if has status

        }//end if if jpg
        this.fileName = getDenomination() +".CloudCoin." + this.nn +"."+ this.sn + ".";
        this.json = "";
        this.jpeg = null;
 
    }//end new cc based on file content

    /**
     * Returns the denomination of the money based on the serial number
     * 
     * @param  sn Serial Numbers 
     * @return  1, 5, 25, 100, 250
     */
    public int getDenomination() 
    {
        int nom = 0;
        if(this.sn < 1 ){  nom = 0;}
        else if(this.sn < 2097153) {  nom = 1; } 
        else if (this.sn < 4194305) { nom = 5; } 
        else if (this.sn < 6291457) { nom = 25; } 
        else if (this.sn < 14680065) { nom = 100; } 
        else if (this.sn < 16777217) { nom = 250; } 
        else { nom = '0'; }
        return nom;
    }


    /**
     * Method setJSON creates JSON text version of the coin that can be written to file.
     *
     * @return The return value is a String of JSON that can be written to hard drive. 
     */
    public String setJSON(){   
        this.json = "{" + System.getProperty("line.separator");
        json +=   "\t\"cloudcoin\": [{" + System.getProperty("line.separator") ;
        json += "\t\t\"nn\":\"1\"," + System.getProperty("line.separator");
        json +="\t\t\"sn\":\""+ sn + "\"," + System.getProperty("line.separator");
        json += "\t\t\"an\": [\"";
        for(int i = 0; i < 25; i++){
            json += ans[i];
            if( i == 4 || i == 9 || i == 14 || i == 19){
                json += "\"," + System.getProperty("line.separator") + "\t\t\t\"";
            }else if( i == 24){
                //json += "\""; last one do nothing
            }
            else
            {//end if is line break
                json += "\",\"";
            }//end else
        }//end for 25 ans
        json += "\"]," + System.getProperty("line.separator");
        json += "\t\t\"ed\":\"9-2016\"," + System.getProperty("line.separator");
        json += "\t\t\"aoid\": []" + System.getProperty("line.separator");
        json += "\t}] "+ System.getProperty("line.separator"); 
        json += "}";  

        //Allways change expiration date when saving (not a truley accurate but good enought 
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        year = year + YEARSTILEXPIRE;
        String expDate = month + "-" + year;
        json.replace("9-2016", expDate );
        return this.json;

    }//end get JSON

    /**
     * Method setJpeg creates an array of bytes that can be written to file to make a jpg image based on the CloudCoin
     *
     * @param rootFolder This points to the template that will be used to make the jpg image. Templates can be customized.
     */
    public void setJpeg( String rootFolder){
        byte[] returnBytes =  null;
        //Make byte array from CloudCoin
        String cloudCoinStr ="";
        for( int i = 0; i < 25; i++  ){
            cloudCoinStr += this.ans[i];
        }//end for each an
        // cloudCoinStr +="Defeat tyrants and obey God0"; //27 AOID and comments
        cloudCoinStr +="204f42455920474f4420262044454645415420545952414e54532000"; //Hex for " OBEY GOD & DEFEAT TYRANTS "
        cloudCoinStr +="00";//LHC = 100%
        cloudCoinStr +="97E2";//0x97E2;//Expiration date Sep. 2018
        cloudCoinStr += "01";// cc.nn;//network number
        String hexSN = Integer.toHexString(this.sn);  
        String fullHexSN ="";
        switch (hexSN.length())//Add leading zeros to the hex number
        {
            case 1: fullHexSN = "00000" +hexSN; break;
            case 2:fullHexSN = "0000" +hexSN; break;
            case 3:fullHexSN = "000" +hexSN; break;
            case 4:fullHexSN = "00" +hexSN; break;
            case 5:fullHexSN = "0" +hexSN; break;
            case 6:fullHexSN = hexSN; break;
        }
        cloudCoinStr += fullHexSN;
        String Path = "";
        switch( getDenomination() ){
            case   1:  Path jpeg1 = Paths.get( rootFolder +"jpegs/jpeg1.jpg");//This is the location of the template for 1s
            try{ returnBytes = Files.readAllBytes(jpeg1); }catch(IOException e){
                System.out.println("General I/O exception: " + e.getMessage()); e.printStackTrace();
            }//end catch
            break;
            case   5: 
            Path jpeg5 = Paths.get(rootFolder +"jpegs/jpeg5.jpg");//This is the location of the template for 5s
            try{ returnBytes = Files.readAllBytes(jpeg5); }catch(IOException e){
                System.out.println("General I/O exception: " + e.getMessage());
                e.printStackTrace(); }//end catch
            break;
            case  25: 
            Path jpeg25 = Paths.get(rootFolder +"jpegs/jpeg25.jpg");
            try{ returnBytes = Files.readAllBytes(jpeg25); }catch(IOException e){
                System.out.println("General I/O exception: " + e.getMessage());
                e.printStackTrace();}//end catch
            break;
            case 100:
            Path jpeg100 = Paths.get(rootFolder +"jpegs/jpeg100.jpg");
            try{ returnBytes = Files.readAllBytes(jpeg100); }catch(IOException e){
                System.out.println("General I/O exception: " + e.getMessage()); e.printStackTrace();
            }//end catch
            break;
            case 250: 
            Path jpeg250 = Paths.get(rootFolder +"jpegs/jpeg250.jpg");
            try{ returnBytes = Files.readAllBytes(jpeg250); }catch(IOException e){ 
                System.out.println("General I/O exception: " + e.getMessage()); e.printStackTrace();
            }//end catch
            break;
        }//end switch
        /*OVERWRITE */
        byte[] ccArray = hexStringToByteArray( cloudCoinStr );
        int offset = 20;  // System.out.println("ccArray length " + ccArray.length);
        for( int j =0; j < ccArray.length; j++  ){
            returnBytes[offset + j ] = ccArray[j];
        }//end for each byte in the ccArray
        this.jpeg = returnBytes;
    }//end get jpeg

    /**
     * Method toHexadecimal
     *
     * @param digest An array of bytes that will change into a string of hex characters
     * @return A string version of the bytes in hex form. 
     */
    private String toHexadecimal(byte[] digest){
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    /**
     * Method saveCoin saves the coin as a json file with any file extension you like.
     * Usually you would use the following extensions: 
     * suspect (means the coin has not yet been authenticated and could be counterfeit)
     * bank (means the coin is good)
     * fractured (means the coin is fractured and should be fixed)
     * lost (Some of the coin is good but not enough to be useful)
     * counterfeit (Entire coin is bad)
     * stack (exported and ready to be given to another person.)
     *
     * @param extension The file extension you would like the JSON file to have. 
     * @return true if saved, false if failed to save.
     */
    public boolean saveCoin(String extension ){
        boolean goodSave = false;
        setJSON();
        File f = new File("./bank/" + this.fileName + extension );
        if(f.exists() && !f.isDirectory()) { 
            System.out.println("A coin with that SN already exists in the bank. Export it first.");
            return goodSave;
        }

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter( new FileWriter( "./bank/" + this.fileName + extension ));
            // System.out.println("\nSaving Coin file to Bank/" + this.fileName + extension );
            writer.write( this.json );
            goodSave = true;
        }catch ( IOException e){ } finally{    try{
                if ( writer != null)
                    writer.close( );
            }catch ( IOException e){}
        }
        return goodSave;

    }//end saveCoin

    public boolean deleteCoin( String path, String extension ){
        boolean deleted = false;
        //System.out.println("Deleteing Coin: "+path + this.fileName + extension);
        File f  = new File( path + this.fileName + extension);
        try {
            deleted = f.delete();
            if(deleted){
            }else{
                // System.out.println("Delete operation is failed.");
            }//end else
        }catch(Exception e){

            e.printStackTrace();

        }
        return deleted;
    }//end delete file

    public void calculateHP(){
        this.hp = 25;
        for( int i = 0; i< 25; i++){
            if( this.pastStatus[i].equalsIgnoreCase("fail")   )
            { 
                this.hp--;
            }
        }

    }//End calculate hp

    /**
     * Method writeJpeg writes a jpg image to the hard drive. 
     *
     * @param path The full path to the file except the file name.
     * @param tag A parameter that adds a tag to the filename
     * @return true if file is saved, false if file is not saved
     */
    public boolean writeJpeg( String path, String tag ){  
        boolean writeGood = true;
        String file = path + File.separator  + this.fileName + tag +".jpg";
        //System.out.println("Saving jpg: " + file);
        try{
            Files.write(Paths.get( file ), this.jpeg );
        }catch( IOException ex ){
          //  System.out.println( "Error Saving Jpeg: " + ex );
            writeGood = false;
        }//end try ioexception
        return writeGood;
    }//end jpeg to file

    /**
     * Creates some string comments for the user to understand the coin status. 
     *
     * @return  String Returns a description of the coin
     */
    public String gradeCoin(){
        int passed = 0;
        int failed = 0;
        int other = 0;
        String passedDesc ="";
        String failedDesc ="";
        String otherDesc ="";
        String internalAoid = ">";
        for( int i=0; i< 25; i++ ){

            if( pastStatus[i].equalsIgnoreCase("pass")  ){
                passed++; internalAoid +="p";//p means pass
            }else if( pastStatus[i].equalsIgnoreCase("fail")){
                failed++;  internalAoid +="f"; //f means fail
            }else{
                other++;  internalAoid +="u";//u means undetected
            }//end if pass, fail or unknown
        }//for each status
         internalAoid += "<";
         this.aoid = internalAoid;
        //Calculate passed
        if( passed == 25 ){
            passedDesc = "100% Passed!"; 
        }else if( passed > 17 ){
            passedDesc = "Super Majority";
        }else if( passed > 13){
            passedDesc = "Majority";
        }else if( passed == 0){
            passedDesc = "None";
        }else if(passed < 5) {
            passedDesc = "Super Minority";
        }else{
            passedDesc = "Minority";
        }
        //Calculate failed
        if( failed == 25 ){
            failedDesc = "100% Failed!"; 
        }else if( failed > 17 ){
            failedDesc = "Super Majority";
        }else if( failed > 13){
            failedDesc = "Majority";
        }else if( failed == 0){
            failedDesc = "None";
        }else if(failed < 5) {
            failedDesc = "Super Minority";
        }else{
            failedDesc = "Minority";
        }
        //Calcualte Other RAIDA Servers did not help. 
        switch( other ){
            case 0: otherDesc = "RAIDA 100% good"; break;
            case 1: 
            case 2: otherDesc = "Four or less RAIDA errors"; break;
            case 3: 
            case 4: otherDesc = "Four or less RAIDA errors"; break;
            case 5: 
            case 6: otherDesc = "Six or less RAIDA errors"; break;
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12:  otherDesc = "Between 7 and 12 RAIDA errors"; break;
            case 13:  
            case 14:  
            case 15:  
            case 16: 
            case 17:  
            case 18: 
            case 19:  
            case 20:  
            case 21:  
            case 22:  
            case 23: 
            case 24:  
            case 25: otherDesc = "RAIDA total failure"; break;
            default: otherDesc = "FAILED TO EVALUATE RAIDA HEALTH"; break;
        }//end RAIDA other errors and unknowns

        return  "\n " + passedDesc + " said Passed. " + "\n "+ failedDesc +" said Failed. \n RAIDA Status: "+ otherDesc;
    }//end grade coin

    /**
     * Method calcExpirationDate figures out when the coin will expire (after it has been authenticated)
     * This value is written to the coin's ed fields. 
     *
     */
    public void calcExpirationDate(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        year = year + YEARSTILEXPIRE;
        this.ed = month + "-" + year;
        this.edHex = Integer.toHexString(month);
        this.edHex += Integer.toHexString(year);       
    }//end calc exp date

    /**
     * Method generatePan creates secure random GUIDs that can be used as pans.
     *
     * @return String 32 hex character guid like 8d3eb063937164c789474f2a82c146d3 without hyphens
     */
    public String generatePan()
    {
        String AB = "0123456789ABCDEF";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder( 25 );
        for( int i=0 ; i<32 ; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    /**
     * Method ordinalIndexOf finds the index of the nth occurance of a character in a string.
     *
     * @param str The entire string
     * @param substr The part of the string to count the nth occrance
     * @param n The number of occurances we want to find. Starts at 1 not zero!
     * @return The return value
     */
    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    /**
     * Method hexStringToByteArray turns a String of hex characters into bytes
     *
     * @param s A String of hex characters
     * @return A byte array that represents the string of hex characters
     */
    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }//End of hexString to byte array

    /**
     * Method gradeStatus does two things: 
     * 1. Returns a verdic by the RAIDA about which RAIDA think the coin is pass or fail and which RAIDA did not repond.
     * 2. Lables the coin as:
     * suspect: Not enough RAIDA to make a determination
     * bank: Good coin.
     * fractured: Some raida said failed but the majority said passed
     * counterfeit: Fails
     * lost: Some said good but most said fail.  
     *
     * @return Returns three stings:
     * 1. What the passes say.
     * 2. What the fails say.
     * 3. The health of the RAIDA.
     */
    public String[] gradeStatus(){
        int passed = 0;
        int failed = 0;
        int other = 0;
        String passedDesc ="";
        String failedDesc ="";
        String otherDesc ="";
        for( int i=0; i< 25; i++ ){

            if( pastStatus[i].equalsIgnoreCase("pass")  ){
                passed++;
            }else if( pastStatus[i].equalsIgnoreCase("fail")){
                failed++;
            }else{
                other++;
            }//end if pass, fail or unknown
        }//for each status

        //Calculate passed
        if( passed == 25 ){
            passedDesc = "100% Passed!"; 
        }else if( passed > 17 ){
            passedDesc = "Super Majority";
        }else if( passed > 13){
            passedDesc = "Majority";
        }else if( passed == 0){
            passedDesc = "None";
        }else if(passed < 5) {
            passedDesc = "Super Minority";
        }else{
            passedDesc = "Minority";
        }
        //Calculate failed
        if( failed == 25 ){
            failedDesc = "100% Failed!"; 
        }else if( failed > 17 ){
            failedDesc = "Super Majority";
        }else if( failed > 13){
            failedDesc = "Majority";
        }else if( failed == 0){
            failedDesc = "None";
        }else if(failed < 5) {
            failedDesc = "Super Minority";
        }else{
            failedDesc = "Minority";
        }
        //Calcualte Other RAIDA Servers did not help. 
        switch( other ){
            case 0: otherDesc = "100% of RAIDA responded"; break;
            case 1: 
            case 2: otherDesc = "Four or less RAIDA errors"; break;
            case 3: 
            case 4: otherDesc = "Four or less RAIDA errors"; break;
            case 5: 
            case 6: otherDesc = "Six or less RAIDA errors"; break;
            case 7: 
            case 8: 
            case 9: 
            case 10: 
            case 11: 
            case 12:  otherDesc = "Between 7 and 12 RAIDA errors"; break;
            case 13:  
            case 14:  
            case 15:  
            case 16: 
            case 17:  
            case 18: 
            case 19:  
            case 20:  
            case 21:  
            case 22:  
            case 23: 
            case 24:  
            case 25: otherDesc = "RAIDA total failure"; break;
            default: otherDesc = "FAILED TO EVALUATE RAIDA HEALTH"; break;
        }//end RAIDA other errors and unknowns
        //Coin will go to bank, counterfeit, lost or fracked

        if(other > 12){//not enough RAIDA to have quorum
            extension = "suspect";
        }else if( passed == 0 && failed > 1 ){//Nothing passed, something failed Counterfeit
            extension = "counterfeit";
        }else if( failed == 0){ //no fails so assumes all good. 
            extension = "bank";
        }else if(passed > failed ){//We have some fails 
            extension = "fracked";
        }else{
            extension = "lost";
        }

        this.gradeStatus[0] = passedDesc;
        this.gradeStatus[1] = failedDesc;
        this.gradeStatus[2] = otherDesc;
        return  gradeStatus;
    }//end gradeStatus

    
    /**
     * Method setAnsToPans
     * This uses the Ans and the pans so that they do not change
     * This is used for fixing fractured RAIDA were the RAIDA is assigned the AN
     *
     */
    public void setAnsToPans(){
     for(int i =0; i<25; i++){
        pans[i] = ans[i];
        }//end for 25 ans
    
    }//end setAnsToPans
    
    
    /**
     * Method fileToString turns a file into String
     *
     * @param pathname A file
     * @return The file contents in a String
     */
    public String fileToString(String pathname) throws IOException {
        File file = new File(pathname);
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }

        } finally {
            scanner.close();
        }
        return fileContents.toString();
    }

    /**
     * Method setAnsToPansIfPassed looks at the past Status to see if the detections passed. If so the AN becomes the PAN.
     *
     */
    public void setAnsToPansIfPassed(){
        //now set all ans that passed to the new pans
        for(int i =0; i< 25; i++){
            if( pastStatus[i].equalsIgnoreCase("pass")  ){
                ans[i] = pans[i];
            }else{
                //Just keep the ans and do not change. Hopefully they are not fracked. 
            }
        }//for each guid in coin
    }//end set ans to pans if passed

    /**
     * Method consoleReport shows what the detailed results of a detection attempt. 
     *
     */
    public void consoleReport(){
        //Used only for console apps
        // System.out.println("Finished detecting coin index " + j);
        //PRINT OUT ALL COIN'S RAIDA STATUS AND SET AN TO NEW PAN
        System.out.println("");
        System.out.println("CloudCoin SN #"+sn +", Denomination: "+ getDenomination() );
        int RAIDAHealth = 25;
        hp=25;
        for(int i = 0; i < 25;i++){
            if ( i % 5 == 0 ) { System.out.println("");}//Give every five statuses a line break
            pastStatus[i]= pastStatus[i];
            if( pastStatus[i] == "pass")
            {    
                ans[i] = pans[i];//RAIDA health stays up
            }
            else if( pastStatus[i] == "fail")
            { 
                hp--; 
            }
            else{
                RAIDAHealth--;
            }//check if failed
            String fi = String.format("%02d", i);//Pad numbers with two digits
            System.out.print("RAIDA"+ fi +": "+ pastStatus[i].substring(0,4) + " | " );
        }//End for each cloud coin GUID statu
    }//end consoleReport
}//End of class CloudCoin
