package hw4;
import java.util.LinkedList;
import java.util.Queue;


public class VehicleQueue{

    Queue<Vehicle> lane = new LinkedList<Vehicle>();
    /**
     * Adds a vehicle to the end of the Queue
     * @param v The vehicle to be added
     */
    public void enqueue(Vehicle v){
        lane.add(v);
    }
    /**
     * Removes a vehicle from the Queue
     * @return The vehicle removed from the Queue
     */
    public Vehicle dequeue(){
        return lane.poll();
    }
    /**
     * Returns the size of the vehicleQueue
     */
    public int size(){
        return lane.size();
    }
    /**
     * Returns whether the vehicleQueue is empty or not
     */
    public boolean isEmpty(){
        // return lane.isEmpty();
        Queue<Vehicle> holdingBay = new LinkedList<Vehicle>();
        boolean isEmpty = true;

        for(int i = 0; i<lane.size(); i++){
            Vehicle temp = lane.poll();
            if(temp instanceof Vehicle){
                isEmpty = false;
            }
            holdingBay.add(temp);
        }
        while(!holdingBay.isEmpty()){
            Vehicle temp = holdingBay.poll();
            lane.add(temp);
        }
        return isEmpty;
    }

    

    
}
