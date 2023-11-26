package hw4;

public class Vehicle{

    private int serialId;
    private static int serialCounter = 0;
    private int timeArrived;

    /**
     * Default Constructor. You should automatically increment the serialCounter, and set the serialId to its new value.
     * @param initTimeArrived time the vehicle arrived at the intersection.
     */
    public Vehicle(int initTimeArrived) throws IllegalArgumentException{
        if(initTimeArrived <= 0){
            throw new IllegalArgumentException();
        }
        timeArrived = initTimeArrived;
        serialCounter++;
        serialId = serialCounter;
    }
    
    /**
     * Getter for serialID
     * @return Returns a vehicle's specific serial ID
     */

    public int getSerialID(){
        return serialId;
    }

    /**
     * Getter for timeArrived
     * @return Returns a vehicle's arrival time
     */
    public int getTimeArrived(){
        return timeArrived;
    }

}
