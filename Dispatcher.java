/*
    Assignment 1
    Author: Shaan Arora C3236359
    Dispatcher.java
        A Dispatcher object has the ability to run the different types of scheduling algorithms and when a process should
            execute, wait or be interrupted based on that algorithm
 */

import java.util.ArrayList;

public class Dispatcher
{
    //Private member variables
    //The time the dispatcher takes to run (context switch) which is given in the input file
    private int runTime;
    //The running queue for the dispatcher
    private ArrayList<Process> running;
    //The ready/waiting queue for the dispatcher
    private ArrayList<Process> waiting;
    //Storage for the processes that have completed their execution time
    private ArrayList<Process> complete;
    //Stores all the processes that have been read in from the input file
    private ArrayList<Process> input;
    //the time slice for the round robin scheduling algorithm
    private double timeSlice;
    //the total time a scheduling algorithm takes
    private int totalTime;
    //What mode the dispatcher is in, false for none preemptive and true for preemptive
    boolean preemptive;

    //Default constructor
    public Dispatcher()
    {
        this.runTime = 0;
        this.running = new ArrayList<>();
        this.waiting = new ArrayList<>();
        this.complete = new ArrayList<>();
        this.input = new ArrayList<>();
        this.timeSlice = 0.0;
        this.totalTime = 0;
        //Default mode for dispatcher is non preemptive
        this.preemptive = false;
    }

    //First Come First Serve Algorithm
    //Preconditions: current Dispatcher object has been declared and initialized
    //Postconditions: Simulates the FCFS algorithm and calculates the necessary time values for each Process object
    public void FCFS()
    {
        //load processes from input into waiting queue
        this.moveInput();
        //run the dispatcher once
        this.runDispatcher();
        //for every process in the dispatcher
        for(int i = 0; i < this.getInput().size(); i++)
        {
            this.moveWaiting();
            this.runProcess();
            this.runDispatcher();
        }
    }

    //Preconditions: Dispatcher object has been declared & initialized and FCFS() has been called.
    //                  if another algorithm was called then call resetDispatcher() then FCFS()
    //Postconditions: formatted output for the FCFS algorithm is displayed
    public void outputFCS()
    {
        System.out.println("FCFS:");
        for(Process p : this.getComplete())
        {
            System.out.println("T" + p.getStartTime() + ":  " + p.getID() + "(" + p.getPriority() + ")");
        }
        System.out.println("\nProcess Turnaround Time Waiting Time");
        for(Process p : this.getComplete())
        {
            //length of the turnaround and waiting time integer
            String t = Integer.toString(p.getTat());
            String w = Integer.toString(p.getWaitTime());
            //number of whitespaces needed to add for turnaround and waiting time
            int wsT, wsW = 16;
            if(t.length() == 2)
            {
                wsT = 8;
            }
            else {wsT = 7;}
            if(w.length() == 1){wsW = 15;}
            String output = String.format("%s%" + wsT + "d%" + wsW + "d", p.getID(), p.getTat(), p.getWaitTime());
            System.out.println(output);
        }
    }

    //Shortest Process Next Algorithm
    //Preconditions: current Dispatcher object has been declared and initialized, if another algorithm has run previously then resetDispatcher() needs to be called
    //Postconditions: Simulates the SPN algorithm and calculates the necessary time values for each Process object
    public void SPN()
    {
        //Move all Process objects in input to the waiting queue
        this.moveInput();
        //Sort waiting based on how long each process takes.
        //The Process object with the smallest execution time will be at the start and largest at the end
        this.getWaiting().sort(new ProcessSorter.serviceTimeSort());
        this.runDispatcher();
        for(int i = 0; i < this.getInput().size(); i++)
        {
            this.moveWaiting();
            this.runProcess();
            this.runDispatcher();
        }
    }

    public void outputSPN()
    {
        System.out.println("\nSPN: ");
        for(Process p : this.getComplete())
        {
            System.out.println("T" + p.getStartTime() + ":  " + p.getID() + "(" + p.getPriority() + ")");
        }
        System.out.println("\nProcess Turnaround Time Waiting Time");
        //Since the ArrayList this.complete is populated by the order in which the processes are ran we need to re sort
        this.getComplete().sort(new ProcessSorter.IDSort());
        for(Process p : this.getComplete())
        {
            //Get the length of the turnaround time integer
            String t = Integer.toString(p.getTat());
            //Get the length of the waiting time integer
            String w = Integer.toString(p.getWaitTime());
            //how many whitespaces we need to add for the turnaround time and waiting time
            int wsT, wsW = 16;
            //if the turnaround is 2 digits
            if(t.length() == 2)
            {
                wsW = 15;
                wsT = 8;
            }
            else {wsT = 7;}
            if(w.length() == 2){wsW = 16;}
            String output = String.format("%s%" + wsT + "d%" + wsW + "d", p.getID(), p.getTat(), p.getWaitTime());
            System.out.println(output);
        }
    }

