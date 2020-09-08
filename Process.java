/*
    Assignment 1
    Author: Shaan Arora C3236359
    Process Class
        Core object of the entire dispatcher. Holds all necessary information about a process such as it's arrival time,
            execution time and priority
 */

import java.lang.String;

public class Process
{
    //Private Member Variables
    //The ID of the process
    private String ID;
    //The time a process arrives at
    private int arrivalTime;
    //The total time a process took
    private double completeTime;
    //The total amount of execution/service time for the process
    private int serviceTime;
    //The priority of the process
    private int priority;
    //The turnaround time of the process calculated by complete time - arrival time
    private double tat;
    //The total waiting time
    private double waitTime;
    //Maybe one for if the process is ready or blocked or maybe one for the normalized turnaround time

    //Default Constructor
    public Process()
    {
        this.ID = "";
        this.arrivalTime = 0;
        this.completeTime = 0;
        this.serviceTime = 0;
        //default priority is the lowest priority which is 5
        this.priority = 5;
        this.tat = 0.0;
        this.waitTime = 0.0;
    }

    //Parameter Constructor
    public Process(String id, int arrival, int complete, int service, int p, double turnaround, double wait)
    {
        this.ID = id;
        this.arrivalTime = arrival;
        this.completeTime = complete;
        this.serviceTime = service;
        this.priority = p;
        this.tat = turnaround;
        this.waitTime = wait;
    }

    //Setters
    public void setID(String id) {this.ID = id;}

    public void setArrivalTime(int arrive) {this.arrivalTime = arrive;}

    public void setCompleteTime(double complete) {this.completeTime = complete;}

    public void setPriority(int p) {this.priority = p;}

    public void setServiceTime(int service) {this.serviceTime = service;}

    public void setTat(double turnaround) {this.tat = turnaround;}

    public void setWaitTime(double wait) {this.waitTime = wait;}

    //Getters
    public String getID() {return this.ID;}

    public int getArrivalTime() {return arrivalTime;}

    public double getCompleteTime() {return completeTime;}

    public int getPriority() {return priority;}

    public int getServiceTime() {return serviceTime;}

    public double getTat() {return tat;}

    public double getWaitTime() {return waitTime;}

    //Override toString for output
    @Override
    public String toString()
    {
        String output = "";
        output += this.getID() + this.priority;
        return output;
    }
}