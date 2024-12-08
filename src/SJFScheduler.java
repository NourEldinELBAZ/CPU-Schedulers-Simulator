import java.util.*;


public class SJFScheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter process name, arrival time, and burst time: ");
            String name = sc.next();
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            processes.add(new Process(name, arrivalTime, burstTime));
        }

        simulateSJFWithAging(processes);
        
        System.out.println("\nProcess\tArrival\tBurst\tWaiting\tTurnaround");
        for (Process p : processes) {
            System.out.println(p.name + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                               p.waitingTime + "\t" + p.turnaroundTime);
        }
    }

    private static void simulateSJFWithAging(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort by arrival time initially
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
            Comparator.comparingInt((Process p) -> p.remainingTime) // Prefer shortest remaining burst time
                      .thenComparingInt(p -> p.age)                 // Break ties using age (older first)
        );

        int currentTime = 0;
        int completed = 0;
        while (completed < processes.size()) {
            // Add newly arrived processes to the ready queue
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            // Age all waiting processes in the queue
            for (Process p : readyQueue) {
                if (p.remainingTime > 0) p.age++;
            }

            if (!readyQueue.isEmpty()) {
                // Execute the process with the shortest burst time (or highest age if tie)
                Process currentProcess = readyQueue.poll();
                currentProcess.age = 0; // Reset age after execution
                if (currentProcess.remainingTime > 0) {
                    currentTime += currentProcess.remainingTime; // Complete the current process
                    currentProcess.remainingTime = 0;
                    currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completed++;
                }
            } else {
                // No process is ready, advance the time
                currentTime++;
            }
        }
    }
}

        class Process {
            String name;
            int arrivalTime;
            int burstTime;
            int waitingTime = 0;
            int turnaroundTime = 0;
            int remainingTime;
            int age = 0; // Additional field to help with aging
        
            Process(String name, int arrivalTime, int burstTime) {
                this.name = name;
                this.arrivalTime = arrivalTime;
                this.burstTime = burstTime;
                this.remainingTime = burstTime;
            }
}
