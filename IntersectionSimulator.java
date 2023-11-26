package hw4;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;


public class IntersectionSimulator {
    static Scanner input = new Scanner(System.in);

    public static int simulationTime;
    public static double arrivalProbability;
    public static int numRoads;
    public static String[] names;
    public static int[] greenTimes;
    public static TwoWayRoad[] roads;
    public static Intersection intersection;
    public static BooleanSourceHW4 boolSource;
    public static int waitingCars = 0;

    /**
     * Prompts the user to input values for the intersection Simulation
     */
    public static void userPrompt(){
        try{
            System.out.println("Input the simulation time: ");
            simulationTime = nextIntLine();
            System.out.println("Input the arrival probability");
            arrivalProbability = nextDoubleLine();

            boolSource = new BooleanSourceHW4(arrivalProbability);

            System.out.println("Input the number of streets: ");
            numRoads = nextIntLine();
            names = new String[numRoads];
            greenTimes = new int[numRoads];
            roads = new TwoWayRoad[numRoads];

            for(int i = 0; i<numRoads; i++){
                System.out.println("Input Street " + (i+1) + " name: ");
                names[i] = input.nextLine();
                for(int j = 0; j<i; j++){
                    while(names[j].equals(names[i])){
                        System.out.println("Duplicate Detected.");
                        System.out.println("Input Street " + (i+1) + " name:");
                        names[i] = input.nextLine();
                    }
                }
            }

            for(int k = 0; k<numRoads; k++){
                System.out.println("Input max green time for " + names[k]);
                greenTimes[k] = nextIntLine();
            }

            for(int j = 0; j<numRoads; j++){
                roads[j] = new TwoWayRoad(names[j], greenTimes[j]);
            }

            intersection = new Intersection(roads);

            System.out.println("Starting Simulation...");
        }catch(InputMismatchException i){
            System.out.println("Invalid input.");
        }catch(IllegalArgumentException i){
            System.out.println("Invalid input.");
        }

    }

    public static double nextDoubleLine(){
        double n = input.nextDouble();
        if(input.hasNextLine()){
            input.nextLine();
        }
        return n;
    }

