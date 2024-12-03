import java.util.*;

import static java.lang.Math.ceil;

public class FCAIScheduler {
    //V1 = last arrival time of all processes/10
    //V2 = max burst time of all processes/10
    //FCAI Factor = (10−Priority) + (Arrival Time/V1) + (Remaining Burst Time/V2)
    private double calculateV1(List<Process> processes) {
        double lastArrivalTime = -1.0;
        for (Process process : processes) {
            if (process.getArrivalTime() > lastArrivalTime) {
                lastArrivalTime = process.getArrivalTime();
            }
        }
        return lastArrivalTime/10;
    }

    private double calculateV2(List<Process> processes) {
        double maxBrustTime = -1.0;
        for (Process process : processes) {
            if (process.getBurstTime() > maxBrustTime) {
                maxBrustTime = process.getBurstTime();
            }
        }
        return maxBrustTime/10;
    }

    public  int calculateFCAIFactor(Process process, double v1, double v2) {
        return (int) ((10 - process.getPriority())
                + ceil (process.getArrivalTime() / v1)
                + ceil (process.getRemainingTime() /v2));
    }

    public void scheduleProcesses(List<Process> processes){
        double V1 = calculateV1(processes);
        double V2 = calculateV2(processes);
        System.out.println("V1 = " + V1 + ", V2 = " + V2);

        System.out.print(calculateFCAIFactor(processes.get(3), V1, V2));
    }







    public static void main(String[] args) {
        Process process1 = new Process("p1", 0, 17, 4, 4, "red");
        Process process2 = new Process("p2", 3, 6, 9, 3, "green");
        Process process3 = new Process("p3", 4, 10, 3, 5, "blue");
        Process process4 = new Process("p4", 29, 4, 10, 2, "yellow");
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(process1);
        processes.add(process2);
        processes.add(process3);
        processes.add(process4);
        FCAIScheduler scheduler = new FCAIScheduler();
        scheduler.scheduleProcesses(processes);
        //System.out.println(scheduler.processes);

//        System.out.println(scheduler.calculateFCAIFactor(process1));
//        System.out.println(scheduler.calculateFCAIFactor(process2));
//        System.out.println(scheduler.calculateFCAIFactor(process3));
//        System.out.println(scheduler.calculateFCAIFactor(process4));
    }
}
