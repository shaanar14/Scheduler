/*
    Assignment 1
    Author: Shaan Arora C3236359
    ProcessSorter Class
        Implements Comparator so we can specify how we want to sort Process objects
 */

import java.util.Comparator;

public class ProcessSorter
{
    //Allows Process objects to be sorted by their respective service/execution time
    public static class serviceTimeSort implements Comparator<Process>
    {
        @Override
        public int compare(Process p1, Process p2)
        {
            return Integer.compare(p1.getServiceTime(), p2.getServiceTime());
        }
    }

    //Allows Process objects to be sorted by their respective priority where 0 is the highest and 5 is the lowest priority
    public static class prioritySort implements Comparator<Process>
    {
        @Override
        public int compare(Process p1, Process p2)
        {
            if(p1.getPriority() > p2.getPriority())
            {
                return 1;
            }
            else if(p1.getPriority() < p2.getPriority())
            {
                return -1;
            }
            return 0;
        }
    }

    public static class IDSort implements Comparator<Process>
    {
        @Override
        public int compare(Process p1, Process p2)
        {
            return Character.compare(p1.getID().charAt(1), p2.getID().charAt(1));
        }
    }
}
