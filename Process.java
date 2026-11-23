import java.util.*;


public class Process {
    String id;
    int arrivalTime, burstTime, initialPriority;
    int remainingTime, completionTime = -1, firstStartTime = -1;
    int waitingTime, turnaroundTime, responseTime;
    int tier2EntryTime = -1;
    int effectivePriority;
    boolean finished = false;

    Process(String id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrivalTime = arrival;
        this.burstTime = burst;
        this.initialPriority = priority;
        this.remainingTime = burst;
        this.effectivePriority = priority;
    }


    Process copy() {
            return new Process(id, arrivalTime, burstTime, initialPriority);
     }

    static List<Process> copyProcesses(List<Process> input) {
    List<Process> result = new ArrayList<>();
    for (int i = 0; i < input.size(); i++) {
        result.add(input.get(i).copy());
    }
    result.sort(Comparator.comparingInt(p -> p.arrivalTime));

    return result;
    }
}
