// the idea is not to reassign the current process, we simply switch between the currentprocess and
// if we find betterprocess after 40% we assign currentProcess = betterProcess
// else we assign it to null and in the next iteration if it's null we assign it to readyQueue.top;

import java.util.*;

import static java.lang.Math.ceil;

public class FCAIScheduler {
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

    public void scheduleProcesses(List<Process> processes) {
        double V1 = calculateV1(processes);
        double V2 = calculateV2(processes);

        // Initial FCAI factor calculation
        for (Process process : processes) {
            process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
        }

        int currentTime = 0;
        List<String> executionOrder = new ArrayList<>();//records for input
        Queue<Process> readyQueue = new LinkedList<>();
        Process currentProcess = null;

        while(!processes.isEmpty() || !readyQueue.isEmpty()) {
            Iterator<Process> iterator = processes.iterator();
            while (iterator.hasNext()) {
                Process process = iterator.next();
                if (process.getArrivalTime() <= currentTime) {
                    readyQueue.add(process);
                    iterator.remove();
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
//          chosing the top of readyqueue if there was no betterProcess
            if(currentProcess == null) {
                currentProcess = readyQueue.poll();
            }
            int quantum = currentProcess.getQuantum();
            int executionTime = quantum; // Total quantum to execute
            int nonPreemptiveTime = (int) Math.ceil(quantum * 0.4); // Non-preemptive execution
            int timeExecuted = 0;

            // non-preemptive 40%
            while (timeExecuted < nonPreemptiveTime && currentProcess.getRemainingTime() > 0) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                currentTime++;
                timeExecuted++;


                // Add new arrivals to the ready queue
                iterator = processes.iterator();
                while (iterator.hasNext()) {
                    Process process = iterator.next();
                    if (process.getArrivalTime() <= currentTime) {
                        readyQueue.add(process);
                        iterator.remove();
                    }
                }

                // calculating fcaiFactor
                for (Process process : processes) {
                    process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
                }
                if (!readyQueue.isEmpty()) {
                    for (Process process : readyQueue) {
                        process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
                    }
                }
            }

//          check if the process ended
            if(currentProcess.getRemainingTime() == 0) {
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                currentProcess = null;
                continue;
            }

            // after the 40% we should check if there's any process with better fcaiFactor in the ready queue
            // find if there's a betterProcess
            Process betterProcess = currentProcess;
            for (Process p : readyQueue) {
                if (p.getFCAI_Factor() < betterProcess.getFCAI_Factor()) {
                    betterProcess = p;
                }
            }

            // preempt with the better process and update q
            if (betterProcess != currentProcess) {
                currentProcess.setQuantum(2 * currentProcess.getQuantum() - timeExecuted);
                currentProcess.setFCAI_Factor(calculateFCAIFactor(currentProcess, V1, V2));
                readyQueue.add(currentProcess);
                executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                currentProcess = betterProcess;
                readyQueue.remove(betterProcess);
                continue;
            }

            // the next 60%
            boolean betterProcessFlag = false;
            while(timeExecuted < executionTime && currentProcess.getRemainingTime() > 0) {
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                currentTime++;
                timeExecuted++;

                // Add new arrivals to the ready queue
                iterator = processes.iterator();
                while (iterator.hasNext()) {
                    Process process = iterator.next();
                    if (process.getArrivalTime() <= currentTime) {
                        readyQueue.add(process);
                        iterator.remove();
                    }
                }

                //recalculate fcaiFactor
                for (Process process : processes) {
                    process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
                }
                if (!readyQueue.isEmpty()) {
                    for (Process process : readyQueue) {
                        process.setFCAI_Factor(calculateFCAIFactor(process, V1, V2));
                    }
                }

                // check if there's a better process
                betterProcess = currentProcess;
                for (Process p : readyQueue) {
                    if (p.getFCAI_Factor() < betterProcess.getFCAI_Factor()) {
                        betterProcess = p;
                    }
                }
                if (betterProcess != currentProcess) {
                    betterProcessFlag = true;
                    // if the process ended
                    if(currentProcess.getRemainingTime() == 0) {
                        currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                        executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                        // so in the next iteration currentProcess = readyQueue.poll()
                        currentProcess = null;
                    }
                    // if preempted
                    else{
                        currentProcess.setQuantum(2 * currentProcess.getQuantum() - timeExecuted);
                        currentProcess.setFCAI_Factor(calculateFCAIFactor(currentProcess, V1, V2));
                        readyQueue.add(currentProcess);
                        executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                        // currentProcess will be the better process in the next iteration
                        currentProcess = betterProcess;
                        readyQueue.remove(betterProcess);
                    }
                    break;
                }
            }

            // after the quantum ends
            //if there was no better process and the process was non-preemptive
            if(!betterProcessFlag) {
                if(currentProcess.getRemainingTime() == 0) {
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                    // so in the next iteration currentProcess = readyQueue.poll()
                    currentProcess = null;
                }
                else{
                    currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                    currentProcess.setFCAI_Factor(calculateFCAIFactor(currentProcess, V1, V2));
                    executionOrder.add(currentProcess.getName() + " [" + (currentTime - timeExecuted) + "-" + currentTime + "]");
                    readyQueue.add(currentProcess);
                    // so in the next iteration currentProcess = readyQueue.poll()
                    currentProcess = null;
                }
            }
        }

        System.out.println("Execution Order: " + executionOrder);

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
    }
}
