import java.util.ArrayList;
import java.util.List;

public class Process {
    public String name;          // Process name (e.g., P1, P2)
    public int arrivalTime;      // Time at which the process arrives
    public int burstTime;        // Total burst time of the process
    public int priority;         // Priority of the process
    public int remainingTime;    // Remaining burst time (used in preemptive scheduling)
    public int waitingTime;      // Total waiting time for the process
    public int turnaroundTime;   // Total turnaround time for the process
    public String color;         // Color for graphical representation (e.g., HEX or color name)
    public int FCAI_Factor= 0;
    public int quantum= 0;
    public List<String> quantumHistory = new ArrayList<>();
    int age = 0;
    int completionTime;

    // Constructor
//    public Process(String name,int arrivalTime, int burstTime, String color){
//        this.name = name;
//        this.arrivalTime = arrivalTime;
//        this.burstTime = burstTime;
//        this.remainingTime = burstTime;
//        this.color = color;
//    }
//    public Process(String name, int arrivalTime, int burstTime,int priority){
//        this.name = name;
//        this.arrivalTime = arrivalTime;
//        this.burstTime = burstTime;
//        this.priority = priority;
//    }

    public Process(String name, int arrivalTime, int burstTime){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
    // priority
    public Process (String name,int priority,int burstTime,String color){
        this.name = name;
        this.priority = priority;
        this.burstTime = burstTime;
        this.color = color;
    }

    // sjf
    public Process(String name, int arrivalTime, int burstTime, int priority, String color) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime; // Initially, remaining time is equal to burst time
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.color = color;
    }

    public Process(){}

    // fcai
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
        this.quantumHistory.add("Time" +arrivalTime+ ": Initial Quantum = " + quantum);
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

    public void setFCAI_Factor(int fcaiFactor) {
        this.FCAI_Factor = fcaiFactor;
    }

    public int getQuantum() {return quantum;}
    public void setQuantum(int quantum) {this.quantum = quantum;}

    public void updateQuantumHistory(int newQuantum, int currentTime) {
        this.quantumHistory.add("Time " + currentTime + ": Updated Quantum = " + newQuantum);
    }

    public List<String> getQuantumHistory() {
        return quantumHistory;
    }
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
                ", FCAIFactor=" + FCAI_Factor +
                ", color='" + color + '\'' +
                '}';
    }
}
