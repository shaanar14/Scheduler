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
        Dispatcher d = new Dispatcher();
        ArrayList<Process> input = new ArrayList<>();
        ArrayList<Process> FCFS, SPN, PP;
        double fcfsAvgT = 0.0, spnAvgT = 0.0, ppAvgT = 0.0, fcfsAvgW = 0.0, spnAvgW = 0.0, ppAvgW = 0.0;
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
        //Wrap this output in functions
        FCFS = d.outputFCS();
        //Reset the dispatcher
        d.resetDispatcher();
        //TODO run the PRR algorithm
        /*for(Process p : FCFS)
        {
            fcfsAvgT += p.getTat();
            fcfsAvgW += p.getWaitTime();
        }
        fcfsAvgT = (fcfsAvgT / FCFS.size());
        fcfsAvgW = (fcfsAvgW / FCFS.size());
        d.SPN();
        SPN = d.outputSPN();
        d.resetDispatcher();
        for(Process p : SPN)
        {
            spnAvgT += p.getTat();
            spnAvgW += p.getWaitTime();
        }
        spnAvgT = (spnAvgT / SPN.size());
        spnAvgW = (spnAvgW / SPN.size());
        d.PP();
        PP = d.outputPP();
        d.resetDispatcher();
        for(Process p : PP)
        {
            ppAvgT += p.getTat();
            ppAvgW += p.getWaitTime();
        }
        ppAvgT = (ppAvgT / PP.size());
        ppAvgW = (ppAvgW / PP.size());
        System.out.printf("\n%1.2f %1.2f", ppAvgT, ppAvgW);*/
        /*System.out.println("\nSummary");
        System.out.println("\nAlgorithm        Average Turnaround Time   Average Waiting Time");
        System.out.println(fcfsAvg);*/
    }
}
