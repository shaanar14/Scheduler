/*
    Assignment 1
    Author: Shaan Arora C3236359
    Process Class
        Core object of the entire dispatcher. Holds all necessary information about a process such as it's arrival time,
            execution time and priority
 */

import java.lang.String;

public class Process implements Comparable<Process>
{
    //Private Member Variables
    //The ID of the process
    private String ID;
    //The time a process arrives at
    private int arrivalTime;
    //The time the process starts executing
    private int startTime;
    //The total amount of execution/service time for the process
    private int serviceTime;
    //The priority of the process
    private int priority;
    //The turnaround time of the process calculated by the time it left the system - arrival time
    private int tat;
    //The total waiting time calculated by turnaround time - service time
    private int waitTime;
    //Maybe one for if the process is ready or blocked or maybe one for the normalized turnaround time

    //Default Constructor
    public Process()
    {
        this.ID = "";
        this.arrivalTime = 0;
        this.startTime = 0;
        this.serviceTime = 0;
        //default priority is the lowest priority which is 5
        this.priority = 5;
        this.tat = 0;
        this.waitTime = 0;
    }

    //Parameter Constructor
    public Process(String id, int arrival, int start, int service, int p, int turnaround, int wait)
    {
        this.ID = id;
        this.arrivalTime = arrival;
        this.startTime = start;
        this.serviceTime = service;
        this.priority = p;
        this.tat = turnaround;
        this.waitTime = wait;
    }

    //Setters

    //Preconditions: None
    //Postconditions: Assigns the value of id to the Process object's ID private member variable
    public void setID(String id) {this.ID = id;}

    //Preconditions: None
    //Postconditions: Assigns the value of arrive to the Process object's arrivalTime private member variable
    public void setArrivalTime(int arrive) {this.arrivalTime = arrive;}

    //Preconditions: None
    //Postconditions: Assigns the value of start to the Process object's startTime private member variable
    public void setStartTime(int start) {this.startTime = start;}

    //Preconditions: None
    //Postconditions: Assigns the value of priority to the Process object's priority private member variable
    public void setPriority(int p) {this.priority = p;}

    //Preconditions: None
    //Postconditions: Assigns the value of service to the Process object's serviceTime private member variable
    public void setServiceTime(int service) {this.serviceTime = service;}

    //Preconditions: None
    //Postconditions: the turnaround time for the Process object is calculated and updated
    public void setTat(int timeCompleted) {this.tat = timeCompleted - this.getArrivalTime();}

    //Preconditions: None
    //Postconditions: the wait time of the process object is calculated
    public void setWaitTime() {this.waitTime = (this.getTat() - this.getServiceTime());}

    //Getters
    
    //Preconditions: None
    //Postconditions: Returns the ID of the Process object
    public String getID() {return this.ID;}

    //Postconditions: None
    //Preconditions: Returns what time the Process object arrives at
    public int getArrivalTime() {return arrivalTime;}

    //Preconditions: None
    //Postconditions: Returns the time in which the Process object started running
    public int getStartTime() {return startTime;}
    
    //Preconditions: None
    //Postconditions: Returns the priority of the Process object
    public int getPriority() {return priority;}
    
    //Preconditions: None
    //Postconditions: Returns how long the Process object takes to execute
    public int getServiceTime() {return serviceTime;}

    //Preconditions:
    //Postconditions: Returns the turnaround time of the Process object
    public int getTat() {return tat;}
    
    //Preconditions: None
    //Postconditions: Returns the total time the Process object has waited
    public int getWaitTime() {return waitTime;}

    //Override toString for output
    @Override
    public String toString()
    {
        String output = "";
        output = "ID: " + this.getID() + " Arrive: " +this.getArrivalTime() + " Service: " + this.getServiceTime() + " Priority: " + this.getPriority();
        return output;
    }

    //So far only used for when we use SPN
    @Override
    public int compareTo(Process p)
    {
        //if the two Process objects have the exact same values then check their ID's
        if((this.arrivalTime == p.getArrivalTime()) && (this.serviceTime == p.getServiceTime()) && (this.getPriority() == p.getPriority()))
        {
            //check the number after the p in each objects ID
            return Character.compare(p.getID().charAt(1), this.ID.charAt(1));
        }
        //if all else fails return -1 meaning that the current object is less than the specific Process p
        return -1;
    }
}