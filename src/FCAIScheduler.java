// - it works either 40 % or 100% of q there's no between
//   need to handle this case, my idea is to iterate unit by unit after the 40 % and check if there's
//   any process with less fcai factor.

// - I think there's some problems with calculating the time i solved it in the past but i accidentally lost the code
//   so I need to recheck the code

// - need to calculate turnaround and waiting times

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

    public int calculateFCAIFactor(Process process, double v1, double v2) {
        return (int) ((10 - process.getPriority())
                + ceil (process.getArrivalTime() / v1)
                + ceil (process.getRemainingTime() /v2));
    }

    public void scheduleProcesses(List<Process> processes){
        double V1 = calculateV1(processes);
        double V2 = calculateV2(processes);
        System.out.println("V1 = " + V1 + ", V2 = " + V2);

        //System.out.print(calculateFCAIFactor(processes.get(3), V1, V2));

        // initial fcaiFactor calculation
        for(Process process : processes){
            process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
        }

        int currentTime = 0;
        List<String> executionOrder = new ArrayList<>();
        Queue<Process> readyQueue = new LinkedList<>();

        //we are adding the ready processes to the ready queue so we could schedule next
        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            // there's no ready processes
            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            //sorting ready queue with fcaiFactor
            List<Process> list = new ArrayList<>(readyQueue);
            list.sort((p1, p2) -> calculateFCAIFactor(p2, V1, V2) - calculateFCAIFactor(p1, V1, V2));
            readyQueue.clear();
            readyQueue.addAll(list);


            Process currentProcess = readyQueue.poll();
            int quantum = currentProcess.getQuantum();
            int executionTime = (int) Math.ceil(quantum * 0.4); // Non-preemptive for 40% of quantum

            //non-preemptive
            if (currentProcess.getRemainingTime() <= executionTime) {
                executionTime = currentProcess.getRemainingTime();
                executionOrder.add(currentProcess.getName() + " [" + currentTime + "-" + (currentTime + executionTime) + "]");
                currentTime += currentProcess.getRemainingTime();
                currentProcess.setRemainingTime(0);

//                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
//                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
            }//preemptive
            else {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
//                currentTime += executionTime;

                list = new ArrayList<>(readyQueue);
                list.sort((p1, p2) -> calculateFCAIFactor(p2, V1, V2) - calculateFCAIFactor(p1, V1, V2));
                readyQueue.clear();
                readyQueue.addAll(list);

                if(currentProcess == readyQueue.peek()){
                    if(currentProcess.getRemainingTime() >=  currentProcess.getQuantum() - executionTime){
                        executionOrder.add(currentProcess.getName() + " [" + currentTime + "-" + (currentTime + currentProcess.getQuantum()) + "]");
                        currentTime += currentProcess.getQuantum();
                        currentProcess.setRemainingTime(currentProcess.getRemainingTime() - currentProcess.getQuantum());
                        currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    }
                    else{
                        executionOrder.add(currentProcess.getName() + " [" + currentTime + "-" + (currentTime + executionTime + currentProcess.getRemainingTime()) + "]");
                        currentTime += currentProcess.getRemainingTime() + executionTime;
                        currentProcess.setRemainingTime(0);
                    }
                    readyQueue.poll();
                }
                else{
                    executionOrder.add(currentProcess.getName() + " [" + currentTime + "-" + (currentTime + executionTime) + "]");
                    currentTime += executionTime;
                    currentProcess.setQuantum(currentProcess.getQuantum() * 2 - executionTime);
                }
            }

        }

        System.out.println("Execution Order: " + executionOrder);

//            if (currentProcess.getRemainingTime() > 0) {
//                quantum += executionTime;
//                readyQueue.add(currentProcess);
//            } else {
//                // Calculate turnaround and waiting times
//                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
//                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
//
//                list = new ArrayList<>(readyQueue);
//                list.sort((p1, p2) -> calculateFCAIFactor(p2, V1, V2) - calculateFCAIFactor(p1, V1, V2));
//                readyQueue.clear();
//                readyQueue.addAll(list);
//                if(currentProcess != readyQueue.poll()){
//                    quantum += quantum-executionTime;
//
//                }
//                else {
//                    currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
//                }






        for(Process process : processes){
            System.out.println(process);
        }
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