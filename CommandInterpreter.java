import java.util.Random;
import java.io.*;
import java.nio.file.Files;

public class CommandInterpreter{
    /* Load items for all methods to share*/
    public static KeyboardReader reader = new KeyboardReader();
    public static StateManager stateManager = new StateManager();
    public static Random myRandom = new Random();//This is used for naming new chests
    public static String rootFolder = System.getProperty("user.dir") + File.separator +"bank" + File.separator ;
    public static Bank bank = new Bank(rootFolder);
    public static String prompt = "CloudCoin Bank";

    public static void main(String[] args) {

        printWelcome();

        StateManager stateManager = new StateManager();

        run();//Makes all commands available and loops

        System.out.println("Thank you for using CloudCoin Foundation. Goodbye.");
    }//End main

    public static void run() {
        boolean restart = false;
        while( ! restart )
        {
            String[] commandsAvailable = stateManager.currentState.getCommands();
            System.out.println( "=======================");
            System.out.println( prompt + " Commands Available:");
            int commandCounter = 1;
            for ( String command : commandsAvailable){
                System.out.println( commandCounter + ". "+ command );
                commandCounter++;
            }

            System.out.print( prompt+">");
            String commandRecieved = reader.readString( commandsAvailable );          

            switch( commandRecieved.toLowerCase() ){     
                case "show coins": showCoins(); break;
                case "import": importCoins();  break;
                case "export all": exportAll(); break;
                case "export": export(); break;
                case "fix fracked": fixFracked(); break;
                case "quit": System.out.println("Goodbye!"); System.exit(0); break;
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

    public static void showCoins(){
        //This is for consol apps.
        int total_1 =  0;
        int total_5 =  0;
        int total_25 = 0;
        int total_100 =  0;
        int total_250 =  0;

        //System.out.println("'\nRoot folder is " + rootFolder);
        CloudCoin[] bankedCoins = bank.loadCoinArray( rootFolder ,"bank");
        total_1 =  bank.countCoins( bankedCoins, 1 );
        total_5 =  bank.countCoins( bankedCoins, 5 );
        total_25 =  bank.countCoins( bankedCoins, 25 );
        total_100 =  bank.countCoins( bankedCoins, 100 );
        total_250 =  bank.countCoins( bankedCoins, 250 );

        System.out.println("Your Bank Inventory:");
        int grandTotal = ( total_1*1 )+ ( total_5 * 5 ) + ( total_25 * 25 ) + ( total_100 * 100 ) + ( total_250 * 250 ); 
        System.out.println("Total: " + grandTotal );
        System.out.print("  1s: "+ total_1  +" || ");
        System.out.print("  5s: "+ total_5  +" ||");
        System.out.print(" 25s: "+ total_25 +" ||" );
        System.out.print("100s: "+ total_100+" ||");
        System.out.println("250s: "+ total_250 );

        CloudCoin[] frackedCoins = bank.loadCoinArray( rootFolder ,"fracked");
        if( frackedCoins.length > 0 ){
            total_1 =  bank.countCoins( frackedCoins, 1  );
            total_5 =  bank.countCoins( frackedCoins, 5  );
            total_25 =  bank.countCoins( frackedCoins, 25  );
            total_100 =  bank.countCoins( frackedCoins, 100  );
            total_250 =  bank.countCoins( frackedCoins, 250  );

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
        CloudCoin[] lostCoins = bank.loadCoinArray( rootFolder ,"lost");
        if( lostCoins.length > 0 ){
            total_1 =  bank.countCoins( lostCoins, 1 );
            total_5 =  bank.countCoins( lostCoins, 5 );
            total_25 =  bank.countCoins( lostCoins, 25 );
            total_100 =  bank.countCoins( lostCoins, 100 );
            total_250 =  bank.countCoins( lostCoins, 250 );

            System.out.println("Your lost Inventory:");
            System.out.print("  1s: "+ total_1  +" || ");
            System.out.print("  5s: "+ total_5  +" ||");
            System.out.print(" 25s: "+ total_25 +" ||" );
            System.out.print("100s: "+ total_100+" ||");
            System.out.println("250s: "+ total_250 );

        }else{
            System.out.println("No lost coins. " );
        }
        CloudCoin[] counterfeitCoins = bank.loadCoinArray( rootFolder ,"counterfeit");
        if( counterfeitCoins.length > 0 ){
            total_1 =  bank.countCoins( counterfeitCoins, 1 );
            total_5 =  bank.countCoins( counterfeitCoins, 5 );
            total_25 =  bank.countCoins( counterfeitCoins, 25 );
            total_100 =  bank.countCoins( counterfeitCoins, 100 );
            total_250 =  bank.countCoins( counterfeitCoins, 250 );

            System.out.println("Your counterfeit Inventory:");
            System.out.print("  1s: "+ total_1  +" || ");
            System.out.print("  5s: "+ total_5  +" ||");
            System.out.print(" 25s: "+ total_25 +" ||" );
            System.out.print("100s: "+ total_100+" ||");
            System.out.println("250s: "+ total_250 );

        }else{
            System.out.println("No counterfeit coins. " );
        }
        //write file name to bank

    }//end show

    public static void importCoins(){
        System.out.println("Loading all CloudCoins in your import folder: What is the path to your import folder?");
        String loadFileName = reader.readString( false );   
        //Check to see if folder exists and that folder is not empty. 
        File f = new File( loadFileName );
        if (f.exists() && f.isDirectory()) {
            System.out.println( loadFileName + " not found. Please check your file name and try again."); 
            return;
        }else{
            bank.importAllInFolder( loadFileName );
            fixFracked();
        }
        showCoins();
    }//end import

    public static void exportAll(){
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
            bank.exportCoins = bank.loadCoinArray( rootFolder, "bank");
            bank.exportAllJson(tag2, "bank");
            break;
            case 2:
            bank.exportCoins = bank.loadCoinArray( rootFolder,"lost");
            bank.exportAllJson(tag2,  "lost");
            break;
            case 3: 
            bank.exportCoins = bank.loadCoinArray( rootFolder,"counterfeit");
            bank.exportAllJson( tag2, "counterfeit");
            break;
            case 4: 
            bank.exportCoins = bank.loadCoinArray( rootFolder,"fracked");
            bank.exportAllJson(tag2,  "fracked");
            break;
            case 5: 
            bank.exportCoins = bank.loadCoinArray( rootFolder,"income");
            bank.exportAllJson( tag2, "income");
            break;
        }//export all
    }//end export all

    public static void export(){
        // System.out.println("Root folder is " + rootFolder);
        bank.bankedCoins = bank.loadCoinArray( rootFolder ,"bank");
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
    }//end export One
    
    
    public static void fixFracked(){
        //Load coins from file in to banks fracked array
        bank.totalValueToBank = 0;
        bank.totalValueToFractured = 0;
        bank.totalValueLost = 0;
        bank.totalValueToCounterfeit=0;
        bank.frackedCoins = bank.loadCoinArray( rootFolder,"fracked");

        System.out.println("You  have " + bank.frackedCoins.length + " fracked coins.");
        /* LOOP THROUGH EVERY COIN THAT IS FRACKED */
        for(int k = 0; k < bank.frackedCoins.length; k++){
            //frackedCoins[k].reportStatus();
            System.out.println("Unfracking SN #"+bank.frackedCoins[k].sn +", Denomination: "+ bank.frackedCoins[k].getDenomination() );
            System.out.println("This may take a minute or two." );
       
            bank.raida.fixCoin( bank.frackedCoins[k] );//Checks all 25 GUIDs in the Coin and sets the status.

            //Check CloudCoin's hp. 
            int RAIDAHealth2 = 25;
            //Now the coin has been fixed. See if there were some improvements. 
            bank.frackedCoins[k].hp = 25;
            for(int i = 0; i < 25;i++){
                if ( i % 5 == 0 ) { System.out.println("");}//Give every five statuses a line break
                if( bank.frackedCoins[k].pastStatus[i] == "pass"){    
                    //Keep ans because it is now good
                }
                else if( bank.frackedCoins[k].pastStatus[i] == "fail"){ 
                    bank.frackedCoins[k].hp--; 
                }
                else{
                    RAIDAHealth2--;
                }//check if failed
                String fi = String.format("%02d", i);//Pad numbers with two digits
                System.out.print("RAIDA"+ fi +": "+ bank.frackedCoins[k].pastStatus[i].substring(0,4) + " | " );
            }//end switch on the place the coin will go
            System.out.println("\nRAIDA Health " + RAIDAHealth2 + "/25");
            switch( bank.frackedCoins[k].extension ){
                case "bank": bank.totalValueToBank++; break;
                case "fractured": bank.totalValueToFractured++; break;
                case "lost": bank.totalValueLost++; break;
                case "counterfeit": bank.totalValueToCounterfeit++; break;
            }//end for each guid
        }//end for each fracked coin
        //REPORT ON DETECTION OUTCOME
        System.out.println("Results of Fix Fractured:");
        System.out.println("Good and Moved in Bank: "+ bank.totalValueToBank);
        System.out.println("Counterfeit and Moved to trash: "+bank.totalValueToCounterfeit);
        System.out.println("Still Fracked and Moved to Fracked: "+ bank.totalValueToFractured);
        System.out.println("Lost and Moved to Lost: "+ bank.totalValueLost);
    }//end fix fracked
}//EndMain
