// JavaScript source code

import java.util.*;



class Process {

    int priority;

    int burstTime;

    int processId;



    public Process(int processId, int priority, int burstTime) {

        this.processId = processId;

        this.priority = priority;

        this.burstTime = burstTime;

    }

}



public class PriorityScheduling {

    public static void main(String[] args) {

        List < Process > processes = new ArrayList <> ();

        processes.add(new Process(1, 3, 10));

        processes.add(new Process(2, 1, 4));

        processes.add(new Process(3, 4, 8));

        processes.add(new Process(4, 2, 5));



        // Sort the processes based on priority

        processes.sort(Comparator.comparingInt(p -> p.priority));



        int currentTime = 0;

        System.out.println("Process Execution Order:");

        for (Process process : processes) {

            System.out.println("Process " + process.processId + " is executing from " + currentTime + " to " + (currentTime + process.burstTime));

            currentTime += process.burstTime;

        }

    }

}
