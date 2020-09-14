/*
    Assignment 1
    Author: Shaan Arora C3236359
    Dispatcher.java
        A Dispatcher object has the ability to run the different types of scheduling algorithms and when a process should
            execute, wait or be interrupted based on that algorithm
 */

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Dispatcher
{
    //Private member variables
    //The time the dispatcher takes to run (context switch) which is given in the input file
    private int runTime;
    //Stores all the processes that have been read in from the input file
    private final  ArrayList<Process> input;
    //The ready/waiting queue for the dispatcher
    private ArrayList<Process> waiting;
    //Storage for the processes that have completed their execution time
    private ArrayList<Process> complete;
    //The current process running in the dispatcher
    private Process current;
    //the total time a scheduling algorithm takes
    private int totalTime;

    //Default constructor
    public Dispatcher()
    {
        this.runTime = 0;
        this.waiting = new ArrayList<>();
        this.complete = new ArrayList<>();
        this.input = new ArrayList<>();
        this.current = new Process();
        this.totalTime = 0;
    }

    //First Come First Serve Algorithm
    //Preconditions: Current Dispatcher object has been declared & initialized and resetDispatcher() if another algorithm has been
    //                  called and displayed
    //Postconditions: Simulates the FCFS scheduling algorithm thus calculates specific time values and returns a log of the final values for each process in input
    public StringBuilder FCFS()
    {
        StringBuilder log = new StringBuilder("FCFS:\n");
        //Check to see if any Process objects in input have an arrival time of 0 and if so then move them into waiting
        this.moveInput();
        //Run the dispatcher for its run time
        this.runDispatcher();
        while(this.totalTime < 24)
        {
            //check to see if any Process objects in input have the same arrival time as the current totalTime
            this.moveInput();
            //If there are no processes running then move a process from waiting to running
            if(this.getWaiting().isEmpty())
            {
                this.moveInput();
            }
            //Execute the Process we just moved to running
            log.append(this.runProcess());
            //run the dispatcher for its run time and update the waiting time for any Process objects in waiting
            this.runDispatcher();
            if(this.getComplete().size() == this.getInput().size()){break;}
        }
        //Return a log of all processes that have executed
        return log;
    }

    //Shortest Process Next Algorithm
    //Preconditions: Current Dispatcher object has been declared & initialized and resetDispatcher() if another algorithm has been
    //                  called and displayed
    //Postconditions: Simulates the SPN scheduling algorithm thus calculates specific time values and returns a log of the final values for each process in input
    public StringBuilder SPN()
    {
        StringBuilder log = new StringBuilder("SPN:\n");
        //Check to see if any Process objects in input have an arrival time of 0 and if so then move them into waiting
        this.moveInput();
        //Run the dispatcher for its run time
        this.runDispatcher();
        while(this.totalTime < 24)
        {
            //Check to see if any Process objects in input have the same arrivalTime as totalTime and if so move them to waiting
            this.moveInput();
            if(this.getWaiting().isEmpty())
            {
                this.moveInput();
            }
            //Sort all waiting processes by their execution time, shortest first
            this.getWaiting().sort(new ProcessSorter.serviceTimeSort());
            //Execute the Process we just moved to running and add its output to log
            log.append(this.runProcess());
            //run the dispatcher for its run time and update the waiting time for any Process objects in waiting
            this.runDispatcher();
            if(this.getComplete().size() == this.getInput().size()){break;}
        }
        //Return a log of all processes that have executed
        return log;
    }

    //Preemptive Priority Algorithm
    //Preconditions: Current Dispatcher object has been declared & initialized and resetDispatcher() if another algorithm has been
    //                  called and displayed
    //Postconditions: Simulates the PP scheduling algorithm thus calculates specific time values and returns a log of the final values for each process in input
    public String PP()
    {
        String log = "\nPP:\n";
        //check for processes with an arrival time of 0
        this.moveInput();
        while(this.totalTime < 29)
        {
            this.runDispatcher();
            this.moveInput();
            this.setCurrent(this.getNext());
            this.getCurrent().setStartTime(this.totalTime);
            log += "T" + this.getCurrent().getStartTime() + ": " + this.getCurrent().getID() + "(" + this.getCurrent().getPriority() + ")\n";
            while(this.getCurrent().getServiceCount() < this.getCurrent().getServiceTime())
            {
                this.totalTime++;
                this.moveInput();
                this.getCurrent().incServiceCount();
                if(this.checkInterrupt())
                {
                    this.runDispatcher();
                    this.getWaiting().add(this.getCurrent());
                    this.setCurrent(this.getNext());
                    this.getCurrent().setStartTime(this.totalTime);
                    log += "T" + this.getCurrent().getStartTime() + ": " + this.getCurrent().getID() + "(" + this.getCurrent().getPriority() + ")\n";
                }
            }
            this.getCurrent().setTat(this.totalTime);
            this.getCurrent().setWaitTime();
            this.getComplete().add(this.getCurrent());
            if(this.getComplete().size() == this.getInput().size()) {break;}
        }
        return log;
    }

    //Priority Round Robin Algorithm
    //Preconditions: Current Dispatcher object has been declared and initialized and resetDispatcher() has been called if another algorithm has been
    //                      called and displayed
    //Postconditions: Simulates the PRR scheduling algorithm thus calculates specific time values and returns a log of the final values for each process in input
    public String PRR()
    {
        String log = "\nPRR:\n";
        //Assign each Process object in input there own time slice based on priority before we start running the algorithm
        this.assignTimeSlice();
        //check if any processes have an arrival time of 0
        this.moveInput();
        while(this.totalTime < 28)
        {
            //run the dispatcher for its run time
            this.runDispatcher();
            //move any processes in input to waiting if they have the same arrival time as totalTime
            this.moveInput();
            //Probably index issues when arrival time is greater than 1
            this.setCurrent(this.getWaiting().remove(0));
            //Set the start time of the process
            this.getCurrent().setStartTime(this.totalTime);
            //Update the output log for the process that is currently running
            log += "T" + this.getCurrent().getStartTime() + ": " + this.getCurrent().getID() + "(" + this.getCurrent().getPriority() + ")\n";
            //run the process for its time slice
            for(int i = 0; i < this.getCurrent().getTimeSlice(); i++)
            {
                this.totalTime++;
                this.moveInput();
                this.getCurrent().incServiceCount();
                if(this.getCurrent().getServiceCount() == this.getCurrent().getServiceTime())
                {
                    this.getCurrent().setTat(this.totalTime);
                    this.getCurrent().setWaitTime();
                    this.getComplete().add(this.getCurrent());
                    break;
                }
            }
            //if a process has not completed yet then add it back to waiting to run again for its time slice
            if(!this.getComplete().contains(this.getCurrent())) {this.getWaiting().add(this.getCurrent());}
            //if all processes are complete then we are done
            if(this.getComplete().size() == this.getInput().size()) {break;}
        }
        return log;
    }

    //Preconditions: An algorithm function has been called and thus this.complete has been populated with Process objects that have been executed
    //Postconditions: Outputs the ID, turnaround and wait time of each Process in complete
    //                  and returns complete so we can calculate the average turnaround and wait time for the algorithm function that was called
    public ArrayList<Process> output()
    {
        System.out.println("Process Turnaround Time Waiting Time");
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
        return this.getComplete();
    }

    //Moves Process objects from input to waiting
    //Preconditions: This.setInput() has been called
    //Postconditions: Moves any Process objects with an arrivalTime the same as the current totalTime from input to waiting
    public void moveInput()
    {
        for(Process p : this.getInput())
        {
            if(p.getArrivalTime() == this.totalTime)
            {
                this.waiting.add(p);
            }
        }
    }

    //Runs one Process object for its entire execution/service time in the running queue
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: executes and runs the first Process object in running and once its complete, calculates it's turnaround and wait time then
    //                  remove that Process object from running and stores it in complete
    public String runProcess()
    {
        String log = "";
        if(this.getWaiting().isEmpty()) {this.moveInput();}
        this.setCurrent(this.getWaiting().get(0));
        //have to reset service count here because I was getting index out of bounds errors even though I called resetDispatcher()
        this.getCurrent().setServiceCount(0);
        this.getCurrent().setStartTime(this.totalTime);
        log += "T" + this.getCurrent().getStartTime() + ": " + this.getCurrent().getID() + "(" + this.getCurrent().getPriority() + ")\n";
        while(this.getCurrent().getServiceCount() < this.getCurrent().getServiceTime())
        {
            this.moveInput();
            this.getCurrent().incServiceCount();
            this.totalTime++;
        }
        this.getCurrent().setTat(this.totalTime);
        this.getCurrent().setWaitTime();
        this.getComplete().add(this.getCurrent());
        this.getWaiting().remove(this.getCurrent());
        return log;
    }

    //Run the dispatcher for however long its run time is set
    //Preconditions: the current Dispatcher object has been declared and initialized
    //Postconditions: runs the dispatcher for its specified run time
    public void runDispatcher() {this.totalTime += this.getRunTime();}

    //Reset the dispatcher in between each scheduling algorithm
    //Preconditions: the current Dispatcher object has been declared and initialized and at least one scheduling algorithm has been called
    //Postconditions: reset the main primary private member variables of the dispatcher
    public void resetDispatcher()
    {
        this.setWaiting(new ArrayList<>());
        this.setComplete(new ArrayList<>());
        //something was happening where the execution counter for each Process object was being updated
        for(Process p : this.getInput()) {p.setServiceCount(0);}
        this.setCurrent(new Process());
        this.setTotalTime(0);
    }

    //Returns the next Process object in waiting based on priority where the lower the number the higher the priority
    //Preconditions: this.getWaiting().isEmpty() == false
    //Postconditions: returns the Process object with the next highest priority in waiting and removes it from waiting
    private Process getNext()
    {
        //if there are no processes waiting to run
        //then check to see if any processes in input have the same arrivalTime as the totalTime
        if(this.getWaiting().isEmpty())
        {
            this.moveInput();
        }
        //Capture the first process in waiting
        Process next = this.getWaiting().get(0);
        //temporary Process object used during iterating through waiting
        Process temp;
        //check for every process in waiting to see which one has the highest priority and assign it to next
        for(Process p : this.getWaiting())
        {
            temp = p;
            //consider all processes that have arrived before the current total time that has elapsed
            if((next.getPriority() > temp.getPriority()) && temp.getArrivalTime() < this.getTotalTime())
            {
                next = temp;
            }
        }
        //remove whatever Process object next is assigned to from waiting and return it
        this.getWaiting().remove(next);
        return next;
    }

    //Checks to see if a Process with a higher priority is waiting and if so return true so that we know we need to interrupt the process currently running
    //Preconditions: this.getCurrent() != null
    //Postconditions: returns true if there is a Process object with a higher priority in waiting
    private boolean checkInterrupt()
    {
        for(Process p : this.getWaiting())
        {
            if(p.getPriority() < this.getCurrent().getPriority()) {return true;}
        }
        return false;
    }

    //Setters

    //Preconditions: Input file has been read
    //Postconditions: The run time for the current Dispatcher object is assigned the value of runTime
    public void setRunTime(int runTime) {this.runTime = runTime;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of wait to waiting
    public void setWaiting(ArrayList<Process> wait) {this.waiting = wait;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of comp to complete
    public void setComplete(ArrayList<Process> comp) {this.complete = comp;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assign the value of input to the private member variable input
    public void setInput(ArrayList<Process> input)
    {
        for(Process p : input)
        {
            //weird bug with the execution counter persisting through different runs of the algorithm
            //so have to set the execution counter for each Process object to 0 to avoid the bug
            p.setServiceCount(0);
            this.input.add(p);
        }
    }

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assigns the Process Object running to the private member variable current
    public void setCurrent(Process running) {this.current = running;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Assigns the value of time to the private member variable totalTime
    public void setTotalTime(int time) {this.totalTime = time;}

    //Preconditions: File has been read and this.setInput has been called
    //Postconditions: Assigns each Process object in this.input a time slice/quantum based on its priority
    //                  Priority of 0, 1 or 2 classify the process as Higher Priority Class and get a time slice of 4
    //                  Priority of 3, 4 or 5 classify the process as Lower Priority CLass and get a time slice of 2
    private void assignTimeSlice()
    {
        for(Process p : this.getInput())
        {
            if(p.getPriority() == 0 || p.getPriority() == 1 || p.getPriority() == 2) {p.setTimeSlice(4);}
            else if(p.getPriority() == 3 || p.getPriority() == 4 || p.getPriority() == 5) {p.setTimeSlice(2);}
        }
    }

    //Getters

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the run time of the Dispatcher object
    public int getRunTime() {return this.runTime;}

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
    //Postconditions: Returns the current process that is running in the dispatcher in the form of Process Object
    public Process getCurrent() {return this.current;}

    //Preconditions: Dispatcher object has been declared and initialized
    //Postconditions: Returns the total amount of time the dispatcher has been running for
    public int getTotalTime() {return this.totalTime;}
}
