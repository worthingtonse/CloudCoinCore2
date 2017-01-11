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
 * The Bank tracks the entire contents of the Bank folder used to manage multiple CloudCoins
 * 
 * @author Sean H. Worthington
 * @version 1/10/2016
 */
public class Bank
{
    /**
     * Number of milliseconds to wait until the RAIDA are ignored. 
     */
    private final int MAX_RESPONSE_TIME = 10000;//10 seconds - How long the RAIDA will be allowed to all respond
     /**
     * location of the "bank" folder.
     */
    private final String rootFolder;
    /**
     * The object that allows communication to the RAIDA
     */
    public RAIDA raida;
    /**
     * Used to hold an array of fracked coins while fixing them
     */
    public CloudCoin[] frackedCoins;
    /**
     * Used to hold an array of coins that will be exported
     */
    public CloudCoin[] exportCoins;
/**
 * Total amount of authentic unfractured coins in the bank file 
 */
int totalValueToBank;
/**
 * Total amount of counterfeit coins in the bank file 
 */
int totalValueToCounterfeit;
/**
 * Total amount of fractured coins in the bank file 
 */
    int totalValueToFractured;

    /**
     * CONSTRUCTOR
     * @param location of bank file. 
     */
    public Bank(String rootFolder)
    {
        // initialise instance variables
        this.rootFolder = rootFolder;
        raida = new RAIDA( MAX_RESPONSE_TIME );
        totalValueToBank = 0;
    totalValueToCounterfeit = 0;
    totalValueToFractured = 0;

    }

   
    /**
     * Method countCoins counts how many coins of a given extension are in a given directory
     *
     * @param directoryPath A folder
     * @param extension A file extension like .bank
     * @return An array of the differnt denominations of coins. The first index is the total amount of all coins added together. 
     */
    public int[] countCoins( String directoryPath, String extension ){
        int totalCount =  0;
        int[] returnCounts = new int[6];//0. Total, 1.1s, 2,5s, 3.25s 4.100s, 5.250s
        String[] fileNames = selectFileNamesWithSameExtension(directoryPath, extension);
        for(int i = 0 ; i < fileNames.length; i++){
            String[] nameParts = fileNames[i].split("\\.");
            String denomination = nameParts[0];
            switch( denomination ){
                case "1": returnCounts[0] += 1; returnCounts[1]+= 1; break;
                case "5": returnCounts[0] += 5; returnCounts[2]+= 5; break;
                case "25": returnCounts[0] += 25; returnCounts[3]+= 25; break;
                case "100": returnCounts[0] += 100; returnCounts[4]+= 100; break;
                case "250": returnCounts[0] += 250; returnCounts[5]+= 250; break;  
            }//end switch

        }//end for each coin
        return returnCounts;
    }//end count coins

    /**
     * Method deleteCoin
     *
     * @param path The folder that the coin is located in
     * @return true if the coin is deleted and false if it does not get deleted. 
     */
    public boolean deleteCoin( String path ){
        boolean deleted = false;
        File f  = new File( path ); //System.out.println("Deleteing Coin: "+path + this.fileName + extension);
        try {
            deleted = f.delete(); if(deleted){ }else{
               // System.out.println("Delete operation is failed.");
            }//end else
        }catch(Exception e){
            e.printStackTrace();
        }
        return deleted;
    }//end delete file

    /**
     * Method detectAuthenticity
     *
     * @return The total value put in the bank, the total value that was counterfeited, and total value that was fractured
     */
    public int[] detectAuthenticity(){
        //LOAD THE .suspect COINS ONE AT A TIME AND TEST THEM
        String[] suspectFileNames  = selectAllFileNamesInBank( "suspect" );
        int totalValueToBank = 0;
        int totalValueToCounterfeit = 0;
        int totalValueToFractured = 0;
        int[] results = new int[3];
        CloudCoin newCC;
        for(int j = 0; j < suspectFileNames.length; j++){
            try{
                //System.out.println("Construct Coin: "+rootFolder + suspectFileNames[j]);
                newCC = new CloudCoin( rootFolder + suspectFileNames[j]);
                System.out.println("Detecting SN #"+ newCC.sn +", Denomination: "+ newCC.getDenomination() );
                CloudCoin detectedCC =  raida.detectCoin( newCC );//Checks all 25 GUIDs in the Coin and sets the status. 
                
                detectedCC.saveCoin( detectedCC.extension );//save coin as bank
                detectedCC.consoleReport();
                deleteCoin( rootFolder + suspectFileNames[j] );
                switch( detectedCC.extension ){
                    case "bank": totalValueToBank++; break;
                    case "fractured": totalValueToFractured++; break;//fracked still ads value to the bank
                    case "counterfeit": totalValueToCounterfeit++; break;
                }//end switch on the place the coin will go 
            }catch(FileNotFoundException ex){
            }catch(IOException ioex){            }//end try catch
        }//end for each coin to import
        //System.out.println("Results of Import:");
        results[0] = totalValueToBank; 
        results[1] = totalValueToCounterfeit; //System.out.println("Counterfeit and Moved to trash: "+totalValueToCounterfeit);
        results[2] = totalValueToFractured;//System.out.println("Fracked and Moved to Fracked: "+ totalValueToFractured);
        return results;
    }//end detectAuthenticity

