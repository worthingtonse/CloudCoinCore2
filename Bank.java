import java.security.SecureRandom;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.*;
import java.util.Random;  
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.*;
import java.io.File;

/**
 * The Bank tracks the entire contents of the Bank folder used to manage CloudCoins
 * 
 * 
 * @author Sean H. Worthington
 * @version 1/2/2016
 */
public class Bank
{
    // instance variables - replace the example below with your own
    // public KeyboardReader reader = new KeyboardReader();
    // public CloudCoin[] chestCoins; 
    public CloudCoin[] frackedCoins;
    //  public CloudCoin[] importedCoins;
    public CloudCoin[] bankedCoins;
    public CloudCoin[] counterfeitCoins;
    public CloudCoin[] exportCoins;
    int totalValueToBank = 0;
    int totalValueToCounterfeit = 0;
    int totalValueToFractured = 0;
    int totalValueLost = 0;
    String rootFolder;
    public RAIDA raida;

    /**
     * CONSTRUCTOR
     */
    public Bank(String rootFolder)
    {
        // initialise instance variables
        this.rootFolder = rootFolder;
        raida = new RAIDA();
    }

    /**
     * METHODS
     */
    public int countCoins( CloudCoin[] coins, int denomination ){
        int totalCount =  0;
        for(int i = 0 ; i < coins.length; i++){
            if( coins[i].getDenomination() == denomination ){
                totalCount++;
            }//end if coin is the denomination
        }//end for each coin
        return totalCount;
    }//end count coins

    public boolean ifFileExists( String filePathString ){
        File f = new File(filePathString);
        if(f.exists() && !f.isDirectory()) { 
            return true;
        }
        return false;
    }//end if file Exists

    public boolean importAll(String loadFileName){
        int totalValueToBank = 0;
        int totalValueToCounterfeit = 0;
        int totalValueToFractured = 0;
        int totalValueLost = 0;
        String extension = "";
        int indx = loadFileName.lastIndexOf('.');
        if (indx > 0) {
            extension = loadFileName.substring(indx+1);
        }
        extension = extension.toLowerCase();
        boolean jpg = false;
        if ( extension.equals("jpeg") || extension.equals("jpg")){ jpg =true;   }

        //     case 1: 
        if( jpg ){
            if( ! loadJpeg( loadFileName )){ 
                System.out.println("Failed to load JPEG file");
                return false;
            }else{
                if( ! loadIncome( loadFileName, "income")){ 
                    System.out.println("Failed to load CloudCoin file");
                    return false;
                }
            }//end if jpg
        }
        //change imported file to have a .imported extention
        renameFileExtension(loadFileName, "imported" );

        //LOAD THE .income COINS ONE AT A TIME AND TEST THEM
        String[] incomeFileNames  = selectAllFileNamesInFolder( rootFolder, "suspect" );
        //importedCoins = loadCoins( rootFolder, "income" );//Load Coins from hard drive into RAM

        System.out.println("Loaded " + incomeFileNames.length + " income files");
        detectAuthenticity( );//Checks all Coins in the folder. 

        return true;
    }//end import

    /***
     * GIven a directory and an extension, loads all CloudCoins of that extension
     * 
     */
    public CloudCoin[] loadCoins(String directoryPath, String extension){
        File f = null;
        String[] paths;
        CloudCoin[] loadedCoins =null;
        try{ 
            f = new File(directoryPath); // System.out.println("Checking " + directoryPath + " for " + extension + " files.");
            FilenameFilter fileNameFilter = new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        if(name.lastIndexOf('.')>0){
                            int lastIndex = name.lastIndexOf('.')+1; // get last index for '.' char
                            String str = name.substring(lastIndex);  // get extension
                            if(str.equals( extension )) {// match path name extension
                                return true;
                            }
                        }
                        return false;
                    }
                };
            paths = f.list(fileNameFilter);// returns pathnames for files and directory
            loadedCoins = new CloudCoin[ paths.length ];// for each pathname in pathname array
            for(int i = 0; i < paths.length; i++)            {
                loadedCoins[i] = new CloudCoin( directoryPath + paths[i] );  //  System.out.println("Loading " + directoryPath + paths[i]);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return loadedCoins;
    }//end load fracked  

