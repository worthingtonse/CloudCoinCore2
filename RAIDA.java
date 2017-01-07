import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.List;
import java.util.ArrayList;
/**
 * Write a description of class RAIDA here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RAIDA
{
    // instance variables - replace the example below with your own
    public DetectionAgent[] raidaArray;
    private ExecutorService executor;
    private ExecutorService executor3;
    private Directory directory;
    
    /**
     * Constructor for objects of class RAIDA
     */
    public RAIDA()
    {
        // initialise instance variables
        raidaArray = new DetectionAgent[25];
        executor = Executors.newFixedThreadPool(25);
        executor3 = Executors.newFixedThreadPool(3);
        directory = new Directory();
    }

    
 
    
    /***
     * This sends an echo to each RAIDA server and records the results.
     */
    public void echoAll(){
        // String echo1 = raidaArray[0].echo());
        //create a callable for each method
        Callable<Void> callable0 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{ raidaArray[0].echo();return null; }};
        Callable<Void> callable1 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {raidaArray[1].echo(); return null;}};
        Callable<Void> callable2 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {raidaArray[2].echo();return null;}};
        Callable<Void> callable3 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[3].echo();return null;}};
        Callable<Void> callable4 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[4].echo();return null;}};
        Callable<Void> callable5 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{raidaArray[5].echo();return null; }};
        Callable<Void> callable6 = new Callable<Void>(){
            @Override
                public Void call() throws Exception {raidaArray[6].echo();return null;} };
        Callable<Void> callable7 = new Callable<Void>(){
                @Override
                public Void call() throws Exception { raidaArray[7].echo(); return null;}};
        Callable<Void> callable8 = new Callable<Void>(){
                @Override
                public Void call() throws Exception { raidaArray[8].echo();return null;}};
        Callable<Void> callable9 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[9].echo();return null; }};
        Callable<Void> callable10 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{ raidaArray[10].echo();return null;}};
        Callable<Void> callable11 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[11].echo(); return null;} };
        Callable<Void> callable12 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[12].echo();return null; }};
        Callable<Void> callable13 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[13].echo();return null; }};
        Callable<Void> callable14 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{raidaArray[14].echo();return null;}};
        Callable<Void> callable15 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[15].echo();return null;} };

        Callable<Void> callable16 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{raidaArray[16].echo(); return null;}};
        Callable<Void> callable17 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{ raidaArray[17].echo();return null;}};
        Callable<Void> callable18 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[18].echo(); return null;} };
        Callable<Void> callable19 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {raidaArray[19].echo();return null;}};
        Callable<Void> callable20 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[20].echo(); return null; }};
        Callable<Void> callable21 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{raidaArray[21].echo();return null;}};
        Callable<Void> callable22 = new Callable<Void>(){
                @Override
                public Void call() throws Exception { raidaArray[22].echo();return null;}};
        Callable<Void> callable23 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {raidaArray[23].echo();return null;}};
        Callable<Void> callable24 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[24].echo(); return null;}};
        //add to a list
        List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
        taskList.add(callable0);
        taskList.add(callable1);
        taskList.add(callable2);
        taskList.add(callable3);
        taskList.add(callable4);
        taskList.add(callable5);
        taskList.add(callable6);
        taskList.add(callable7);
        taskList.add(callable8);
        taskList.add(callable9);
        taskList.add(callable10);
        taskList.add(callable11);
        taskList.add(callable12);
        taskList.add(callable13);
        taskList.add(callable14);
        taskList.add(callable15);
        taskList.add(callable16);
        taskList.add(callable17);
        taskList.add(callable18);
        taskList.add(callable19);
        taskList.add(callable20);
        taskList.add(callable21);
        taskList.add(callable22);
        taskList.add(callable23);
        taskList.add(callable24);

        try{
            //start the threads
            List<Future<Void>> futureList = executor.invokeAll(taskList);

            for(Future<Void> voidFuture : futureList){
                try{
                    //check the status of each future.  get will block until the task
                    //completes or the time expires
                    voidFuture.get(100, TimeUnit.MILLISECONDS);
                }
                catch (ExecutionException e) {
                    System.out.println("Error executing task " + e.getMessage());
                }
                catch (TimeoutException e){
                    System.out.println("Timed out executing task" + e.getMessage());
                }
            }
        } catch (InterruptedException ie){
            //do something if you care about interruption;
        }
    }

   
    
    /**
     * Method detectCoin
     *
     * @param newCoin A parameter
     */
    public CloudCoin detectCoin( CloudCoin cc ){
        // Make an array to capture the results of the detection. 
                //create a callable for each method  
        Callable<Void> callable0 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[0] = raidaArray[0].detect( cc.nn, cc.sn, cc.ans[0], cc.pans[0], cc.getDenomination() ); return null;}};
        Callable<Void> callable1 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[1]  = raidaArray[1].detect(cc.nn, cc.sn, cc.ans[1], cc.pans[1], cc.getDenomination());return null; }};
        Callable<Void> callable2 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[2]  = raidaArray[2].detect(cc.nn, cc.sn, cc.ans[2], cc.pans[2], cc.getDenomination()); return null;}};
        Callable<Void> callable3 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[3]  = raidaArray[3].detect(cc.nn, cc.sn, cc.ans[3], cc.pans[3], cc.getDenomination());return null;}};
        Callable<Void> callable4 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[4]  = raidaArray[4].detect(cc.nn, cc.sn, cc.ans[4], cc.pans[4], cc.getDenomination()); return null;}};
        Callable<Void> callable5 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[5]  = raidaArray[5].detect(cc.nn, cc.sn, cc.ans[5], cc.pans[5], cc.getDenomination());return null;}};
        Callable<Void> callable6 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[6] = raidaArray[6].detect(cc.nn, cc.sn, cc.ans[6], cc.pans[6], cc.getDenomination());return null;} };
        Callable<Void> callable7 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[7]  = raidaArray[7].detect(cc.nn, cc.sn, cc.ans[7], cc.pans[7], cc.getDenomination());return null;}};
        Callable<Void> callable8 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[8]  = raidaArray[8].detect(cc.nn, cc.sn, cc.ans[8], cc.pans[8], cc.getDenomination());return null;}};
        Callable<Void> callable9 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[9] = raidaArray[9].detect(cc.nn, cc.sn, cc.ans[9], cc.pans[9], cc.getDenomination()); return null; }};
        Callable<Void> callable10 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[10] = raidaArray[10].detect(cc.nn, cc.sn, cc.ans[10], cc.pans[10], cc.getDenomination());return null;} };
        Callable<Void> callable11 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[11] = raidaArray[11].detect(cc.nn, cc.sn, cc.ans[11], cc.pans[11], cc.getDenomination());return null;}};
        Callable<Void> callable12 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[12] = raidaArray[12].detect(cc.nn, cc.sn, cc.ans[12], cc.pans[12], cc.getDenomination());return null;}};
        Callable<Void> callable13 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[13] = raidaArray[13].detect(cc.nn, cc.sn, cc.ans[13], cc.pans[13], cc.getDenomination());return null;}};
        Callable<Void> callable14 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[14] = raidaArray[14].detect(cc.nn, cc.sn, cc.ans[14], cc.pans[14], cc.getDenomination());System.out.print("."); return null;}};
        Callable<Void> callable15 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[15] = raidaArray[15].detect(cc.nn, cc.sn, cc.ans[15], cc.pans[15], cc.getDenomination()); return null;}};
        Callable<Void> callable16 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[16] = raidaArray[16].detect(cc.nn, cc.sn, cc.ans[16], cc.pans[16], cc.getDenomination());return null; } };
        Callable<Void> callable17 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[17] = raidaArray[17].detect(cc.nn, cc.sn, cc.ans[17], cc.pans[17], cc.getDenomination()); return null;}};
        Callable<Void> callable18 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[18] = raidaArray[18].detect(cc.nn, cc.sn, cc.ans[18], cc.pans[18], cc.getDenomination()); return null;}};
        Callable<Void> callable19 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[19] = raidaArray[19].detect(cc.nn, cc.sn, cc.ans[19], cc.pans[19], cc.getDenomination()); return null; }};
        Callable<Void> callable20 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[20] = raidaArray[20].detect(cc.nn, cc.sn, cc.ans[20], cc.pans[20], cc.getDenomination());return null; } };
        Callable<Void> callable21 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[21] = raidaArray[21].detect(cc.nn, cc.sn, cc.ans[21], cc.pans[21], cc.getDenomination()); return null; }};
        Callable<Void> callable22 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[22] = raidaArray[22].detect(cc.nn, cc.sn, cc.ans[22], cc.pans[22], cc.getDenomination());   return null; } };
        Callable<Void> callable23 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[23] = raidaArray[23].detect(cc.nn, cc.sn, cc.ans[23], cc.pans[23], cc.getDenomination()); return null;  }  };
        Callable<Void> callable24 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[24] = raidaArray[24].detect(cc.nn, cc.sn, cc.ans[24], cc.pans[24], cc.getDenomination());return null;}};
        //add to a list
        List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
        taskList.add(callable0);
        taskList.add(callable1);
        taskList.add(callable2);
        taskList.add(callable3);
        taskList.add(callable4);
        taskList.add(callable5);
        taskList.add(callable6);
        taskList.add(callable7);
        taskList.add(callable8);
        taskList.add(callable9);
        taskList.add(callable10);
        taskList.add(callable11);
        taskList.add(callable12);
        taskList.add(callable13);
        taskList.add(callable14);
        taskList.add(callable15);
        taskList.add(callable16);
        taskList.add(callable17);
        taskList.add(callable18);
        taskList.add(callable19);
        taskList.add(callable20);
        taskList.add(callable21);
        taskList.add(callable22);
        taskList.add(callable23);
        taskList.add(callable24);

        try{
            //start the threads
            List<Future<Void>> futureList = executor.invokeAll(taskList);

            for(Future<Void> voidFuture : futureList){
                try{
                    //check the status of each future.  get will block until the task
                    //completes or the time expires
                    voidFuture.get(100, TimeUnit.MILLISECONDS);
                }
                catch (ExecutionException e){
                    System.out.println("Error executing task " + e.getMessage());
                }
                catch (TimeoutException e){
                    System.out.println("Timed out executing task" + e.getMessage());
                }
            }
        }
        catch (InterruptedException ie){
            //do something if you care about interruption;
        }
        cc.setAnsToPansIfPassed();
        cc.gradeStatus(); //sets the grade and figures out what the file extension should be (bank, fracked, counterfeit, lost
        return cc;//Return the cloudCoin that has been modified. 
    }//end detect

    
     public boolean getTickets( int[] triad, String[] ans, int nn, int sn, int denomination ){
        // String echo1 = raidaArray[0].echo());
        //create a callable for each method
        Callable<Void> callable0 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{ raidaArray[triad[0]].get_ticket(  nn, sn, ans[0], denomination ); return null;}}; 
        Callable<Void> callable1 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{raidaArray[triad[1]].get_ticket( nn, sn, ans[1],denomination ); return null;}};
        Callable<Void> callable2 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {raidaArray[triad[2]].get_ticket( nn, sn, ans[2], denomination ); return null;}};
        //add to a list
        List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
        taskList.add(callable0);
        taskList.add(callable1);
        taskList.add(callable2);
        try{//start the threads
            List<Future<Void>> futureList = executor3.invokeAll(taskList);
            for(Future<Void> voidFuture : futureList){
                try{ //check the status of each future.  get will block until the task
                    //completes or the time expires
                    voidFuture.get(100, TimeUnit.MILLISECONDS);
                }catch (ExecutionException e){
                    System.out.println("Error executing task " + e.getMessage());
                }catch (TimeoutException e){
                    System.out.println("Timed out executing task" + e.getMessage());
                }
            }
        }catch (InterruptedException ie){
            //do something if you care about interruption;
        }
        //Check that all ticket status are good
        if ( raidaArray[triad[0]].lastTicketStatus.equalsIgnoreCase("ticket") && raidaArray[triad[1]].lastTicketStatus.equalsIgnoreCase("ticket") && raidaArray[triad[2]].lastTicketStatus.equalsIgnoreCase("ticket") ){
            return true;
        }else{
            return false;
        }
    }//end get Ticket

    

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
                        hasTickets = getTickets( fixer.currentTriad, fixer.currentAns, brokeCoin.nn, brokeCoin.sn, brokeCoin.getDenomination() ); 
                        if( hasTickets ){
                            fix_result = raidaArray[guid_id].fix( fixer.currentTriad, raidaArray[fixer.currentTriad[0]].lastTicket, raidaArray[fixer.currentTriad[1]].lastTicket, raidaArray[fixer.currentTriad[2]].lastTicket, brokeCoin.ans[guid_id]);
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

        
    public void showRaidaStatus(){
        for(int i =0; i<25; i++){
            System.out.println("Raida "+ i + " status:" +  raidaArray[i].status + ". " + raidaArray[i].ms + " ms "  );
        }//end for
    }//end show raida status
 
}//End RAIDA
