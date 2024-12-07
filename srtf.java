import javax.swing.*;
import java.awt.*;
import java.util.*;

class Process {
    String name;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int waitingTime;
    int turnaroundTime;

    Process(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class SRTFSchedulingWithBonus {
    static List<String> executionOrder = new ArrayList<>();
    static Map<String, Color> processColors = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();

        
        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter process name, arrival time, and burst time (space-separated): ");
            String name = scanner.next();
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            processes.add(new Process(name, arrivalTime, burstTime));

        
            processColors.put(name, new Color((int) (Math.random() * 0x1000000)));
        }

        
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        
        int currentTime = 0;
        int completed = 0;
        Process currentProcess = null;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));
        int starvationThreshold = 5; 

        while (completed < n) {
            
            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            
            for (Process p : readyQueue) {
                if (currentTime - p.arrivalTime > starvationThreshold) {
                    p.remainingTime--; 
                }
            }

            if (currentProcess != null && currentProcess.remainingTime > 0) {
                readyQueue.add(currentProcess);
            }

            if (!readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                executionOrder.add(currentProcess.name); 
                currentProcess.remainingTime--;
                currentTime++;

                if (currentProcess.remainingTime == 0) {
                    completed++;
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                }
            } else {
                currentTime++;
                executionOrder.add("Idle"); 
            }
        }

        // Display results
        System.out.println("\nProcess\tArrival Time\tBurst Time\tCompletion Time\tTurnaround Time\tWaiting Time");
        for (Process p : processes) {
            System.out.printf("%s\t\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n",
                    p.name, p.arrivalTime, p.burstTime, p.completionTime, p.turnaroundTime, p.waitingTime);
        }

        double avgWaitingTime = processes.stream().mapToInt(p -> p.waitingTime).average().orElse(0.0);
        double avgTurnaroundTime = processes.stream().mapToInt(p -> p.turnaroundTime).average().orElse(0.0);

        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);

        // Graphical Representation (Bonus)
        SwingUtilities.invokeLater(() -> showGanttChart(executionOrder));
        scanner.close();
    }

    private static void showGanttChart(List<String> executionOrder) {
        JFrame frame = new JFrame("Gantt Chart");
        frame.setSize(800, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GanttChartPanel(executionOrder));
        frame.setVisible(true);
    }
}

class GanttChartPanel extends JPanel {
    List<String> executionOrder;

    public GanttChartPanel(List<String> executionOrder) {
        this.executionOrder = executionOrder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50, y = 50, width = 30, height = 50;

        for (String process : executionOrder) {
            g.setColor(SRTFSchedulingWithBonus.processColors.getOrDefault(process, Color.LIGHT_GRAY));
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            g.drawString(process, x + 5, y + 30);
            x += width;
        }

        g.drawString("Time ->", 50, y + 80);
    }
}