    /*
    public static String[] toStringArray(JSONArray array) {
    if(array==null)
    return null;

    String[] arr=new String[array.length()];
    for(int i=0; i<arr.length; i++) {
    arr[i]=array.optString(i);
    }
    return arr;
    }//end toStringArray
     */
    /***
     * Given a file name, changes the file extension without changing the file
     * @parameter source The name of the target file
     * @parameter newExtension The new extension to be give the file
     * @return boolean True if the extension is changes otherwise false. 
     */
    public boolean renameFileExtension(String source, String newExtension){
        String target;
        String currentExtension = getFileExtension(source);

        if (currentExtension.equals("")){
            target = source + "." + newExtension;
        }
        else {
            target = source.replaceFirst(Pattern.quote("." + currentExtension) + "$", Matcher.quoteReplacement("." + newExtension));
        }
        return new File(source).renameTo(new File(target));
    }

    /***
     * Given directory path return an array of strings of all the files in the directory.
     * @parameter directoryPath The location of the directory to be scanned
     * @return filenames The names of all the files in the directory
     */
    public String[] selectAllFileNamesInFolder(String directoryPath, String extention) {
        File dir = new File(directoryPath);
        String candidateFileExt = "";
        Collection<String> files  =new ArrayList<String>();
        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            for(File file : listFiles){
                if(file.isFile()) {//Only add files with the matching file extension
                    candidateFileExt = getFileExtension( file.getName() );
                    if ( candidateFileExt.equalsIgnoreCase(extention) ){
                        files.add(file.getName());
                    }//end if it is correct file ext.
                }
            }
        }
        return files.toArray(new String[]{});
    }//End select all file names in a folder

    public void detectAuthenticity(){

        //change imported file to have a .imported extention
        //renameFileExtension(loadFileName, "imported" );

        //LOAD THE .suspect COINS ONE AT A TIME AND TEST THEM
        String[] incomeFileNames  = selectAllFileNamesInFolder( rootFolder, "suspect" );
        //importedCoins = loadCoins( rootFolder, "income" );//Load Coins from hard drive into RAM

        System.out.println("Loaded " + incomeFileNames.length + " income files");
        int RAIDAHealth = 25;
        //Now the coin has been fixed. See if there were some improvements. 
        CloudCoin newCC;
        for(int j = 0; j < incomeFileNames.length; j++){
            newCC = new CloudCoin( rootFolder + incomeFileNames[j]);
            System.out.println("Detecting SN #"+ newCC.sn +", Denomination: "+ newCC.getDenomination() );
            CloudCoin detectedCC =  raida.detectCoin( newCC );//Checks all 25 GUIDs in the Coin and sets the status. 

            //SORT OUT EACH COIN INTO CATAGORIES
            System.out.println("\nRAIDA Health: " +RAIDAHealth + "/25");
            switch( detectedCC.extension ){
                case "bank": totalValueToBank++; break;
                case "fractured": totalValueToFractured++; break;
                case "lost": totalValueLost++; break;
                case "counterfeit": totalValueToCounterfeit++; break;
            }//end switch on the place the coin will go 
            //NOW FIX FRACTURED IF IF NEEDED
            //  loadCloudCoins("./Bank/","fractured");
        }//end for each coin to import
        //REPORT ON DETECTION OUTCOME
        System.out.println("Results of Import:");
        System.out.println("Good and Moved in Bank: "+ totalValueToBank);
        System.out.println("Counterfeit and Moved to trash: "+totalValueToCounterfeit);
        System.out.println("Fracked and Moved to Fracked: "+ totalValueToFractured);
        System.out.println("Lost and Moved to Lost: "+ totalValueLost);
    }//end detectAuthenticity

    public boolean exportJson( int m1, int m5, int m25, int m100, int m250, String tag){
        boolean jsonExported = true;
        int totalSaved = m1 + ( m5 * 5 ) + ( m25 * 25 ) + (m100 * 100 ) + ( m250  * 250 );//Track the total coins
        int coinCount = m1 + m5 + m25 + m100 + m250;
        /* CONSRUCT JSON STRING FOR SAVING */
        String[] coinsToDelete =  new String[coinCount];
        String[] bankedFileNames = selectAllFileNames("bank");//list all file names with bank extension

        //Check to see the denomination by looking at the file start
        int c = 0;//c= counter
        String json = "{ \"cloudcoin\": [";
        String d ="";   
        //Put all the JSON together and add header and footer
        for(int i =0; i< bankedFileNames.length; i++ ){
            d = bankedFileNames[i].split("\\.")[0];

            if( d.equals("1") && m1 > 0 ){ 
                if( c !=0 ){ json += ",\n";} 
                coinsToDelete[c]= bankedFileNames[i]; c++; 
                json += getOneJSON( bankedFileNames[i]); m1--;   //Get the clean JSON of the coin
            }//end if coin is a 1
            if( d.equals("5") && m5 > 0 ){  
                if( c !=0 ){ json += ",\n";}
                coinsToDelete[c]=bankedFileNames[i]; c++; 
                json += getOneJSON( bankedFileNames[i]); m5--;   
            }//end if coin is a 5
            if( d.equals("25") && m25 > 0 ){ 
                if( c !=0 ){ json += ",\n";} 
                coinsToDelete[c]=bankedFileNames[i]; c++; 
                json += getOneJSON( bankedFileNames[i]); m25--  ; 
            }//end if coin is a 25
            if( d.equals("100") && m100 > 0 ){  
                if( c !=0 ){ json += ",\n";}
                coinsToDelete[c]=bankedFileNames[i]; c++; 
                json += getOneJSON( bankedFileNames[i]); m100--;   
            }//end if coin is a 100
            if( d.equals("250") && m250 > 0 ){  
                if( c !=0 ){ json += ",\n";}
                coinsToDelete[c]=bankedFileNames[i]; c++;  
                json += getOneJSON( bankedFileNames[i]); m250--;   
            }//end if coin is a 250
            if( m1 ==0 && m5 ==0 && m25 == 0 && m100 == 0 && m250 == 0 ){break;}//Break if all the coins have been called for.     
        }//for each 1 note
        json += "]}";

        /* FIGURE OUT NEW STACK NAME AND SAVE TO FILE */
        String filename = rootFolder + File.separator + totalSaved +".CloudCoins." + tag + ".stack";
        if(  ifFileExists(filename)){//tack on a random number if a file already exists with the same tag
            //Add random 
            Random rnd = new Random();
            int tagrand = rnd.nextInt(999);
            filename = rootFolder + File.separator + totalSaved +".CloudCoins." + tag + tagrand + ".stack";
        }//end if file exists

        /* DELETE EXPORTED CC FROM BANK */ 
        for(int cc = 0; cc < coinsToDelete.length; cc++){
            // System.out.println("Deleting "+ path + coinsToDelete[cc].fileName + "bank");
            deleteCoin( coinsToDelete[cc] );
        }//end for

        return jsonExported;
    }//end export

    public void exportJpeg(int m1, int m5, int m25, int m100, int m250, String tag ){
        boolean jsonExported = true;
        int totalSaved = m1 + ( m5 * 5 ) + ( m25 * 25 ) + (m100 * 100 ) + ( m250  * 250 );//Track the total coins
        int coinCount = m1 + m5 + m25 + m100 + m250;
        /* CONSRUCT JSON STRING FOR SAVING */
        String[] coinsToDelete =  new String[coinCount];
        String[] bankedFileNames = selectAllFileNames("bank");//list all file names with bank extensio
        String r = rootFolder;
        String b = "bank";
        String t = tag;
        String p = rootFolder;
        /* SET JPEG, WRITE JPEG and DELETE CLOUDCOINS*/
        int c = 0;//c= counter
        String d ="";   
        CloudCoin jpgCoin = null;
        //Put all the JSON together and add header and footer
        for(int i =0; i< bankedFileNames.length; i++ ){
            d = bankedFileNames[i].split("\\.")[0];//Get's denominiation
            if( d.equals("1") && m1 > 0 ){ 
                jpgCoin = new CloudCoin( bankedFileNames[i] );
                jpgCoin.setJpeg(r); 
                if( jpgCoin.writeJpeg(p,t)){
                    jpgCoin.deleteCoin(r,b); }
                m1--;  
            }//end if coin is a 1
            if( d.equals("5") && m5 > 0 ){ 
                jpgCoin = new CloudCoin( bankedFileNames[i] );
                jpgCoin.setJpeg(r); 
                if( jpgCoin.writeJpeg(p,t)){ 
                    jpgCoin.deleteCoin(r,b);
                }  
                m5--;   
            }//end if coin is a 5
            if( d.equals("25") && m25 > 0 ){ 
                jpgCoin = new CloudCoin( bankedFileNames[i] );
                jpgCoin.setJpeg(r); if( jpgCoin.writeJpeg(p,t)){ jpgCoin.deleteCoin(r,b);}  m25--  ; }//end if coin is a 25
            if( d.equals("100") && m100 > 0 ){ 
                jpgCoin = new CloudCoin( bankedFileNames[i] );
                jpgCoin.setJpeg(r); if( jpgCoin.writeJpeg(p,t)){ jpgCoin.deleteCoin(r,b);}  m100--;   }//end if coin is a 100
            if( d.equals("250") && m250 > 0 ){ 
                jpgCoin = new CloudCoin( bankedFileNames[i] );
                jpgCoin.setJpeg(r); if( jpgCoin.writeJpeg(p,t)){ jpgCoin.deleteCoin(r,b);}  m250--;   }//end if coin is a 250
            if( m1 ==0 && m5 ==0 && m25 == 0 && m100 == 0 && m250 == 0 ){break;}//Break if all the coins have been called for.     
        }//for each 1 note

    }//end export

    public boolean exportAllJson( String tag, String extension ){
        boolean jsonExported = true;
        int totalSaved = 0;
        String[] allFileNames = selectAllFileNames( extension );//list all file names with bank extension

        String json = "{ \"cloudcoin\": [";
        for(int i =0; i< allFileNames.length; i++ ){
            if( i !=0 ){ json += ",\n";} 
            totalSaved += exportCoins[i].getDenomination();
            json += exportCoins[i].setJSON();   
        }//for each 1 note
        json += "]}";

        /* FIGURE OUT NEW STACK NAME AND SAVE TO FILE */
        String filename = rootFolder + File.separator + totalSaved +".CloudCoins." + tag + "."+  extension ;
        if(  ifFileExists(filename)){//tack on a random number if a file already exists with the same tag
            //Add random 
            Random rnd = new Random();
            int tagrand = rnd.nextInt(999);
            filename = rootFolder + File.separator + totalSaved +".CloudCoins." + tag + tagrand + "." + extension;
        }//end if file exists
        System.out.println("Writing to : " + filename);

        if ( stringToFile( json, filename ) ){
            /* DELETE EXPORTED CC FROM BANK */ 
            for(int cc = 0; cc < exportCoins.length; cc++){
                // System.out.println("Deleting "+ path + coinsToDelete[cc].fileName + "bank");
                exportCoins[cc].deleteCoin( rootFolder, extension );
            }//end for
        }else{//Write Failed
            jsonExported = false;
        }//end if write was good
        return jsonExported;
    }//end export

    public String getOneJSON( String fileName){
        try{
            String jsonData = loadFileToString( fileName );
            //extract single coin. 
            int indexOfFirstSquareBracket = ordinalIndexOf( jsonData, "[", 0);
            int indexOfLastSquareBracket = ordinalIndexOf( jsonData, "]", 2);
            return jsonData.substring( indexOfFirstSquareBracket, indexOfLastSquareBracket );

        }catch( FileNotFoundException ex){
            return "";
        }

    }//end get one json

    public String loadFileToString( String jsonfile) throws FileNotFoundException {
        String jsonData = "";
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader( jsonfile ));
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
        }
        return jsonData;
    }//en d json test

    /***
     * Given a string and a file name, write the string to the harddrive.
     * @parameter text The string to go into the file
     * @paramerter filename The name to be given to the file.
     */
    public boolean stringToFile( String text, String filename) {
        boolean writeGood =  false;
        try(  PrintWriter out = new PrintWriter( filename )  ){
            out.println( text );
            writeGood = true;
        }catch( FileNotFoundException ex){
            System.out.println(ex);
        }
        return writeGood;
    }//end string to file 

    /***
     * Given directory path return an array of strings of all the files in the directory.
     * @parameter directoryPath The location of the directory to be scanned
     * @return filenames The names of all the files in the directory
     */
    public String[] selectAllFileNames( String ext) {
        File dir = new File("./Bank/");
        String candidateFileExt = "";
        Collection<String> files  =new ArrayList<String>();
        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            for(File file : listFiles){
                if(file.isFile()) {//Only add files with the matching file extension
                    candidateFileExt = getFileExtension( file.getName() );
                    if ( candidateFileExt.equalsIgnoreCase( ext) ){
                        files.add(file.getName());
                    }//end if it is correct file ext.
                }
            }
        }
        return files.toArray(new String[]{});
    }//End select all file names in a folder

    public boolean deleteCoin( String path ){
        boolean deleted = false;
        //System.out.println("Deleteing Coin: "+path + this.fileName + extension);
        File f  = new File( path );
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

    /***
     * Given string that repressents a file name, return the file extention
     * @parameter f The filename
     * @return ext The file extention 
     */
    public String getFileExtension(String f) {
        String ext = "";
        int i = f.lastIndexOf('.');
        if (i > 0 &&  i < f.length() - 1) {
            ext = f.substring(i + 1);
        }
        return ext;
    }

    public void showCoins(){
        //This is for consol apps.
        //Get JSON from RAIDA Directory
        //for(int i = 0; i < 25;i++){System.out.println("RAIDA"+ i +": "+ raidaArray[i].status +", ms:" + raidaArray[i].ms );}//end for each raida status
        int total_1 =  0;
        int total_5 =  0;
        int total_25 = 0;
        int total_100 =  0;
        int total_250 =  0;

        //System.out.println("'\nRoot folder is " + rootFolder);
        bankedCoins = loadCoins( rootFolder ,"bank");
        total_1 =  countCoins( bankedCoins, 1 );
        total_5 =  countCoins( bankedCoins, 5 );
        total_25 =  countCoins( bankedCoins, 25 );
        total_100 =  countCoins( bankedCoins, 100 );
        total_250 =  countCoins( bankedCoins, 250 );

        System.out.println("Your Bank Inventory:");
        System.out.print("  1s: "+ total_1  +" || ");
        System.out.print("  5s: "+ total_5  +" ||");
        System.out.print(" 25s: "+ total_25 +" ||" );
        System.out.print("100s: "+ total_100+" ||");
        System.out.println("250s: "+ total_250 );

        frackedCoins = loadCoins( rootFolder ,"fracked");
        if( frackedCoins.length > 0 ){
            total_1 =  countCoins( frackedCoins, 1  );
            total_5 =  countCoins( frackedCoins, 5  );
            total_25 =  countCoins( frackedCoins, 25  );
            total_100 =  countCoins( frackedCoins, 100  );
            total_250 =  countCoins( frackedCoins, 250  );

            System.out.println("Your fracked Inventory:");
            System.out.print("  1s: "+ total_1  +" || ");
            System.out.print("  5s: "+ total_5  +" ||");
            System.out.print(" 25s: "+ total_25 +" ||" );
            System.out.print("100s: "+ total_100+" ||");
            System.out.println("250s: "+ total_250 );

        }else{
            System.out.println("No fractured coins. " );
        }
        //if has fracked coins
        //get all names in the folder
        //state how many 1, 5, 25, 100 and 250
        counterfeitCoins = loadCoins( rootFolder ,"lost");
        if( counterfeitCoins.length > 0 ){
            total_1 =  countCoins( counterfeitCoins, 1 );
            total_5 =  countCoins( counterfeitCoins, 5 );
            total_25 =  countCoins( counterfeitCoins, 25 );
            total_100 =  countCoins( counterfeitCoins, 100 );
            total_250 =  countCoins( counterfeitCoins, 250 );

            System.out.println("Your lost Inventory:");
            System.out.print("  1s: "+ total_1  +" || ");
            System.out.print("  5s: "+ total_5  +" ||");
            System.out.print(" 25s: "+ total_25 +" ||" );
            System.out.print("100s: "+ total_100+" ||");
            System.out.println("250s: "+ total_250 );

        }else{
            System.out.println("No lost coins. " );
        }
        counterfeitCoins = loadCoins( rootFolder ,"counterfeit");
        if( counterfeitCoins.length > 0 ){
            total_1 =  countCoins( counterfeitCoins, 1 );
            total_5 =  countCoins( counterfeitCoins, 5 );
            total_25 =  countCoins( counterfeitCoins, 25 );
            total_100 =  countCoins( counterfeitCoins, 100 );
            total_250 =  countCoins( counterfeitCoins, 250 );

            System.out.println("Your fracked Inventory:");
            System.out.print("  1s: "+ total_1  +" || ");
            System.out.print("  5s: "+ total_5  +" ||");
            System.out.print(" 25s: "+ total_25 +" ||" );
            System.out.print("100s: "+ total_100+" ||");
            System.out.println("250s: "+ total_250 );

        }else{
            System.out.println("No counterfeit coins. " );
        }
        //write file name to bank

    }//end show coins

    private int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    /**
     * This method is used to load .jpg and .jpeg files.
     * @param  loadFilePath: The path to the Bank file and the name of the file. 
     * @param  Security: How the ANs are going to be changed during import (Random, Keep, password).
     */
    public boolean loadJpeg( String loadFilePath) {  
        boolean isSuccessful =false;
        System.out.println("Trying to load: " + loadFilePath );
        String incomeJson = ""; 
        // String new fileName = coinCount +".CloudCoin.New"+ rand.nextInt(5000) + "";
        try{
            incomeJson = loadJSON( loadFilePath );
            isSuccessful = true;
        }catch( IOException ex ){
            System.out.println( "JPEG Corupt Error: " + ex );
        }
        // String ans[] = new String[25];
        CloudCoin tempCoin = new CloudCoin( loadFilePath );
        tempCoin.saveCoin("income");
        return isSuccessful;
    }//end load income

    /**
     * This method is used to load .chest and .stack files that are in JSON notation.
     * 
     * @param  loadFilePath: The path to the Bank file and the name of the file. 
     * @param  Security: How the ANs are going to be changed during import (Random, Keep, password).
     */
    public boolean loadIncome( String loadFilePath, String extension) {  
        boolean isSuccessful = false;
        System.out.println("Trying to load: " + loadFilePath );
        String incomeJson = ""; 
        // String new fileName = coinCount +".CloudCoin.New"+ rand.nextInt(5000) + "";
        try{
            incomeJson = loadJSON( loadFilePath );
        }catch( IOException ex ){
            System.out.println( "error " + ex );
        }
        // String ans[] = new String[25];
        JSONArray incomeJsonArray;

        try{
            JSONObject o = new JSONObject( incomeJson );
            incomeJsonArray = o.getJSONArray("cloudcoin");
            //this.newCoins = new CloudCoin[incomeJsonArray.length()];
            CloudCoin tempCoin = null;
            for (int i = 0; i < incomeJsonArray.length(); i++) {  // **line 2**
                JSONObject childJSONObject = incomeJsonArray.getJSONObject(i);
                int nn     = childJSONObject.getInt("nn");
                int sn     = childJSONObject.getInt("sn");
                JSONArray an = childJSONObject.getJSONArray("an");
                String ed     = childJSONObject.getString("ed");
                String aoid = childJSONObject.getString("aoid");

                //this.newCoins[i] = new CloudCoin( nn, sn, toStringArray(an), ed, aoid, security );//This could cause memory issues.   
                tempCoin = new CloudCoin( nn, sn, toStringArray(an), ed, aoid, "suspect" );//security should be change or keep for pans.
                tempCoin.saveCoin(extension);//Could be income or bank
                //System.out.println("bank: New coin "+ i +" created " + this.newCoins[i].sn + ", ans[0] =" + this.newCoins[i].ans[0]);
                //System.out.println("bank: [0] coin 0 created " + this.newCoins[0].sn + ", ans[0] =" + this.newCoins[0].ans[0]);
                System.out.println( "Loading Coin: nn " + nn + ", sn " + sn + ", ed " + ed + ", aoid " + aoid );
            }//end for each coin
            isSuccessful = true;
        }catch( JSONException ex)
        {
            System.out.println("Stack File Corrupt. See CloudCoin file api and edit your stack file: " + ex);

        }//try 
        return isSuccessful;
    }//end load income

    public String loadJSON( String jsonfile) throws FileNotFoundException {
        String jsonData = "";
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader( jsonfile ));
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
        }
        return jsonData;
    }//en d json test

    public static String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;

        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }//end toStringArray

    public void fixFracked(){
        //Load coins from file in to banks fracked array
        totalValueToBank = 0;
        totalValueToFractured = 0;
        totalValueLost = 0;
        totalValueToCounterfeit=0;
        frackedCoins = loadCoins( rootFolder,"fracked");

        System.out.println("Loaded " + frackedCoins.length + " fracked files");
        //   System.out.println("Do you want to (1) import in mass or (2)inspect each coin that is imported?");
        //  int inspectionMode2 = reader.readInt(1,2);
        /* LOOP THROUGH EVERY COIN THAT IS FRACKED */
        for(int k = 0; k < frackedCoins.length; k++){

            //frackedCoins[k].reportStatus();
            System.out.println("Unfracking SN #"+frackedCoins[k].sn +", Denomination: "+ frackedCoins[k].getDenomination() );
            fixCoin( frackedCoins[k] );//Checks all 25 GUIDs in the Coin and sets the status.

            //Check CloudCoin's hp. 
            int RAIDAHealth2 = 25;
            //Now the coin has been fixed. See if there were some improvements. 
            frackedCoins[k].hp = 25;
            for(int i = 0; i < 25;i++){
                if ( i % 5 == 0 ) { System.out.println("");}//Give every five statuses a line break
                if( frackedCoins[k].pastStatus[i] == "pass")
                {    
                    //Keep ans because it is now good
                }
                else if( frackedCoins[k].pastStatus[i] == "fail")
                { 
                    frackedCoins[k].hp--; 
                }
                else
                {
                    RAIDAHealth2--;
                }//check if failed
                String fi = String.format("%02d", i);//Pad numbers with two digits
                System.out.print("RAIDA"+ fi +": "+ frackedCoins[k].pastStatus[i].substring(0,4) + " | " );
            }//end switch on the place the coin will go
            System.out.println("\nRAIDA Health " + RAIDAHealth2 + "/25");
            switch( sortCoin( frackedCoins[k], RAIDAHealth2)){
                case "bank": totalValueToBank++; break;
                case "fractured": totalValueToFractured++; break;
                case "lost": totalValueLost++; break;
                case "counterfeit": totalValueToCounterfeit++; break;
            }//end for each guid

        }//end for each fracked coin
        //REPORT ON DETECTION OUTCOME
        System.out.println("Results of Fix Fractured:");
        System.out.println("Good and Moved in Bank: "+ totalValueToBank);
        System.out.println("Counterfeit and Moved to trash: "+totalValueToCounterfeit);
        System.out.println("Still Fracked and Moved to Fracked: "+ totalValueToFractured);
        System.out.println("Lost and Moved to Lost: "+ totalValueLost);

    }//end fix fracked

    public void fixCoin( CloudCoin brokeCoin ){
        //Make an array of broken coins or go throug each if broken fix
        int mode = 1;
        boolean hasTickets = false;
        String fix_result = "";

        //brokeCoin.reportStatus();

        for (int guid_id = 0; guid_id < 25; guid_id++  ){//Check every Guid in the cloudcoin to see if it is fractured
            //  System.out.println("Inspecting RAIDA guid " + guid_id );

            FixitHelper fixer;
            if( brokeCoin.pastStatus[guid_id].equalsIgnoreCase("fail")){//This guid has failed, get tickets
                System.out.println("RAIDA" +guid_id +" failed." );
                fixer = new FixitHelper( guid_id, brokeCoin );
                //fixer.reportCoinStatus();
                mode = 1;
                hasTickets = false;
                while( ! fixer.finnished ){

                    if( fixer.currentTriadReady ){
                        hasTickets = raida.getTickets( fixer.currentTriad, fixer.currentAns, brokeCoin.nn, brokeCoin.sn, brokeCoin.getDenomination() ); 
                        if( hasTickets ){
                            fix_result = raida.raidaArray[guid_id].fix( fixer.currentTriad, raida.raidaArray[fixer.currentTriad[0]].lastTicket, raida.raidaArray[fixer.currentTriad[1]].lastTicket, raida.raidaArray[fixer.currentTriad[2]].lastTicket, brokeCoin.ans[guid_id]);
                            if( fix_result.equalsIgnoreCase("success")){
                                //Save pan to an, stop looping, report sucess. 
                                brokeCoin.pastStatus[guid_id] = "pass";
                                // brokeCoin.ans[guid_id] = brokeCoin.pans[guid_id];
                                //The CloudCoin an does not change. The RAIDA's an changes. No need to save the pan. 
                                fixer.finnished = true;
                                System.out.println("GUID fixed for guid " + guid_id );

                            }else{
                                System.out.println("Fix it command failed for guid  " + guid_id );
                                mode++;//beed to try another corner
                                fixer.setCornerToCheck( mode );
                            }//end if success fixing

                        }else{//No tickets, go to next triad

                            System.out.println("Get ticket commands failed for guid " + guid_id );
                            mode++;
                            fixer.setCornerToCheck( mode );
                        }//all the tickets are good. 
                    }else{//Triad will not work change it 
                        System.out.println("Trusted triad "+ mode + " is not able to help: " + brokeCoin.pastStatus[fixer.currentTriad[0]] +", "+brokeCoin.pastStatus[fixer.currentTriad[1]]+", "+brokeCoin.pastStatus[fixer.currentTriad[2]]);
                        mode++;
                        fixer.setCornerToCheck( mode );
                    }//end if traid is ready
                }//end while still trying to fix
                //Finnished fixing 
            }//end if guid is broken and needs to be fixed
        }//end for each guid
    }//end fix coin

    public static String getHtml(String url_in) throws MalformedURLException, IOException {
        int c;
        URL cloudCoinGlobal = new URL(url_in);
        URLConnection conn = cloudCoinGlobal.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        InputStream input = conn.getInputStream();

        StringBuilder sb = new StringBuilder();

        while((( c = input.read()) != -1))
        {
            sb.append((char)c); 
        }//end while   
        input.close();
        return sb.toString();
    }//end get url
