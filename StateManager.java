import java.util.ArrayList;
import java.util.HashMap;
/**
 * This is used to support the console app and is not needed in GUI
 * Creates the state with commands, links to other states and datastores. 
 * 
 * @author Sean Worthington
 * @version 1/7/2016
 */
public class StateManager
{
    // instance variables - replace the example below with your own
    private static ArrayList<State> states; 
    public State currentState;

    /**
     * Constructor for objects of class StateManager. 
     * Not much goin on here. 
     */
    public StateManager( )
    {
        /* MAKE STATES  */
        states = new ArrayList<State>();

        State start_mode = new State("Start mode");

        currentState = start_mode;

        /* START MODE */  
        start_mode.setCommand("import");
        start_mode.setCommand("show coins");
        start_mode.setCommand("export");
        start_mode.setCommand("export all");
        start_mode.setCommand("fix fracked");
        start_mode.setCommand("quit");

    }//End constructor

    /**
     * @return the States
     */
    public static ArrayList<State> getStates() {
        return states;
    }//end get rooms

    public State getCurrentState()
    {
        return currentState;
    }//end get current room
}//end class

