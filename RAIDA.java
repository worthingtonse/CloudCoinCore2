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
import java.time.*;

/**
 * Redundant Array of Independent Detection Agents
 * This operates all 25 Detection Agents as a group. 
 * 
 * @author Sean H. Worthington
 * @version 1/8/2016
 */
public class RAIDA
{   // instance variables
    /**
     * An array of 25 detection agents that make up the RAIDA.
     */
    public DetectionAgent[] agent;
    /**
     * These are threads so that the detection agents can be contacted all at the same time. 
     */
    private ExecutorService executor;
     /**
     * These are threads so that the detection agents can be contacted all at the same time. 
     */
    private ExecutorService executor3;

    /**
     * RAIDA Constructor
     *
     * @param milliSecondsToTimeOut The number of milliseconds that requests shall be allowed to run before timing out. 
     */
    public RAIDA(int milliSecondsToTimeOut )
    {
        // initialise instance variables
        agent = new DetectionAgent[25];
        for(int i= 0; i< 25; i++){
            agent[i] = new DetectionAgent( i, milliSecondsToTimeOut);
        }//end for each Raida
        executor = Executors.newFixedThreadPool(25);
        executor3 = Executors.newFixedThreadPool(3);
    }

    /**
     * Method detectCoin
     *
     * @param newCoin A CloudCoin that will be detected.
     */
    public CloudCoin detectCoin( CloudCoin cc ){
        // Make an array to capture the results of the detection. 
        //create a callable for each method  
        Callable<Void> callable0 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[0] = agent[0].detect( cc.nn, cc.sn, cc.ans[0], cc.pans[0], cc.getDenomination() ); return null;}};
        Callable<Void> callable1 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[1]  = agent[1].detect(cc.nn, cc.sn, cc.ans[1], cc.pans[1], cc.getDenomination());return null; }};
        Callable<Void> callable2 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[2]  = agent[2].detect(cc.nn, cc.sn, cc.ans[2], cc.pans[2], cc.getDenomination()); return null;}};
        Callable<Void> callable3 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[3]  = agent[3].detect(cc.nn, cc.sn, cc.ans[3], cc.pans[3], cc.getDenomination());return null;}};
        Callable<Void> callable4 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[4]  = agent[4].detect(cc.nn, cc.sn, cc.ans[4], cc.pans[4], cc.getDenomination()); return null;}};
        Callable<Void> callable5 = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[5]  = agent[5].detect(cc.nn, cc.sn, cc.ans[5], cc.pans[5], cc.getDenomination());return null;}};
        Callable<Void> callable6 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[6] = agent[6].detect(cc.nn, cc.sn, cc.ans[6], cc.pans[6], cc.getDenomination());return null;} };
        Callable<Void> callable7 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[7]  = agent[7].detect(cc.nn, cc.sn, cc.ans[7], cc.pans[7], cc.getDenomination());return null;}};
        Callable<Void> callable8 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[8]  = agent[8].detect(cc.nn, cc.sn, cc.ans[8], cc.pans[8], cc.getDenomination());return null;}};
        Callable<Void> callable9 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[9] = agent[9].detect(cc.nn, cc.sn, cc.ans[9], cc.pans[9], cc.getDenomination()); return null; }};
        Callable<Void> callable10 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[10] = agent[10].detect(cc.nn, cc.sn, cc.ans[10], cc.pans[10], cc.getDenomination());return null;} };
        Callable<Void> callable11 = new Callable<Void>() {
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[11] = agent[11].detect(cc.nn, cc.sn, cc.ans[11], cc.pans[11], cc.getDenomination());return null;}};
        Callable<Void> callable12 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[12] = agent[12].detect(cc.nn, cc.sn, cc.ans[12], cc.pans[12], cc.getDenomination());return null;}};
        Callable<Void> callable13 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[13] = agent[13].detect(cc.nn, cc.sn, cc.ans[13], cc.pans[13], cc.getDenomination());return null;}};
        Callable<Void> callable14 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[14] = agent[14].detect(cc.nn, cc.sn, cc.ans[14], cc.pans[14], cc.getDenomination()); return null;}};
        Callable<Void> callable15 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[15] = agent[15].detect(cc.nn, cc.sn, cc.ans[15], cc.pans[15], cc.getDenomination()); return null;}};
        Callable<Void> callable16 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[16] = agent[16].detect(cc.nn, cc.sn, cc.ans[16], cc.pans[16], cc.getDenomination());return null; } };
        Callable<Void> callable17 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[17] = agent[17].detect(cc.nn, cc.sn, cc.ans[17], cc.pans[17], cc.getDenomination()); return null;}};
        Callable<Void> callable18 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[18] = agent[18].detect(cc.nn, cc.sn, cc.ans[18], cc.pans[18], cc.getDenomination()); return null;}};
        Callable<Void> callable19 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[19] = agent[19].detect(cc.nn, cc.sn, cc.ans[19], cc.pans[19], cc.getDenomination()); return null; }};
        Callable<Void> callable20 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[20] = agent[20].detect(cc.nn, cc.sn, cc.ans[20], cc.pans[20], cc.getDenomination());return null; } };
        Callable<Void> callable21 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[21] = agent[21].detect(cc.nn, cc.sn, cc.ans[21], cc.pans[21], cc.getDenomination()); return null; }};
        Callable<Void> callable22 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[22] = agent[22].detect(cc.nn, cc.sn, cc.ans[22], cc.pans[22], cc.getDenomination());   return null; } };
        Callable<Void> callable23 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{
                    cc.pastStatus[23] = agent[23].detect(cc.nn, cc.sn, cc.ans[23], cc.pans[23], cc.getDenomination()); return null;  }  };
        Callable<Void> callable24 = new Callable<Void>(){
                @Override
                public Void call() throws Exception {
                    cc.pastStatus[24] = agent[24].detect(cc.nn, cc.sn, cc.ans[24], cc.pans[24], cc.getDenomination());return null;}};
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
        /*MUTATE THE COIN TO SHOW IT HAD BEEN DETECTED*/
        cc.setAnsToPansIfPassed();
        cc.calculateHP();
        cc.gradeCoin(); //sets the grade and figures out what the file extension should be (bank, fracked, counterfeit, lost
        cc.calcExpirationDate();
        cc.gradeStatus();
        return cc;//Return the cloudCoin that has been modified. 

    }//end detect

    /**
     * Method fixCoin - Changes the ANs on the RAIDA Detection Agents to the CloudCoin's ANs. 
     *
     * @param brokeCoin The coin that is Fractured. 
     */
    public CloudCoin fixCoin( CloudCoin brokeCoin ){
        brokeCoin.setAnsToPans();//Make sure we set the RAIDA to the cc ans and not new pans. 
        //For every guid, check to see if it is fractured
        for (int guid_id = 0; guid_id < 25; guid_id++  ){ //  System.out.println("Inspecting RAIDA guid " + guid_id );
            
            if( brokeCoin.pastStatus[guid_id].equalsIgnoreCase("fail")){//This guid has failed, get tickets 
                 //System.out.println("RAIDA" +guid_id +" failed." );
                FixitHelper fixer = new FixitHelper( guid_id ); 
                int corner = 1;
                /*GET THE ANS Needed to get the TICKETS*/
                String[] trustedServerAns = new String[]{brokeCoin.ans[fixer.currentTriad[0]],brokeCoin.ans[fixer.currentTriad[1]],brokeCoin.ans[fixer.currentTriad[2]]};
                while( ! fixer.finnished ){
                     Instant before = Instant.now();

                      String fix_result = "";
                        String[] tickets = getTickets( fixer.currentTriad, trustedServerAns, brokeCoin.nn, brokeCoin.sn, brokeCoin.getDenomination() ); 
                        
                        //See if there are errors in the tickets                  
                        if( tickets[0].equals("error") || tickets[2].equals("error") ||  tickets[2].equals("error") ){
                            //No tickets, go to next triad corner 
                           // System.out.println("Get ticket commands failed for guid " + guid_id );
                            corner++;
                            fixer.setCornerToCheck( corner );
                        }else{//Has three good tickets   
                            fix_result = agent[guid_id].fix( fixer.currentTriad, tickets[0], tickets[1], tickets[2], brokeCoin.ans[guid_id]);
                            if( fix_result.equalsIgnoreCase("success")){ //
                              //  System.out.println("GUID fixed for guid " + guid_id );
                                brokeCoin.pastStatus[guid_id] = "pass";
                                fixer.finnished = true;   
                            }else{//command failed
                                corner++;//beed to try another corner
                                fixer.setCornerToCheck( corner );
                            }//end if success fixing
                        }//all the tickets are good. 
                        Instant after = Instant.now(); 
                        System.out.println("It took this many ms to fix the guid: " + Duration.between(before, after).toMillis());
                }//end while still trying to fix
            }//end if guid is broken and needs to be fixed
        }//end for each guid

        brokeCoin.calculateHP();
        brokeCoin.gradeCoin(); //sets the grade and figures out what the file extension should be (bank, fracked, counterfeit, lost
        brokeCoin.calcExpirationDate();
        brokeCoin.gradeStatus();
        return brokeCoin;
    }//end fix coin

    /**
     * Method getTickets
     *<img src="http://cloudcoin.co/img/fixit.png">
     * @param triad Three numbers between 0 and 25 that are three trusted servers that will be contacted
     * @param ans 25 Authenticity Numbers.
     * @param nn Network Number
     * @param sn Serial Number
     * @param denomination The denomination of the CloudCoin 1, 5, 25, 100, 250.
     * @return The return value is three tickets (hopefully)
     */
    public String[] getTickets( int[] triad, String[] ans, int nn, int sn, int denomination ){
        String[] returnTickets = new String[3];
        //create a callable for each method
        Callable<Void> callable0 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{ 
                    returnTickets[0] = agent[triad[0]].get_ticket(  nn, sn, ans[0], denomination ); 
                    return null;}}; 
        Callable<Void> callable1 = new Callable<Void>(){
                @Override
                public Void call() throws Exception{ 
                    returnTickets[1] = agent[triad[1]].get_ticket( nn, sn, ans[1],denomination ); 
                    return null;}};
        Callable<Void> callable2 = new Callable<Void>(){
                @Override
                public Void call() throws Exception { 
                    returnTickets[2] = agent[triad[2]].get_ticket( nn, sn, ans[2], denomination ); 
                    return null;}};
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
                    //System.out.println("Error executing task " + e.getMessage());
                    returnTickets[0] ="error";returnTickets[1] ="error";returnTickets[2] ="error";
                }catch (TimeoutException e){
                    //System.out.println("Timed out executing task" + e.getMessage());
                    returnTickets[0] ="error";returnTickets[1] ="error";returnTickets[2] ="error";
                }
            }
        }catch (InterruptedException ie){
            //do something if you care about interruption;
            returnTickets[0] ="error";returnTickets[1] ="error";returnTickets[2] ="error";
        }
        return returnTickets;
    }//end get Ticket


}//End RAIDA
