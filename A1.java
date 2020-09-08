/*
    Author: Shaan Arora C3236359
    A1 driver class
        Contains functionality to read a file and set up a Dispatcher object and multiple Process objects
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class A1
{
    public static void main(String[] args)
    {
        assert(args.length != 1) : "File name required";
        String fileName = args[0];
        Process p = new Process();
        Dispatcher d = new Dispatcher();
        try
        {
            File f = new File(fileName);
            Scanner scan = new Scanner(f);

            //while we are not at the end of the file
            while(scan.hasNextLine())
            {
                scan.nextLine();
            }
            scan.close();
            System.out.println(p);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
