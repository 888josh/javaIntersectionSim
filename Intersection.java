package hw4;

import hw4.TwoWayRoad.LightValue;

public class Intersection {

    private int lightIndex;
    private int countdownTimer;
    private TwoWayRoad[] roads;


    /**
     * Constructor which initializes the roads array.
     * @param initRoads Array of roads to be used by this intersection.
     */
    public Intersection(TwoWayRoad[] initRoads)throws IllegalArgumentException{
        boolean nullIndex =  false;
        for(int i = 0; i<initRoads.length; i++){
            if(initRoads[i] == null){
                nullIndex = true;
            }
        }

        if(initRoads == null || nullIndex || initRoads.length > 4){//initroads.length??
            throw new IllegalArgumentException();
        }
        roads = initRoads;
        lightIndex = 0;
        countdownTimer = roads[lightIndex].getGreenTime();


    }
    /**
     * Performs a single iteration through the intersection. This method should apply all the logic defined in this specification related to the passing of cars through the intersection and switching the selected road (Note: LightValue changes for a particular road should be handled within the TwoWayRoad class itself and not within this method). Please refer to the Simulation Procedure section above for instructions on how to apply this procedure.
     * @return An array of Vehicles which have passed though the intersection during this time step.
     */
    public Vehicle[] timeStep(){
        //switching the countdown timer and selected road using conditional, timer is controlled from inside this method
        //switch light when timer<=leftsignalgreentime or when the lane is empty
        //conditional 

        Vehicle[] dequeuedV = null;
        boolean advance = roads[lightIndex].advance;

        if(advance==true && countdownTimer>roads[lightIndex].getleftSignalGreenTime()){
            roads[lightIndex].setLightValue(LightValue.RED);
            roads[lightIndex].advance = false;
            lightIndex++;
            if(lightIndex>=roads.length){
                lightIndex=0;
            }
        }
        if(countdownTimer == 1){
            dequeuedV = roads[lightIndex].proceed(countdownTimer);
            
            
            if(lightIndex < roads.length-1){
                lightIndex++;
                roads[lightIndex].advance = false;
                countdownTimer = roads[lightIndex].getGreenTime();
            }else{
                lightIndex = 0;
                countdownTimer = roads[lightIndex].getGreenTime();
            }
        }else{
            if(countdownTimer!=0){
                roads[lightIndex].setLightValue(LightValue.RED);
                dequeuedV = roads[lightIndex].proceed(countdownTimer);
                advance=false;
                countdownTimer--;
            }
        }
        return dequeuedV;
    }
    /**
     * Enqueues a vehicle onto a lane in the intersection.
     * @param roadIndex Index of the road in roads which contains the lane to enqueue onto.
     * @param wayIndex Index of the direction the vehicle is headed. Can either be TwoWayRoad.FORWARD or TwoWayRoad.BACKWARD
     * @param laneIndex Index of the lane on which the vehicle is to be enqueue. Can either be TwoWayRoad.RIGHT_LANE, TwoWayRoad.MIDDLE_LANE, or TwoWayRoad.LEFT_LANE.
     * @param vehicle The Vehicle to enqueue onto the lane.
     * @throws IllegalArgumentException If vehicle is null. If any of the index parameters above are not within the valid range..
     */
    public void enqueueVehicle(int roadIndex, int wayIndex, int laneIndex, Vehicle vehicle) throws IllegalArgumentException{
        if(vehicle == null || roadIndex < 0 || roadIndex >= roads.length || wayIndex < 0 || wayIndex >= TwoWayRoad.NUM_WAYS || laneIndex < 0 || laneIndex >= TwoWayRoad.NUM_LANES){
            throw new IllegalArgumentException();
        }
        roads[roadIndex].enqueueVehicle(wayIndex, laneIndex, vehicle);
    }

    /**
     * Method to check if the intersection contains vehicles
     * @return Returns whether the intersection is empty
     */


    public boolean isIntersectionEmpty(){
        boolean isEmpty = true;
        for(int i = 0; i<roads.length; i++){
            if(!roads[i].isRoadEmpty()){
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    /**
     * Prints the intersection to the terminal in a neatly formatted manner. See the sample I/O for an example of what this method should display.
     */

    public void display(){//just printing the vehicles/intersection print the light and vehicles from inside of this? - giant string returned by generate method - 
        if(getCurrentLightValue() == LightValue.GREEN){
            System.out.println("\nGreen light for " + roads[lightIndex].getName() + "\n");

        }else if(getCurrentLightValue() == LightValue.LEFT_SIGNAL){
            System.out.println("\nLeft signal for " + roads[lightIndex].getName() + "\n");
        }else if(getCurrentLightValue() == LightValue.RED){
            System.out.println("\nRed light for " + roads[lightIndex].getName() + "\n");
        }
        for(int i = 0; i<roads.length; i++){
            roads[i].printRoad();
        }
    }

    /**
     * 
     * @return The number of roads inside of the intersection
     */

    public int getNumRoads(){
        return roads.length;
    }
    /**
     * 
     * @return The selected lightIndex
     */
    public int getLightIndex(){
        return lightIndex;
    }
    /**
     * 
     * @return The current step in the countdown timer
     */
    public int getCountdownTimer(){
        return countdownTimer;
    }
    /**
     * 
     * @return The current light value of the selected road within the intersection.
     */
    public LightValue getCurrentLightValue(){
        return roads[lightIndex].getLightValue();
    }

    /**
     * 
     * @return Total number of vehicles inside of the current intersection
     */
    public int size(){
        int size = 0;
        for(int i = 0; i<roads.length; i++){
            size+=roads[i].size();
        }
        return size;
    }


    
}
