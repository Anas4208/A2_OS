import java.util.*;

/*
 * MEMBER 3 - Preemptive Shortest-Job-First (shortest remaining time first).
 * Rule from the assignment: the process with the shortest REMAINING time must always be the one running.
 * Context switching (CS) must be charged.
 *
 * Expected results (use them to test):
 *   Test case 1 (CS=0):  finish P1=4 P2=13 P3=6   waiting 0,5,1 (avg 2.00)  turnaround 4,12,3 (avg 6.33)  response 0,5,1 (avg 2.00)
 *   Test case 2 (CS=1):  finish P1=5 P2=17 P3=10  waiting 0,10,2 (avg 4.00) turnaround 5,16,6 (avg 9.00)  response 0,10,2 (avg 4.00)
 *                        (context switches at t=5 and t=10)
 */
public class SJF {

    // TODO (member 3): among processes that have arrived (arrivalTime <= time), are not finished and have
    // remaining time > 0, return the one with the smallest remainingTime, or null if there is none.
    // Tie on remaining time: 'preferred' (the process already on the CPU) wins - otherwise you preempt for
    // nothing and pay a useless context switch. If neither is 'preferred', the earlier arrival wins.
    static Process findShortestProcess(List<Process> processes, int time, Process preferred) {
        throw new UnsupportedOperationException("TODO member 3: findShortestProcess()");
    }

    // TODO (member 3): simulate one time unit per loop iteration, starting at time 0:
    //  1. processes = Process.copyProcesses(input); timeline = new list of strings.
    //  2. loop until all processes are finished:
    //       a. selected = findShortestProcess(...)
    //       b. nothing arrived yet -> add "t=.. CPU: IDLE" to the timeline, time++, forget the last process
    //          (no context switch is charged after idle), continue.
    //       c. selected differs from the last process that ran AND contextSwitch > 0 -> add one
    //          "CONTEXT SWITCH" line per time unit and advance time by contextSwitch;
    //          then look again (a shorter job may have arrived during the switch).
    //       d. set firstStartTime the first time a process runs.
    //       e. add a timeline line, remainingTime--, time++.
    //       f. if remainingTime == 0: finished = true, completionTime = time.
    //  3. return new Result("Preemptive SJF (CS=" + contextSwitch + ")", processes, timeline);
    static Result runPreemptiveSJF(List<Process> input, int contextSwitch) {
        throw new UnsupportedOperationException("TODO member 3: runPreemptiveSJF()");
    }
}