public String sortCoin( CloudCoin coin, int RAIDAHealth ){
        String returnString = "";
        coin.calculateHP();
        String grade = coin.gradeCoin();
        System.out.println("\nResults:" + grade );
        System.out.println("Health Points are: " + coin.hp +"/25");
        coin.calcExpirationDate();
        System.out.println("Expiration Data is " + coin.ed);
   
            //SORT OUT EACH COIN INTO CATAGORIES
            System.out.println("HP is: " + coin.hp );
            if( coin.hp > 24 && RAIDAHealth > 11 ){//No Problems Move to Bank
                coin.saveCoin("bank");
                coin.deleteCoin(rootFolder, "fracked");//The coin is being brought in from fracked or income
                coin.deleteCoin(rootFolder, "income");
                returnString ="bank";
            }
            else if( coin.hp > 9 )
            {//Can be fixed
                coin.saveCoin("fracked");
                coin.deleteCoin(rootFolder, "fracked");
                coin.deleteCoin(rootFolder, "income");
                returnString ="fracked";
                //greater than 20, send to bank
            } else if( coin.hp > 1) {//Lost coin
                coin.saveCoin("lost");
                coin.deleteCoin(rootFolder,"fracked");
                coin.deleteCoin(rootFolder,"income");//Could be comming from fracked or income
                returnString ="lost";
            }else{ //Counterfeit - send to counterfeit
                coin.saveCoin("counterfeit");
                coin.deleteCoin(rootFolder,"fracked");
                coin.deleteCoin(rootFolder,"income");//Could be comming from fracked or income
                returnString ="counterfeit";
            }
            
        return returnString;
    }//end grade coin
    /***
     * Given a byte array and a file name, write the bytes to the harddrive.
     * @parameter text The string to go into the file
     * @paramerter filename The name to be given to the file.
     */
    /*  public static void bytesToFile( String text, String filename){

    try(  PrintWriter out = new PrintWriter( filename )  ){
    out.println( text );
    }catch( FileNotFoundException ex){
    System.out.println(ex);
    }
    }//end string to file 

     */
}
