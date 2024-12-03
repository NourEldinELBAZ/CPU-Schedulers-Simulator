public class Process {
    private String name;          // Process name (e.g., P1, P2)
    private int arrivalTime;      // Time at which the process arrives
    private int burstTime;        // Total burst time of the process
    private int priority;         // Priority of the process
    private int remainingTime;    // Remaining burst time (used in preemptive scheduling)
    private int waitingTime;      // Total waiting time for the process
    private int turnaroundTime;   // Total turnaround time for the process
    private String color;         // Color for graphical representation (e.g., HEX or color name)
    private int FCAI_Factor= 0;
    private int quantum= 0;

    // Constructor
    public Process(String name, int arrivalTime, int burstTime, int priority, String colo) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // Initially, remaining time is equal to burst time
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.color = color;
    }

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // Initially, remaining time is equal to burst time
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.color = color;
        this.quantum = quantum;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getFCAI_Factor() {
        return FCAI_Factor;
    }

    public void setFCAI_Factor(int FcaI_Factor) {
        this.FCAI_Factor = FCAI_Factor;
    }

    public int getQuantum() {return quantum;}
    public void setQuantum(int quantum) {this.quantum = quantum;}
    // Utility Methods
    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", remainingTime=" + remainingTime +
                ", waitingTime=" + waitingTime +
                ", turnaroundTime=" + turnaroundTime +
                ", color='" + color + '\'' +
                '}';
    }
}
