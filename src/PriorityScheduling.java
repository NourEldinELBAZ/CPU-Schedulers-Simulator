import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PriorityScheduling {

    public static void main(String[] args) {

        // Input handling
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        System.out.print("Enter context switching time: ");
        int contextSwitchTime = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter process name, burst time, and priority: ");
            String name = sc.next();
            int burstTime = sc.nextInt();
            int priority = sc.nextInt();
            processes.add(new Process(name, priority, burstTime, ""));
        }

        // Sort the processes based on priority
        processes.sort(Comparator.comparingInt(Process::getPriority));

        int currentTime = 0;
        List<TimelineSegment> timelineSegments = new ArrayList<>();

        System.out.println("\nProcess Execution Order:");

        for (Process process : processes) {
            // Add the process to the timeline
            timelineSegments.add(new TimelineSegment(process.getName(), currentTime, currentTime + process.getBurstTime(), Color.CYAN));
            System.out.println("Process " + process.getName() + " is executing from "
                    + currentTime + " to " + (currentTime + process.getBurstTime()));

            process.setTurnaroundTime(currentTime + process.getBurstTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());

            currentTime += process.getBurstTime();

            // Add context switch time if not the last process
            if (process != processes.get(processes.size() - 1)) {
                timelineSegments.add(new TimelineSegment("CS", currentTime, currentTime + contextSwitchTime, Color.GRAY));
                System.out.println("Context switch occurs, added " + contextSwitchTime + " units of time.");
                currentTime += contextSwitchTime;
            }
        }

        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("\nProcess Execution Details:");
        for (Process process : processes) {
            System.out.println("Process " + process.getName() +
                    ": Waiting Time = " + process.getWaitingTime() +
                    ", Turnaround Time = " + process.getTurnaroundTime());
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.println("\nAverage Turnaround Time = " + averageTurnaroundTime);
        System.out.println("Waiting Time = " + averageWaitingTime);

        // Display the graphical timeline using Swing
        SwingUtilities.invokeLater(() -> new TimelineFrame(timelineSegments));
    }
}

