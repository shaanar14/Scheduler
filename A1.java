/*
    Author: Shaan Arora C3236359
    A1 driver class
        Contains functionality to read a file and set up a Dispatcher object and multiple Process objects
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class A1
{
    public static void main(String[] args)
    {
        assert(args.length != 1) : "File name required";
        String fileName = args[0];
        ArrayList<Process> input = new ArrayList<>();
        ArrayList<Process> FCFS = new ArrayList<>();
        ArrayList<Process> SPN = new ArrayList<>();
        //ArrayList<Process> PP = new ArrayList<>();
        Dispatcher d = new Dispatcher();
        try
        {
            File f = new File(fileName);
            Scanner scan = new Scanner(f);
            //skip BEGIN
            scan.skip("[BEGIN]*");
            //Set up the Dispatcher object from the input file
            if(scan.hasNext("DISP:"))
            {
                scan.next();
                d.setRunTime(scan.nextInt());
            }
            //Skip END after DISP: in the input file
            scan.next();
            //Set up Process objects from the input file
            while(scan.hasNext())
            {
                //all the scan.next() calls are skipping the labels for those values in the input file
                if(scan.hasNext("ID:"))
                {
                    Process p = new Process();
                    scan.next();
                    p.setID(scan.next());
                    scan.next();
                    p.setArrivalTime(scan.nextInt());
                    scan.next();
                    p.setServiceTime(scan.nextInt());
                    scan.next();
                    p.setPriority(scan.nextInt());
                    scan.next();
                    input.add(p);
                }
                else{break;}
            }
            scan.close();
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        //Load the processes into the dispatcher
        d.setInput(input);
        //Run the first scheduling algorithm
        d.FCFS();
        FCFS = d.getComplete();
        //Wrap this output in functions
        d.outputFCS();
        //Reset the dispatcher
        d.resetDispatcher();
        d.SPN();
        d.outputSPN();
        d.resetDispatcher();
    }
}
