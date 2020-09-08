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
    public void setID(String id) {this.ID = id;}

    public void setArrivalTime(int arrive) {this.arrivalTime = arrive;}

    public void setStartTime(int start) {this.startTime = start;}

    public void setPriority(int p) {this.priority = p;}

    public void setServiceTime(int service) {this.serviceTime = service;}

    public void setTat(int turnaround) {this.tat = turnaround - this.getArrivalTime();}

    public void setWaitTime(int wait) {this.waitTime = wait;}

    //Getters
    public String getID() {return this.ID;}

    public int getArrivalTime() {return arrivalTime;}

    public int getStartTime() {return startTime;}

    public int getPriority() {return priority;}

    public int getServiceTime() {return serviceTime;}

    public int getTat() {return tat;}

    public int getWaitTime() {return waitTime;}

    //Override toString for output
    @Override
    public String toString()
    {
        String output = "";
        output = "ID: " + this.getID() + " Arrive: " +this.getArrivalTime() + " Service: " + this.getServiceTime() + " Priority: " + this.getPriority();
        return output;
    }

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