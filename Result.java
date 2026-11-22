import java.util.*;

/*
 * MEMBER 2 - statistics.
 * Holds what one scheduler run produced. SJF and ACS build it with:
 *      new Result("name", processes, timeline)
 * where 'processes' are finished (completionTime and firstStartTime are set) and 'timeline' is the
 * list of printed lines (one per time unit). The constructor is the shared contract - keep it.
 */
public class Result {
    String algorithm;
    List<Process> processes;
    List<String> timeline;
    double averageWaiting, averageTurnaround, averageResponse;

    Result(String algorithm, List<Process> processes, List<String> timeline) {
        this.algorithm = algorithm;
        this.processes = processes;
        this.timeline = timeline;
        calculateStatistics();
    }

    // TODO (member 2): for EVERY process compute and store
    //      turnaroundTime = completionTime - arrivalTime
    //      waitingTime    = turnaroundTime  - burstTime       (context-switch time counts as waiting)
    //      responseTime   = firstStartTime  - arrivalTime
    // then set averageWaiting, averageTurnaround and averageResponse (sum / number of processes).
    // Take care of an empty list (no division by zero) and of integer division (use double).
    void calculateStatistics() {
        throw new UnsupportedOperationException("TODO member 2: calculateStatistics()");
    }
}