    /**
     * Method exportAllJson. Exports all files of a certain extension to a JSON file. 
     *
     * @param tag A string that can be included in the file name
     * @param extension The extension that will be given to the exported file. 
     * @return The return value
     */
    public boolean exportAllJson( String tag, String extension ){
        boolean jsonExported = true;
        int totalSaved = 0;
        String[] allFileNames = selectAllFileNamesInBank( extension );//list all file names with bank extension

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

    /**
     * Method exportJpeg Exports a bunch of JPG images. 
     *
     * @param m1 How many jpgs to create of the 1s denomination. 
     * @param m5 How many jpgs to create of the 5s denomination. 
     * @param m25 How many jpgs to create of the 25s denomination. 
     * @param m100 How many jpgs to create of the 100s denomination. 
     * @param m250 How many jpgs to create of the 250s denomination. 
     * @param tag A string that will be included in the jpg file name.
     * @param directory to put the jpeg into. 
     */
    public void exportJpeg(int m1, int m5, int m25, int m100, int m250, String tag, String directory ){
        boolean jsonExported = true;
        int totalSaved = m1 + ( m5 * 5 ) + ( m25 * 25 ) + (m100 * 100 ) + ( m250  * 250 );//Track the total coins
        int coinCount = m1 + m5 + m25 + m100 + m250;
        /* CONSRUCT JSON STRING FOR SAVING */
        String[] coinsToDelete =  new String[coinCount];
        String[] bankedFileNames = selectAllFileNamesInBank("bank");//list all file names with bank extension
        String[] frackedFileNames = selectAllFileNamesInBank("fracked");//list all file names with bank extension
        bankedFileNames = concatArrays(bankedFileNames, frackedFileNames);//Add the two arrays together

        
        String r = rootFolder;
        String b = "bank";
        String t = tag;
        String p = directory;
        /* SET JPEG, WRITE JPEG and DELETE CLOUDCOINS*/
        int c = 0;//c= counter
        String d ="";   
        CloudCoin jpgCoin = null;
        //Put all the JSON together and add header and footer
        for(int i =0; i< bankedFileNames.length; i++ ){
            d = bankedFileNames[i].split("\\.")[0];//Get's denominiation
            try{

                if( d.equals("1") && m1 > 0 ){ 
                    jpgCoin = new CloudCoin( rootFolder + bankedFileNames[i] );
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

            }catch(FileNotFoundException ex){
            }catch(IOException ioex){ }
        }//for each 1 note  
    }//end export

      /**
     * Method exportJpeg Exports a JSON file with a .stack exension with a lot of CloudCoins in it. 
     *
     * @param m1 How many json to create of the 1s denomination. 
     * @param m5 How many json to create of the 5s denomination. 
     * @param m25 How many json to create of the 25s denomination. 
     * @param m100 How many json to create of the 100s denomination. 
     * @param m250 How many json to create of the 250s denomination. 
     * @param tag A string that will be included in the json file name.
     * @param directory to put the json into. 
     */
    public boolean exportJson( int m1, int m5, int m25, int m100, int m250, String tag, String directory){
        boolean jsonExported = true;
        int totalSaved = m1 + ( m5 * 5 ) + ( m25 * 25 ) + (m100 * 100 ) + ( m250  * 250 );//Track the total coins
        int coinCount = m1 + m5 + m25 + m100 + m250;
        /* CONSRUCT JSON STRING FOR SAVING */
        String[] coinsToDelete =  new String[coinCount];
        String[] bankedFileNames = selectAllFileNamesInBank("bank");//list all file names with bank extension
        String[] frackedFileNames = selectAllFileNamesInBank("fracked");//list all file names with bank extension
        bankedFileNames = concatArrays(bankedFileNames, frackedFileNames);//Add the two arrays together

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

        if ( stringToFile( json, directory + filename ) ){
            /* DELETE EXPORTED CC FROM BANK */ 
            for(int cc = 0; cc < coinsToDelete.length; cc++){
                // System.out.println("Deleting "+ path + coinsToDelete[cc].fileName + "bank");
                deleteCoin( coinsToDelete[cc] );
            }//end for

        }else{
            //Write Failed
            jsonExported = false;
        }//end if write was good
        return jsonExported;
    }//end export

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
    }//end getFileExtension

    /**
     * Method getOneJSON Extracts a single CloudCoin from a JSON file. 
     *
     * @param fileName A parameter
     * @return The JSON of the coin.
     */
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

    /**
     * Method ifFileExists Checks to see if a file exists
     *
     * @param filePathString A parameter
     * @return True if the file exists, false if the file does not exist.
     */
    public boolean ifFileExists( String filePathString ){
        File f = new File(filePathString);
        if(f.exists() && !f.isDirectory()) { 
            return true;
        }
        return false;
    }//end if file Exists

    /**
     * Method importAllInFolder  Takes all CloudCoins in a folder and brings them into the bank file and give them a .suspect extension. 
     * Must use the importOneFile over and over
     * @param directory A parameter
     */
    public void importAllInFolder(String directory){
        String[] fileNames = selectFileNamesInFolder(directory);//Get a list of all in the folder except the directory "imported"
        for(int i=0; i< fileNames.length; i++){
            System.out.println( fileNames[i]);
        }//end for each file name
        for(int i=0; i< fileNames.length; i++){//Loop through each file. 
            importOneFile( directory, fileNames[i] );
        }//end for each file name
    }//end import

    /**
     * Method importOneFile. Takes one .stack or .jpg file in a folder and imports it into the bank folder with a .suspect extension. 
     *
     * @param directory A parameter
     * @param loadFileName A parameter
     * @return The return value
     */
    public boolean importOneFile(String directory, String loadFileName){
        /*WHAT IS THE FILE'S EXTENSION??*/
        String extension = "";
        int indx = loadFileName.lastIndexOf('.');
        if (indx > 0) {
            extension = loadFileName.substring(indx+1);
        }
        extension = extension.toLowerCase();
        boolean jpg = false;
        if ( extension.equals("jpeg") || extension.equals("jpg")){ jpg =true;   }
        /* IF IT IS A JPEG loadJpeg(). OTHERWISE loadIncome()*/
        if( jpg ){
            if( ! importJpeg( directory , loadFileName )){ 
               // System.out.println("Failed to load JPEG file");
                return false;
            }
        }else{
            if( ! importStack( directory , loadFileName )){ 
               // System.out.println("Failed to load .stack file");
                return false;
            }
        }//end if jpg
        //change imported file to have a .imported extention
        renameFileExtension(loadFileName, "suspect" );
        return true;
    }//end import

    /***
     * GIven a directory and an extension, loads all CloudCoins of that extension
     * 
     */
    public CloudCoin[] loadCoinArray(String directoryPath, String extension){
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
    }//end load coin Array

    /**
     * Method loadFileToString
     *
     * @param jsonfile The path and anme of a file that contains CloudCoins in the form of JSON.
     * @return The return value
     */
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
    }//end load file to string

