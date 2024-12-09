import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SJFScheduler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of processes
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        // Input process details
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter process name, arrival time, burst time: ");
            String name = sc.next();
            int arrivalTime = sc.nextInt();
            int burstTime = sc.nextInt();
            int priority = 0;
            String color = "";
            processes.add(new Process(name, arrivalTime, burstTime, priority, color));
        }

        // Timeline segments to visualize execution
        List<TimelineSegment> timelineSegments = new ArrayList<>();

        // Simulate SJF scheduling
        simulateSJF(processes, timelineSegments);

        // Print process details
        System.out.println("\nProcess\tArrival\tBurst\tPriority\tWaiting\tTurnaround");
        int totalWaitingTime = 0, totalTurnaroundTime = 0;
        for (Process p : processes) {
            System.out.println(p.name + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.priority + "\t\t" + p.waitingTime + "\t" + p.turnaroundTime);
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }

        // Print averages
        System.out.println("\nAverage Waiting Time: " + (totalWaitingTime / (double) n));
        System.out.println("Average Turnaround Time: " + (totalTurnaroundTime / (double) n));

        // Show timeline in Swing
        SwingUtilities.invokeLater(() -> new TimelineFrame(timelineSegments));
    }

    private static void simulateSJF(List<Process> processes, List<TimelineSegment> timelineSegments) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime)); // Sort by arrival time initially

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(p -> p.burstTime) // Shortest burst time first
        );

        int currentTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            // Add all processes that have arrived by the current time to the ready queue
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Execute the process with the shortest burst time
                Process currentProcess = readyQueue.poll();

                // Execute the process
                timelineSegments.add(new TimelineSegment(currentProcess.name, currentTime, currentTime + currentProcess.burstTime, Color.CYAN));
                currentTime += currentProcess.burstTime;
                currentProcess.remainingTime = 0;

                // Calculate turnaround and waiting times
                currentProcess.turnaroundTime = currentTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;

                completed++;
            } else {
                // No process is ready; advance the current time
                currentTime++;
            }
        }
    }
}

// Timeline segment to hold process details
class TimelineSegment {
    String name;
    int startTime;
    int endTime;
    Color color;

    public TimelineSegment(String name, int startTime, int endTime, Color color) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.color = color;
    }
}

// Swing Frame to display the timeline
class TimelineFrame extends JFrame {
    public TimelineFrame(List<TimelineSegment> segments) {
        setTitle("Gnatt Chart");
        setSize(1600, 400); // Increased width for wider timeline
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new TimelinePanel(segments));
        setVisible(true);
    }
}

// Panel to draw the timeline
class TimelinePanel extends JPanel {
    List<TimelineSegment> segments;

    public TimelinePanel(List<TimelineSegment> segments) {
        this.segments = segments;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int x = 20; // Starting position
        int y = 100; // Vertical position
        int height = 60; // Bar height
        int scale = 30; // Increased scale for even wider blocks

        g2d.setFont(new Font("Arial", Font.BOLD, 16)); // Larger font for labels

        for (TimelineSegment segment : segments) {
            int width = (segment.endTime - segment.startTime) * scale; // Scale by 30 for wider blocks
            g2d.setColor(segment.color);
            g2d.fillRect(x, y, width, height);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, width, height);

            // Draw the label
            g2d.drawString(segment.name, x + width / 2 - 10, y + height / 2);

            // Move to the next segment position
            x += width;
        }

        // Draw the timeline axis
        g2d.setColor(Color.BLACK);
        g2d.drawLine(20, y + height + 10, x, y + height + 10);

        // Draw time markers
        x = 20;
        for (TimelineSegment segment : segments) {
            g2d.drawString(String.valueOf(segment.startTime), x - 5, y + height + 30);
            x += (segment.endTime - segment.startTime) * scale;
        }
        g2d.drawString(String.valueOf(segments.get(segments.size() - 1).endTime), x - 5, y + height + 30);
    }
}
