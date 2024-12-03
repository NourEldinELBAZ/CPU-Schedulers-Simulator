# **CPU Scheduler Simulator**

A Java program to simulate various CPU scheduling algorithms, including an adaptive algorithm called **FCAI Scheduling**. This simulator is designed to demonstrate how different scheduling approaches handle process execution, optimize resource utilization, and resolve common challenges like starvation.

---

## **Features**
- Simulates the following scheduling algorithms:
  - **Non-preemptive Priority Scheduling**
  - **Non-preemptive Shortest Job First (SJF)** (with starvation prevention)
  - **Shortest Remaining Time First (SRTF)** (with starvation prevention)
  - **FCAI Scheduling** (an adaptive algorithm using dynamic factors like priority, arrival time, and remaining burst time)
- **Detailed Outputs:**
  - Execution order of processes
  - Waiting time for each process
  - Turnaround time for each process
  - Average waiting time and turnaround time
  - Quantum updates for FCAI Scheduling
- **Graphical Representation:**
  - Visual timeline of process execution using JavaFX

---

## **How FCAI Scheduling Works**
- **FCAI Factor Calculation:**
Where:
- `V1 = last arrival time / 10`
- `V2 = max burst time / 10`
- **Quantum Allocation Rules:**
- Initial quantum is dynamic and unique for each process.
- If a process completes its quantum without finishing, its quantum increases by `Q + 2`.
- If a process is preempted, it retains unused quantum.
