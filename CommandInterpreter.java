import java.util.Arrays;
import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.Random;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;
//import org.json.*;
public class CommandInterpreter{
    /* Load items for all methods to share*/
    //final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();//For generating PANs (Proposed Authenticity Numbers)
    public static KeyboardReader reader = new KeyboardReader();
    public static StateManager stateManager = new StateManager();
    //private static ActivityLogger log = new ActivityLogger();

    public static Random myRandom = new Random();//This is used for naming new chests
    //public static String rootFolder = "."+ File.separator + "Bank" + File.separator;
    public static String rootFolder = System.getProperty("user.dir") + File.separator +"Bank" + File.separator ;
    /* CHEST: Load variables for Chest Mode to use*/
    public static Bank bank = new Bank(rootFolder);
    //public static String chestTopFileName;
    //public static String chestBottomFileName;
    //public static int[][] topChestRegister;
    //public static int[][] bottomChestRegister;
    //public static String topChestTag;
    //public static String bottomChestTag;
    //public static CloudCoin testCoin;
    /* INCOME: Load variables for Income Mode to use*/
    //public static int incomeTotalMovedToBank = 0;
    //public static int incomeTotalMovedToFracked  = 0;
    //public static int incomeTotalMovedToCounterfeit  = 0;
    //public static int incomeTotalValueMoved = 0;
    //public static String tagWhenMoving;
    //public static RAIDA[] raidaArray = new RAIDA[25];
    //public static String testCoinName = rootFolder + "1.CloudCoin.1.127002.test";
    public static String prompt = "Start Mode";
    //public static ExecutorService executor = Executors.newFixedThreadPool(25);
    //public static ExecutorService executor3 = Executors.newFixedThreadPool(3);
    public static void main(String[] args) {

        printWelcome();
        //Load up from files
        StateManager stateManager = new StateManager();
        //Start the Program. 
        run();

        System.out.println("Thank you for using CloudCoin Foundation. Goodbye.");
    }//End main