    //Preemptive Priority Algorithm
    public void PP()
    {
        this.setPreemptive(true);
    }

    //Priority Round Robin Algorithm
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: runs the PRR scheduling algorithm and calculates specific time values for each process
    public void PRR()
    {
        //split Process objects into HPC and LPC
    }

    //Moves a Process objects from waiting to running
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: moves the Process object in waiting at index 0 into running
    public void moveWaiting() {this.running.add(this.waiting.remove(0));}

    //Moves a Process object from running to waiting
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: moves the Process object in running at index 0 into waiting
    public void moveRunning(){this.waiting.add(this.running.remove(0));}

    //Moves all Process objects from the input array into the waiting array
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: all Process objects from the input array are moved into the waiting array
    //should pass a reference to each Process object in input to waiting
    public void moveInput() {this.waiting.addAll(this.input);}

    //Run the dispatcher for however long its run time is set
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: runs the dispatcher for its specified run time and then updates the wait for each process in the waiting queue
    public void runDispatcher()
    {
        this.totalTime += this.getRunTime();
        //if there are processes waiting then update their wait time
        for(Process p : this.waiting)
        {
            p.setWaitTime(this.totalTime);
        }
    }

    //Runs one process in the running queue
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: updates the totalTime of the Dispatcher object to be the execution time of the Process, updates that Process's complete time and then
    //                  remove that Process object from running because it is now complete
    public void runProcess()
    {
        //Set the time the process started executing
        this.running.get(0).setStartTime(this.totalTime);
        //Run the process for its service time
        this.totalTime += this.running.get(0).getServiceTime();
        //Update the total time it took
        this.running.get(0).setTat(this.totalTime);
        //Remove that Process object from running as its now complete
        this.complete.add(this.running.remove(0));
    }

    //Reset the dispatcher in between each scheduling algorithm
    //Preconditions: the current Dispatcher object has been declared and initialized and at least one scheduling algorithm has been called
    //Postconditions: reset the main primary private member variables of the dispatcher
    public void resetDispatcher()
    {
        this.setRunning(new ArrayList<>());
        this.setWaiting(new ArrayList<>());
        this.setComplete(new ArrayList<>());
        this.totalTime = 0;
    }

    //Setters

    //Preconditions: input file has been read
    //Postconditions: The run time for the current Dispatcher object is assigned the value of runTime
    public void setRunTime(int runTime) {this.runTime = runTime;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of run to running
    public void setRunning(ArrayList<Process> run) {this.running = run;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of wait to waiting
    public void setWaiting(ArrayList<Process> wait) {this.waiting = wait;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of comp to complete
    public void setComplete(ArrayList<Process> comp) {this.complete = comp;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of input to the private member variable input
    public void setInput(ArrayList<Process> input) {this.input = input;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of timeSlice to the private member variable timeSlice
    public void setTimeSlice(double timeSlice) {this.timeSlice = timeSlice;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Updates the mode of the dispatcher, true if in preemptive mode else in non preemptive mode
    public void setPreemptive(boolean mode) {this.preemptive = mode;}

    //Getters

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: returns the run time of the Dispatcher object
    public int getRunTime() {return this.runTime;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the Process objects that are currently in running as an ArrayList
    public ArrayList<Process> getRunning() {return this.running;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the Process objects that are currently in waiting as an ArrayList
    public ArrayList<Process> getWaiting() {return this.waiting;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the Process objects that are currently in complete as an ArrayList
    public ArrayList<Process> getComplete() {return this.complete;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the Process objects that are currently in input as an ArrayList
    public ArrayList<Process> getInput() {return this.input;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the time slice/quantum of the dispatcher
    public double getTimeSlice() {return this.timeSlice;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: returns true if the Dispatcher object is in preemptive mode or false if its in non preemptive mode
    public boolean isPreemptive() {return this.preemptive;}
}