    /**
     * This method is used to load .chest and .stack files that are in JSON notation.
     * 
     * @param  loadFilePath: The path to the Bank file and the name of the file. 
     * @param  Security: How the ANs are going to be changed during import (Random, Keep, password).
     */
    public boolean importStack( String directory, String loadFilePath ) {  
        boolean isSuccessful = false;
        // System.out.println("Trying to load: " + directory + loadFilePath );
        String incomeJson = ""; 
        // String new fileName = coinCount +".CloudCoin.New"+ rand.nextInt(5000) + "";
        try{
            incomeJson = loadJSON( directory + loadFilePath );
            //  System.out.println(incomeJson);
        }catch( IOException ex ){
            System.out.println( "error " + ex );
        }
        JSONArray incomeJsonArray;
        try{
            JSONObject o = new JSONObject( incomeJson );
            incomeJsonArray = o.getJSONArray("cloudcoin");
            CloudCoin tempCoin = null;
            for (int i = 0; i < incomeJsonArray.length(); i++) {  // **line 2**
                JSONObject childJSONObject = incomeJsonArray.getJSONObject(i);
                int nn     = childJSONObject.getInt("nn");
                int sn     = childJSONObject.getInt("sn");
                JSONArray an = childJSONObject.getJSONArray("an");
                String ed     = childJSONObject.getString("ed");
                //JSONArray aoid = childJSONObject.getJSONArray("aoid");
                // String[] aoids = toStringArray(aoid)
                String aoid = "";//Wipe any old owner notes
                //this.newCoins[i] = new CloudCoin( nn, sn, toStringArray(an), ed, aoid, security );//This could cause memory issues.   
                tempCoin = new CloudCoin( nn, sn, toStringArray(an), ed, "", "suspect" );//security should be change or keep for pans.
                //tempCoin.consoleReport();
                tempCoin.saveCoin("suspect");//Put in bank folder with suspect extension
                moveFileToImported( directory, loadFilePath, "imported");
            }//end for each coin
            isSuccessful = true;
        }catch( JSONException ex){
            System.out.println("Stack File "+ loadFilePath+ " Corrupt. See CloudCoin file api and edit your stack file: " + ex);
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

    /**
     * This method is used to load .jpg and .jpeg files.
     * @param  loadFilePath: The path to the Bank file and the name of the file. 
     * @param  Security: How the ANs are going to be changed during import (Random, Keep, password).
     */
    public boolean importJpeg( String directory, String loadFilePath ) {  
        boolean isSuccessful =false;
        System.out.println("Trying to load: " + directory+ loadFilePath );
        try{
            CloudCoin tempCoin = new CloudCoin( directory + loadFilePath );
            tempCoin.saveCoin("suspect");
            System.out.println("File saved to bank:" + loadFilePath);
            moveFileToImported( directory, loadFilePath, "imported");

            return isSuccessful;
        }catch(FileNotFoundException ex){ System.out.println("File not found:" + loadFilePath);
        }catch(IOException ioex){ System.out.println("IO Exception:" + loadFilePath);
        }//end try catch
        return isSuccessful;
    }//end load income

    /**
     * Method moveFileToImported Moves a file from one folder to subfolder called "imported"
     *
     * @param directory A parameter
     * @param fileName A parameter
     * @param newExtension A parameter
     * @return The return value
     */
    public boolean moveFileToImported(String directory, String fileName, String newExtension){
        String source = directory + fileName;
        String target = directory +"imported\\"+ fileName;
        return new File(source).renameTo(new File(target));
    }

    /**
     * Method renameFileExtension Changes the file extension of a file. 
     *
     * @param source The files name
     * @param newExtension Teh files new extension
     * @return true if renamed false if not. 
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
    public String[] selectAllFileNamesInBank( String ext) {
        File dir = new File("./bank/");
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
    }//End select all file names

    /***
     * Given directory path return an array of strings of all the files in the directory.
     * @parameter directoryPath The location of the directory to be scanned
     * @return filenames The names of all the files in the directory
     */
    public String[] selectFileNamesWithSameExtension(String directoryPath, String extension) {
        File dir = new File(directoryPath);
        String candidateFileExt = "";
        Collection<String> files  =new ArrayList<String>();
        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            for(File file : listFiles){
                if(file.isFile()) {//Only add files with the matching file extension
                    candidateFileExt = getFileExtension( file.getName() );
                    if ( candidateFileExt.equalsIgnoreCase(extension) ){
                        files.add(file.getName());
                    }//end if it is correct file ext.
                }
            }
        }
        return files.toArray(new String[]{});
    }//End select all file names in a folder

    /***
     * Given directory path return an array of strings of all the files in the directory.
     * @parameter directoryPath The location of the directory to be scanned
     * @return filenames The names of all the files in the directory
     */
    public String[] selectFileNamesInFolder(String directoryPath) {
        File dir = new File(directoryPath);
        String candidateFileExt = "";
        Collection<String> files  =new ArrayList<String>();
        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            for(File file : listFiles){
                if(file.isFile()) {//Only add files with the matching file extension
                    files.add(file.getName());
                }
            }
        }
        return files.toArray(new String[]{});
    }//End select all file names in a folder

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

    public int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public String[] toStringArray(JSONArray array) {
        if(array==null)
            return null;
        String[] arr=new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }//end toStringArray

    /**
     * Method concatArrays Takes two arrays and adds them together
     *
     * @param a The first array
     * @param b The second array
     * @return The arrays together
     */
    public String[] concatArrays(String[] a, String[] b) {
        int aLen = a.length;
        int bLen = b.length;
        String[] c= new String[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
}