    public static void run() {
        boolean restart = false;
        //System.out.println( stateManager.currentState.getLongDescription() );
        int commandCounter = 0;
        while( ! restart )
        {
            String[] commandsAvailable = stateManager.currentState.getCommands();
            System.out.println( "=======================");
            System.out.println( prompt + " Commands Available:");
            commandCounter = 1;
            for ( String command : commandsAvailable)
            {
                System.out.println( commandCounter + ". "+ command );
                commandCounter++;
            }

            System.out.print( prompt+">");
            String commandRecieved = reader.readString( commandsAvailable );          

            switch( commandRecieved.toLowerCase() )
            {     

                case "show coins":
                bank.showCoins();
                break;

                case "export all":
                System.out.println("Which type of coin do you want to export all of?");
                System.out.println("1. bank (all authentic coins)");
                System.out.println("2. lost coins");
                System.out.println("3. Counterfeit coins");
                System.out.println("4. Fracked Coins");
                System.out.println("5. Income (Coins that have not been detected yet)");
                //String[] answers = {"bank","lost","counterfeit","fracked","income"};
                int exportAll = reader.readInt( 1, 5 );
                System.out.println("What is the path and folder you want to store it in? eg. c:\\temp");
                String jsonpath2 = reader.readString(false);
                System.out.println("What tag will you add to the file?");
                String tag2 = reader.readString(false);

                switch( exportAll ){
                    case 1: 
                    bank.exportCoins = bank.loadCoins( rootFolder, "bank");
                    bank.exportAllJson(tag2, "bank");
                    break;
                    case 2:
                    bank.exportCoins = bank.loadCoins( rootFolder,"lost");
                    bank.exportAllJson(tag2,  "lost");
                    break;
                    case 3: 
                    bank.exportCoins = bank.loadCoins( rootFolder,"counterfeit");
                    bank.exportAllJson( tag2, "counterfeit");
                    break;
                    case 4: 
                    bank.exportCoins = bank.loadCoins( rootFolder,"fracked");
                    bank.exportAllJson(tag2,  "fracked");
                    break;
                    case 5: 
                    bank.exportCoins = bank.loadCoins( rootFolder,"income");
                    bank.exportAllJson( tag2, "income");
                    break;
                }//export all
                break;
                /*EXPORT*/
                case "export":
                // System.out.println("Root folder is " + rootFolder);
                bank.bankedCoins = bank.loadCoins( rootFolder ,"bank");
                int total_1 =  bank.countCoins( bank.bankedCoins, 1 );
                int total_5 =  bank.countCoins( bank.bankedCoins, 5 );
                int total_25 =  bank.countCoins( bank.bankedCoins, 25 );
                int total_100 =  bank.countCoins( bank.bankedCoins, 100 );
                int total_250 =  bank.countCoins( bank.bankedCoins, 250 );

                System.out.println("Your Bank Inventory:");
                System.out.println("  1s: "+ total_1);
                System.out.println("  5s: "+ total_5);
                System.out.println(" 25s: "+ total_25 );
                System.out.println("100s: "+ total_100);
                System.out.println("250s: "+ total_250 );
                //get all names in the folder
                //state how many 1, 5, 25, 100 and 250
                int exp_1, exp_5, exp_25, exp_100, exp_250;
                exp_1 = 0;
                exp_5 = 0;
                exp_25 = 0;
                exp_100 = 0;
                exp_250 = 0;

                System.out.println("Do you want to export your CloudCoin to (1)jpgs or (2) stack (JSON) file?");
                int file_type = reader.readInt(1,2 ); //1 jpg 2 stack

                if( total_1 > 0 ){
                    System.out.println("How many 1s do you want to export?");
                    exp_1 = reader.readInt(0,total_1 );
                }//if 1s not zero 
                if( total_5 > 0 ){
                    System.out.println("How many 5s do you want to export?");
                    exp_5 = reader.readInt(0,total_5 );
                }//if 1s not zero 
                if( total_25 > 0 ){
                    System.out.println("How many 25s do you want to export?");
                    exp_25 = reader.readInt(0,total_25 );
                }//if 1s not zero 
                if( total_100 > 0 ){
                    System.out.println("How many 100s do you want to export?");
                    exp_100 = reader.readInt(0,total_100 );
                }//if 1s not zero 
                if( total_250 > 0 ){
                    System.out.println("How many 250s do you want to export?");
                    exp_250 = reader.readInt(0,total_250 );
                }//if 1s not zero 

                //move to export
                System.out.println("What is the path and folder you want to store it in? eg. c:\\temp");
                String jsonpath = reader.readString(false);
                System.out.println("What tag will you add to the file?");
                String tag = reader.readString(false);

                if( file_type == 2){
                    bank.exportJson(exp_1, exp_5, exp_25, exp_100, exp_250, tag);
                    //stringToFile( json, "test.txt");
                }else{
                    bank.exportJpeg(exp_1, exp_5, exp_25, exp_100, exp_250, tag);

                }//end if type jpge or stack

                System.out.println("Exporting CloudCoins Completed.");
                break;
                case "quit":
                System.out.println("Goodbye!"); System.exit(0);
                break;
                case "import":
                System.out.println("What is the path and name of the file you want to load?");
                String loadFileName = reader.readString( false );   
                //load the coins into an array of coin objects
                if( !bank.ifFileExists(loadFileName)){
                    System.out.println( loadFileName + " not found. Please check your file name and try again."); 
                    break;
                }else{
                    bank.importAll(loadFileName);
                }
                break;
                case "fix fracked":
                bank.fixFracked();
                break;
                default: System.out.println("Command failed. Try again."); break;
            }//end switch
        }//end while
    }//end run method

    /**
     * Print out the opening message for the player. 
     */
    public static void printWelcome() {
        System.out.println("Welcome to CloudCoin Foundation Opensource.");
        System.out.println("The Software is provided as is, with all faults, defects and errors,");
        System.out.println("and without warranty of any kind.");
    }//End print welcome

}//EndMain
