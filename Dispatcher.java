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
    //Stores all the processes that have been read in from the input file
    private ArrayList<Process> input;
    //the time slice for the round robin scheduling algorithm
    private double timeSlice;
    //the total time a scheduling algorithm takes
    private double totalTime;
    //What mode the dispatcher is in, false for none preemptive and true for preemptive
    boolean preemptive;

    //Default constructor
    public Dispatcher()
    {
        this.runTime = 0;
        this.running = new ArrayList<>();
        this.waiting = new ArrayList<>();
        this.input = new ArrayList<>();
        this.timeSlice = 0.0;
        this.totalTime = 0.0;
        //Default mode for dispatcher is non preemptive
        this.preemptive = false;
    }

    //Parameter constructor for loading in the run time and processes from the input file
    public Dispatcher(int tick, ArrayList<Process> input)
    {
        this.runTime = tick;
        this.input = input;
        this.running = new ArrayList<>();
        this.waiting = new ArrayList<>();
        this.timeSlice = 0.0;
        this.totalTime = 0;
        //Default mode for dispatcher is non preemptive
        this.preemptive = false;
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
    //Postconditions: Returns the Process objects that are currently in input as an ArrayList
    public ArrayList<Process> getInput() {return this.input;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the time slice/quantum of the dispatcher
    public double getTimeSlice() {return this.timeSlice;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: returns true if the Dispatcher object is in preemptive mode or false if its in non preemptive mode
    public boolean isPreemptive() {return this.preemptive;}

    //First Come First Serve Algorithm
    //TODO wrap in a for loop or for each loop
    public void FCFS()
    {
        //load processes from input into waiting queue
        this.moveInput();
        //run dispatcher once
        this.runDispatcher();
        //move process 1 into the running queue
        this.moveWaiting(1);
        //run the process for its service time
        this.runProcess();
        //dispatcher time once
        this.runDispatcher();
        this.moveWaiting(1);
    }

    //Shortest Process Next Algorithm
    public void SPN(){}

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
    //Preconditions: the current Dispatcher object has been declared and initialized and index != 0
    //Postconditions: moves the Process object in waiting at the index passed in, into running
    private void moveWaiting(int index)
    {
        //Remove the Process object from waiting and store it into running
        this.running.add(this.waiting.remove(index));
    }

    //Moves a Process object from running to waiting
    //Preconditions: the current Dispatcher object has been declared and initialized and index != 0
    //Postconditions: moves the Process object in running at the index passed in, into waiting
    private void moveRunning(int index)
    {
        //Removes the Process object from running and store it into waiting
        this.waiting.add(this.running.remove(index));
    }

    //Moves all Process objects from the input array into the waiting array
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: all Process objects from the input array are moved into the waiting array
    //should pass a reference to each Process object in input to waiting
    private void moveInput() {this.waiting.addAll(this.input);}

    //Run the dispatcher for however long its run time is set
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: runs the dispatcher for its specified run time and then updates the wait for each process in the waiting queue
    private void runDispatcher()
    {
        this.totalTime += this.getRunTime();
        //if there are processes waiting then update their wait time
        if(this.waiting.size() != 0)
        {
            for(Process p : this.waiting)
            {
                p.setWaitTime(this.totalTime);
            }
        }
    }

    //Runs one process in the running queue
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: updates the totalTime of the Dispatcher object to be the execution time of the Process, updates that Process's complete time and then
    //                  remove that Process object from running because it is now complete
    private void runProcess()
    {
        //Run the process for its service time
        this.totalTime += this.running.get(0).getServiceTime();
        //Update the total time it took
        this.running.get(0).setCompleteTime(this.totalTime);
        //Remove that Process object from running as its now complete
        this.running.remove(0);
    }

    //Reset the dispatcher in between each scheduling algorithm
    //Preconditions: the current Dispatcher object has been declared and initialized and at least one scheduling algorithm has been called
    //Postconditions: reset the main primary private member variables
    private void resetDispatcher()
    {
        this.setRunning(new ArrayList<>());
        this.setWaiting(new ArrayList<>());
        this.totalTime = 0;
    }
}
