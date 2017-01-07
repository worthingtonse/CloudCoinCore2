import java.util.ArrayList;
import java.util.HashMap;

/** 
 *This is used to support the console app and is not needed in GUI
 * 
 * @Author Sean H. Worthington
 * @version 1/7/2016
 */
public class State{

    //fields
    public String description;
    public String longDescription;
    public ArrayList<Door> doors;
    private ArrayList<String> commandList = new ArrayList<String>();

    //constructors
    public State(String description)
    {
        this.description = description;
        doors = new ArrayList<Door>();
    }

    //methods
    /**
     * Gives the room a door that will connect to other room. 
     * @param direction Describes the door or the direction it goes
     * @param opensTo The room the door connects to.
     * @param isLocked true if unlooked false if locked. 
     */
    public void setDoor(String direction, State opensTo )
    {
        Door door = new Door(direction, opensTo);
        getDoors().add(door);
    }

    public void setCommand(String command )
    {
         commandList.add( command ) ;
    }
    
    public void setLongDescription(String d )
    {
         this.longDescription = d ;
    }
    /**
     * @return The short description of the room (the one that was defined in
     *         the constructor).
     */
    public String getShortDescription() {
        return description;
    }

    /**
     * Return a description of the room in the form: You are in the kitchen.
     * Exits: north west
     * 
     * @return A long description of this room
     */
    public String getLongDescription() {
        return longDescription;//+ getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "Exits:";
        for(Door door : getDoors()) {
            returnString += " " + door.name;
            returnString += ", ";
        }
        return returnString.substring(0, returnString.lastIndexOf(","));//gets rid of last comma
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction
     *            The exit's direction.
     * @return The room in the given direction.
     */
    public State getExit(String direction) {

        State r = this;
        for(Door d: doors )
        {
            if( d.name.equalsIgnoreCase( direction ) )
            {
                r =  d.stateOnOtherSideOfDoor;
            }//end if

        }//end for
        return r;
    }//end getExit

    /**
     * Get the Door of the room
     */
    public Door getDoor(String doorName){
        for (Door door : getDoors()){
            if (door.name.equals(doorName)){
                return door;
            }
        }
        return null;
    }


    /**
     * @return the doors
     */
    public ArrayList<Door> getDoors() {
        return doors;
    }

    /**
     * @return the available commands based on the contents of the room. 
     */
    public String[] getCommands() {
        String[] returnCommands = {};
	    returnCommands= commandList.toArray(returnCommands);
        return returnCommands;
    }

}//end room