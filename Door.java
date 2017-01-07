 /**
 * CloudCoin Crafter
 * 
 * A "Door" represents an exit in state and an entrance to another state
 */
public class Door 
{
    //Fields
    public String name;
    public State stateOnOtherSideOfDoor;

    //Constructors
    /**
     * Creates a new door or portal.
     * @param name is the name of the door.
     * @param isLocked true if the door is locked and false if the door is unlocked. 
     */
    public Door(String name,  State roomOnOtherSideOfDoor  ){
        this.name = name;
        this.stateOnOtherSideOfDoor = roomOnOtherSideOfDoor; //Door starts not connected to any rooms
        
    }//end constructor

    //Methods
    //None
}//end class Door