    public static int nextIntLine(){
        int n = input.nextInt();
        if(input.hasNextLine()){
            input.nextLine();
        }
        return n;
    }
    /**
     * Generates vehicles for each timeStep of the simulation
     */
    public static void generateVehicles(){
        ArrayList<Vehicle> arrived = new ArrayList<Vehicle>();
        System.out.println();
        for(int k = 0; k<numRoads; k++){
            for(int i = 0; i<TwoWayRoad.NUM_WAYS; i++){
                for(int j = 0; j<TwoWayRoad.NUM_LANES; j++){
                    if(boolSource.occursHW4()){
                        Vehicle temp = new Vehicle(simulationTime);
                        arrived.add(temp);
                        roads[k].enqueueVehicle(i, j, temp);

                        if(i == TwoWayRoad.FORWARD_WAY){

                            if(j == TwoWayRoad.MIDDLE_LANE){

                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going FORWARD in the MIDDLE lane.\n",temp.getSerialID());
                            }else if(j == TwoWayRoad.RIGHT_LANE){
                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going FORWARD in the RIGHT lane.\n",temp.getSerialID());

                            }else{
                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going FORWARD in the LEFT lane.\n",temp.getSerialID());
                            }
                        }else if(i == TwoWayRoad.BACKWARD_WAY){
                            if(j == TwoWayRoad.MIDDLE_LANE){
                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going BACKWARD in the MIDDLE lane.\n",temp.getSerialID());

                            }else if(j == TwoWayRoad.RIGHT_LANE){
                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going BACKWARD in the RIGHT lane.\n",temp.getSerialID());

                            }else{
                                System.out.printf("Vehicle[%03d] entered " + names[i] + ", going BACKWARD in the LEFT lane.\n",temp.getSerialID());
                                
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("\nWelcome to IntersectionSimulator 2023\n");

        userPrompt();

        simulate(simulationTime,arrivalProbability,names,greenTimes);

        System.out.println("End Simulation.");

    }
    /**
     * Conducts simulation procedure with parameters provided by a user.
     * @param initSimulationTime time duration for which cars arrive
     * @param arrivalProb probability that cars arrive
     * @param roadNames array containing names of each road
     * @param maxgreenTimes array with equivalent greentimes for road names in roadNames Array
     */
    public static void simulate(int initSimulationTime, double arrivalProb, String[] roadNames, int[] maxgreenTimes){

        int timeStep = 1;
        Double TotalWaitTime = 0.0;
        int longestWaitTime = 0;
        int numPassed = 0;

        while(simulationTime > 0){
            System.out.println("\n################################################################################\n");

            System.out.println("Time Step: " + timeStep);

            System.out.println("Timer = " + intersection.getCountdownTimer());

            System.out.println("ARRIVING CARS: ");

            generateVehicles();

            System.out.println("\nPASSING CARS: \n");

            Vehicle[] deQueued = intersection.timeStep();

            for(int i = 0; i<deQueued.length; i++){
                if(deQueued[i]!=null){
                    System.out.printf("Car[%03d] passes through. ", deQueued[i].getSerialID());
                    int waitTime = deQueued[i].getTimeArrived() - simulationTime;
                    TotalWaitTime+=waitTime;
                    System.out.println("Wait time of " + waitTime);
                    numPassed++;
                }

            }
            intersection.display();
            simulationTime--;
            timeStep++;

            System.out.println("STATISTICS:");
            System.out.printf("%5s\n", "Cars currently waiting: " + intersection.size() + " cars");
            System.out.printf("%5s\n", "Total cars passed: " + numPassed + " cars");
            System.out.printf("%5s\n", "Total Wait time: " + TotalWaitTime + " turns");
            System.out.printf("%5s %.2f", "Average wait time: " ,(TotalWaitTime/numPassed));
            System.out.println(" turns");
        }

        while(!intersection.isIntersectionEmpty()){

            System.out.println("\n################################################################################\n");

            System.out.println("Time Step: " + timeStep);

            System.out.println("Timer = " + intersection.getCountdownTimer());
            
            System.out.println("Cars no longer arriving.");
            System.out.println("ARRIVING CARS: ");

            System.out.println("PASSING CARS: ");
            Vehicle[] deQueued = intersection.timeStep();

            for(int i = 0; i<deQueued.length; i++){
                if(deQueued[i]!=null){
                    System.out.printf("Car[%03d] passes through. ", deQueued[i].getSerialID());
                    int waitTime = deQueued[i].getTimeArrived() - simulationTime;
                    if(waitTime >= longestWaitTime){
                        longestWaitTime = waitTime;
                    }
                    TotalWaitTime+=waitTime;
                    System.out.println("Wait time of " + waitTime);
                    numPassed++;
                }

            }
            intersection.display();
            timeStep++;

            System.out.println("STATISTICS:");
            System.out.printf("%5s\n", "Cars currently waiting: " + intersection.size() + " cars");
            System.out.printf("%5s\n", "Total cars passed: " + numPassed + " cars");
            System.out.printf("%5s\n", "Total Wait time: " + TotalWaitTime + " turns ");
            System.out.printf("%5s %.2f", "Average wait time: ", (TotalWaitTime/numPassed));
            System.out.println(" turns");
        }

        System.out.println("################################################################################\n################################################################################\n################################################################################\n");
        System.out.println("SIMULATION SUMMARY: ");
        System.out.println("Total Time: " + timeStep + " steps");
        System.out.println("Total Vehicles: " + numPassed + " vehicles");
        System.out.println("Longest Wait Time: " + longestWaitTime + " turns");
        System.out.println("Total Wait Time: " + TotalWaitTime + " turns ");
        System.out.print("Average Wait Time: ");
        System.out.printf("%.2f", TotalWaitTime/numPassed);
        System.out.println(" turns");

        
    }
    
}
