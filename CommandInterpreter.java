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

        System.out.println("Thank you for using CloudCoin Core. Goodbye.");
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
        int[] bankTotals = bank.countCoins(rootFolder ,"bank");
        int[] frackedTotals = bank.countCoins(rootFolder ,"fracked");
        int[] counterfeitTotals = bank.countCoins(rootFolder ,"counterfeit");
       
        System.out.println("Your Bank Inventory:");            
        int grandTotal = bankTotals[0] + frackedTotals[0]  ; 
        System.out.println("Total: " + grandTotal );
        System.out.print("  1s: "+ (bankTotals[1] + frackedTotals[1]) + " || ");
        System.out.print("  5s: "+ (bankTotals[2] + frackedTotals[2]) + " ||");
        System.out.print(" 25s: "+ (bankTotals[3] + frackedTotals[3]) + " ||" );
        System.out.print("100s: "+ (bankTotals[4] + frackedTotals[4]) + " ||");
        System.out.println("250s: "+ (bankTotals[5] + frackedTotals[5])  );
        
        
        if( counterfeitTotals[0] > 0 ){//if there is some counterfeits
            System.out.println("Your counterfeit Inventory:");
        System.out.println("Total: " + grandTotal );
        System.out.print("  1s: "+ counterfeitTotals[1] + " || ");
        System.out.print("  5s: "+ counterfeitTotals[2] + " ||");
        System.out.print(" 25s: "+ counterfeitTotals[3] + " ||" );
        System.out.print("100s: "+ counterfeitTotals[4] + " ||");
        System.out.println("250s: "+ counterfeitTotals[5]  );
        }else{
            System.out.println("No counterfeit coins. " );
        }
        //write file name to bank

    }//end show

    public static void importCoins(){
        System.out.println("Loading all CloudCoins in your import folder: c:\\import\\");
        String loadDirectoryName = "c:\\import\\";//reader.readString( false );   
        //Check to see if folder exists and that folder is not empty. 
        File f = new File( loadDirectoryName );
        if ( !f.exists() || !f.isDirectory()) {
            System.out.println( loadDirectoryName + " not found. Please check your file name and try again."); 
            return;
        }else{
            bank.importAllInFolder( loadDirectoryName );//Move all coins to the bank folder and give them a .suspect name. 
            int[] detectionResults =  bank.detectAuthenticity( );//Get all the .suspect files in the bank folder and check them for authenticity. 
            
            System.out.println("Total Received in bank: " + (detectionResults[0] + detectionResults[2]) );//And the bank and the fractured for total
            System.out.println("Total Counterfeit: " + detectionResults[1]);

           // fixFracked(); Change later
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
        System.out.println("Exporting to your export folder: c:\\export\\");
        String jsonpath2 = "c:\\export"; //reader.readString(false);
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
        
        int[] bankTotals = bank.countCoins(rootFolder ,"bank");
        int[] frackedTotals = bank.countCoins(rootFolder ,"fracked");
       
        System.out.println("Your Bank Inventory:");            
        int grandTotal = bankTotals[0] + frackedTotals[0]  ; 
        System.out.println("Total: " + grandTotal );
        System.out.print("  1s: "+ (bankTotals[1] + frackedTotals[1]) + " || ");
        System.out.print("  5s: "+ (bankTotals[2] + frackedTotals[2]) + " ||");
        System.out.print(" 25s: "+ (bankTotals[3] + frackedTotals[3]) + " ||" );
        System.out.print("100s: "+ (bankTotals[4] + frackedTotals[4]) + " ||");
        System.out.println("250s: "+ (bankTotals[5] + frackedTotals[5])  );
        
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

        if( bankTotals[1] + frackedTotals[1] > 0 ){
            System.out.println("How many 1s do you want to export?");
            exp_1 = reader.readInt(0,bankTotals[1] + frackedTotals[1]  );
        }//if 1s not zero 
        if( bankTotals[2] + frackedTotals[2] > 0 ){
            System.out.println("How many 5s do you want to export?");
            exp_5 = reader.readInt(0,bankTotals[2] + frackedTotals[2]  );
        }//if 1s not zero 
        if( bankTotals[3] + frackedTotals[3] > 0 ){
            System.out.println("How many 25s do you want to export?");
            exp_25 = reader.readInt(0,bankTotals[3] + frackedTotals[3]  );
        }//if 1s not zero 
        if( bankTotals[4] + frackedTotals[4] > 0 ){
            System.out.println("How many 100s do you want to export?");
            exp_100 = reader.readInt(0,bankTotals[4] + frackedTotals[4]  );
        }//if 1s not zero 
        if( bankTotals[5] + frackedTotals[5] > 0 ){
            System.out.println("How many 250s do you want to export?");
            exp_250 = reader.readInt(0,bankTotals[5] + frackedTotals[5]  );
        }//if 1s not zero 

        //move to export
        System.out.println("Exporting to: c:\\export\\");
        String directory = "c:\\export\\";//reader.readString(false);
        System.out.println("What tag will you add to the file?");
        String tag = reader.readString(false);

        if( file_type == 2){
            bank.exportJson(exp_1, exp_5, exp_25, exp_100, exp_250, tag, directory);
            //stringToFile( json, "test.txt");
        }else{
            bank.exportJpeg(exp_1, exp_5, exp_25, exp_100, exp_250, tag, directory);

        }//end if type jpge or stack

        System.out.println("Exporting CloudCoins Completed.");
    }//end export One

    public static void fixFracked(){
        //Load coins from file in to banks fracked array
        bank.totalValueToBank = 0;
        bank.totalValueToFractured = 0;
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
                case "counterfeit": bank.totalValueToCounterfeit++; break;
            }//end for each guid
        }//end for each fracked coin
        //REPORT ON DETECTION OUTCOME
        System.out.println("Results of Fix Fractured:");
        System.out.println("Good and Moved in Bank: "+ bank.totalValueToBank);
        System.out.println("Counterfeit and Moved to trash: "+bank.totalValueToCounterfeit);
        System.out.println("Still Fracked and Moved to Fracked: "+ bank.totalValueToFractured);
    }//end fix fracked
}//EndMain
