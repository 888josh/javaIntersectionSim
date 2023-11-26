package hw4;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class TwoWayRoad {
    public static final int FORWARD_WAY = 0;
    public static final int BACKWARD_WAY = 1;
    public static final int NUM_WAYS = 2;

    public static final int LEFT_LANE = 0;
    public static final int MIDDLE_LANE = 1;
    public static final int RIGHT_LANE = 2;

    public static final int NUM_LANES = 3;

    private static int numRoads = 0;

    private String name;

    private int greenTime;
    
    private int leftSignalGreenTime;

    private VehicleQueue[][] lanes = new VehicleQueue[NUM_WAYS][NUM_LANES];

    private LightValue lightValue;

    public boolean advance = false;

    public enum LightValue{
        GREEN, RED, LEFT_SIGNAL;    
    }

    /**
     * Outputs a display of the road 
     */
    public void printRoad(){
        int counter = 2;
        System.out.println(name + ": ");
        System.out.printf("%34s %23s", "FORWARD", "BACKWARD\n");
        System.out.printf("%34s %45s", "==============================", "==============================\n");
        
        for(int i = 0; i<3; i++){
            if(lanes[0][i].isEmpty()){
                System.out.printf("%34s", "");

            }else{
                printQueueReverse(lanes[0][i]);
            }

            if(i == 0){

                System.out.print("[L] ");
                if(lightValue == LightValue.GREEN){
                    System.out.print("X       ");
                }else if(lightValue == LightValue.LEFT_SIGNAL){
                    System.out.print("      X ");
                }else if(lightValue == LightValue.RED){
                    System.out.print("X     X ");
                }
                System.out.print("[R]");

            }else if(i == 1){
                System.out.print("[M] ");

                if(lightValue == LightValue.GREEN){
                    System.out.print("        ");
                    

                }else if(lightValue == LightValue.LEFT_SIGNAL){
                    System.out.print("X     X ");

                }else if(lightValue == LightValue.RED){
                    System.out.print("X     X ");

                }
                System.out.print("[M]");

            }else if(i == 2){
                System.out.print("[R] ");
                if(lightValue == LightValue.GREEN){
                    System.out.print("      X ");
                }else if(lightValue == LightValue.LEFT_SIGNAL){
                    System.out.print("X       ");
                }else if(lightValue == LightValue.RED){
                    System.out.print("X     X ");
                }

                System.out.print("[L]");
            }

            printQueue(lanes[1][counter]);

            if(counter > 0){
                System.out.printf("%34s %44s\n", "------------------------------", "------------------------------");
            }
            counter--;
        }
        System.out.printf("%34s %45s\n", "==============================", "============================== ");


    }
    /**
     * 
     * @param queue Prints a VehicleQueue in reverse
     */
    public void printQueueReverse(VehicleQueue queue){
        if(queue != null){
            Stack<Vehicle> rev =  new Stack<Vehicle>();
            Stack<Vehicle> rev2 =  new Stack<Vehicle>();
            
            while(!queue.isEmpty()){
                rev.push(queue.dequeue());
            }
            System.out.printf("%28s", "");
            while(!rev.isEmpty()){
                Vehicle temp = rev.pop();
                System.out.printf("[%03d]", temp.getSerialID(), "");
                rev2.push(temp);
            }
            while(!rev2.isEmpty()){
                queue.enqueue(rev2.pop());
            }
        }else{
            System.out.println();
        }
    }
    /**
     * 
     * @param queue Prints a vehicleQueue
     */
    public void printQueue(VehicleQueue queue){
        if(queue!=null){
            VehicleQueue tempQueue = new VehicleQueue();
            while(!queue.isEmpty()){
                Vehicle temp = queue.dequeue();
                System.out.printf("[%03d]", temp.getSerialID());
                tempQueue.enqueue(temp);

            }
            while(!tempQueue.isEmpty()){
                Vehicle temp = tempQueue.dequeue();
                queue.enqueue(temp);
            }
        }else{
            System.out.println();
        }
        System.out.println();
    }
    /**
     * Default Constructor. You should automatically initialize the array and all of its member objects, as well as initializing leftSignalGreenTime to 1.0/NUM_LANES * initGreenTime.
     * @param initName The name of the road.
     * @param initGreenTime The amount of time that the light will be active for this particular road. This is the total of the time the light should display green for cars going forward/turning right, as well as for cars going left.
     * @throws IllegalArgumentException If initGreenTime ≤ 0 or initName=null.
     */
    public TwoWayRoad(String initName, int initGreenTime) throws IllegalArgumentException{

        if(initName == null || initGreenTime <= 0){
            throw new IllegalArgumentException();
        }
        name = initName;
        greenTime = initGreenTime;
        leftSignalGreenTime = (int)(Math.floor(1.0/(NUM_LANES * initGreenTime)));
        if(numRoads==0){
            lightValue = LightValue.GREEN;
        }else{
            lightValue = LightValue.RED;
        }
        numRoads++;

        for(int i = 0; i<NUM_WAYS; i++){
            for(int j = 0; j<NUM_LANES; j++){
                lanes[i][j] = new VehicleQueue();
            }
        }
    }
    /**
     * Executes the passage of time in the simulation. The timerVal represents the current value of a countdown timer counting down total green time steps. The light should be in state GREEN any time the timerval is greater than leftSignalGreenTime. When timerVal is less than or equal to leftSignalGreenTime, the light should change to LEFT_SIGNAL. After the execution of timerVal == 1, or if there are no vehicles left the light should change state to RED.
     * @param timerVal The current timer value, determines the state of the light.
     * @return An array of Vehicles that has been dequeued during this time step.
     * @throws IllegalArgumentException IllegalArgumentException If timerval ≤ 0.
     */
    public Vehicle[] proceed(int timerVal) throws IllegalArgumentException{//not setting original road to red after switching, not catching leftsignal
        if(timerVal < 0){
            throw new IllegalArgumentException();
        }
        Queue<Vehicle> toAdd = new LinkedList<>();

        int count = 0;



            if(timerVal <= leftSignalGreenTime && advance == false || (isLaneEmpty(FORWARD_WAY, RIGHT_LANE) && isLaneEmpty(FORWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, RIGHT_LANE))){
                advance=true;
                lightValue = LightValue.LEFT_SIGNAL;
    
                for(int i = 0; i<NUM_WAYS; i++){
                    for(int j = 0; j<NUM_LANES; j++){
                        if(j == LEFT_LANE && lanes[i][j] != null){
                            Vehicle removed = lanes[i][j].dequeue();
                            toAdd.add(removed);
                            count++;
                        }
                    }
                }
                Vehicle[] dequeued = new Vehicle[count];
    
                for(int i = 0; i<count; i++){
                    Vehicle temp = toAdd.poll();
                    dequeued[i] = temp;
                }
        
                return dequeued;
            }

        if(timerVal > leftSignalGreenTime && advance==true || !(isLaneEmpty(FORWARD_WAY, RIGHT_LANE) && isLaneEmpty(FORWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, RIGHT_LANE))){

            lightValue = LightValue.GREEN;
            
            for(int i = 0; i<NUM_WAYS; i++){
                for(int j = 0; j<NUM_LANES; j++){
                    if((j == MIDDLE_LANE || j == RIGHT_LANE) && lanes[i][j] != null){
                        Vehicle removed = lanes[i][j].dequeue();
                        toAdd.add(removed);
                        count++;
                    }
                }
            }
            Vehicle[] dequeued = new Vehicle[count];

            for(int i = 0; i<count; i++){
                Vehicle temp = toAdd.poll();
                dequeued[i] = temp;
            }
            if((isLaneEmpty(FORWARD_WAY, RIGHT_LANE) && isLaneEmpty(FORWARD_WAY, MIDDLE_LANE) && isLaneEmpty(FORWARD_WAY, LEFT_LANE) && isLaneEmpty(BACKWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, RIGHT_LANE) && isLaneEmpty(BACKWARD_WAY, LEFT_LANE))){
                advance = true;
            }
            return dequeued;
        }


        
        if(timerVal == 0 || (isLaneEmpty(FORWARD_WAY, RIGHT_LANE) && isLaneEmpty(FORWARD_WAY, MIDDLE_LANE) && isLaneEmpty(FORWARD_WAY,LEFT_LANE) && isLaneEmpty(BACKWARD_WAY, MIDDLE_LANE) && isLaneEmpty(BACKWARD_WAY, RIGHT_LANE) && isLaneEmpty(BACKWARD_WAY, LEFT_LANE))){//isroadempty is glitching it, causing red light every step
            lightValue = LightValue.RED;
        }

        Vehicle[] dequeued = new Vehicle[count];

        for(int i = 0; i<count; i++){
            Vehicle temp = toAdd.poll();
            dequeued[i] = temp;
        }

        return dequeued;

    }


    /**
     * Enqueues a vehicle into a the specified lane.
     * @param wayIndex The direction the car is going in.
     * @param laneIndex The lane the car arrives in.
     * @param v The vehicle to enqueue; must not be null.
     * @throws IllegalArgumentException If wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2 or vehicle==null
     */

    public void enqueueVehicle(int wayIndex, int laneIndex, Vehicle v) throws IllegalArgumentException{

        if(wayIndex > 1 || laneIndex < 0 || wayIndex < 0 || laneIndex > 2 || v == null){
            throw new IllegalArgumentException();
        }

        lanes[wayIndex][laneIndex].enqueue(v);

    }
    /**
     * Checks if a specified lane is empty.
     * @param wayIndex The direction of the lane.
     * @param laneIndex The index of the lane to check.
     * @return true if the lane is empty, else false.
     * @throws IllegalArgumentException If wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2.
     */

    public boolean isLaneEmpty(int wayIndex, int laneIndex) throws IllegalArgumentException{
        if(wayIndex > 1 || laneIndex < 0 || wayIndex < 0 || laneIndex > 2){
            throw new IllegalArgumentException();
        }
        boolean laneEmpty = true;
        if(lanes[wayIndex][laneIndex].isEmpty() == false){
            laneEmpty = false;
        }
        return laneEmpty;
    }
    /**
     * 
     * @return Boolean specifying whether the road is empty or not
     */
    public boolean isRoadEmpty(){
        boolean isRoadEmpty = true;
        for(int i = 0; i<NUM_WAYS; i++){
            for(int j = 0; j<NUM_LANES; j++){
                if(!isLaneEmpty(i, j)){
                    isRoadEmpty = false;
                }
            }
        }
        return isRoadEmpty;
    }
    /**
     * 
     * @return The greentime of the current road
     */
    public int getGreenTime(){
        return greenTime;
    }
    /**
     * 
     * @return The lightvalue of the current road
     */
    public LightValue getLightValue(){
        return lightValue;
    }
    /**
     * 
     * @return The leftSignalGreenTime of this road.
     */
    public int getleftSignalGreenTime(){
        return this.leftSignalGreenTime;
    }

    /**
     * 
     * @return THe number of vehicles in the road
     */

    public int size(){
        int size = 0;
        for(int i = 0; i<NUM_WAYS; i++){
            for(int j = 0; j<NUM_LANES; j++){
                size+=lanes[i][j].size();
            }
        }
        return size;
    }
    /**
     * sets the lightvalue of the road
     * @param v Specified lightvalue
     */
    public void setLightValue(LightValue v){
        lightValue = v;
    }
    /**
     * 
     * @return THe name of the road
     */
    public String getName(){
        return this.name;
    }
    /**
     * Sets boolean advance 
     * @param b Boolean for advance variable
     */
    public void setAdvance(boolean b){
        this.advance = b;
    }
    
}
