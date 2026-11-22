import java.util.*;

/*
 * MEMBER 1 - data class + copying.
 * The fields and the constructor are the shared contract: do NOT rename or remove them,
 * because SJF, ACS, Result and Main2 all use them. Adding a field is fine if you tell the team.
 */
public class Process {
    String id;
    int arrivalTime, burstTime, initialPriority;
    int remainingTime, completionTime = -1, firstStartTime = -1;
    int waitingTime, turnaroundTime, responseTime;
    int tier2EntryTime = -1;      // time at which the process was demoted into Tier 2
    int effectivePriority;        // priority after aging (smaller number = higher priority)
    boolean finished = false;

    Process(String id, int arrival, int burst, int priority) {
        this.id = id;
        this.arrivalTime = arrival;
        this.burstTime = burst;
        this.initialPriority = priority;
        this.remainingTime = burst;
        this.effectivePriority = priority;
    }

    // TODO (member 1): return a NEW Process with the same id, arrival, burst and priority.
    // Runtime state (remaining, completion, first start, tier 2 entry, finished) must start fresh.
    Process copy() {
        throw new UnsupportedOperationException("TODO member 1: copy()");
    }

    // TODO (member 1):
    //  1. make a copy of every process in 'input' (so the original test data is never modified,
    //     and SJF and ACS can both run on the same input);
    //  2. sort the copies by arrival time;
    //  3. return the sorted list.
    static List<Process> copyProcesses(List<Process> input) {
        throw new UnsupportedOperationException("TODO member 1: copyProcesses()");
    }
}
