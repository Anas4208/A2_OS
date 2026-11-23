import java.util.*;

/* 
 *   Test case 1 (CS=0):  finish P1=4 P2=13 P3=6   waiting 0,5,1 (avg 2.00)  turnaround 4,12,3 (avg 6.33)  response 0,5,1 (avg 2.00)
 *   Test case 2 (CS=1):  finish P1=5 P2=17 P3=10  waiting 0,10,2 (avg 4.00) turnaround 5,16,6 (avg 9.00)  response 0,10,2 (avg 4.00)
 *                        (context switches at t=5 and t=10)
 */
public class SJF {

    static Process findShortestProcess(List<Process> processes, int time, Process preferred) {

        Process shortest = null;

        for (Process p : processes) {
            if (p.arrivalTime > time || p.finished || p.remainingTime <= 0)
                continue;

            if (shortest == null || p.remainingTime < shortest.remainingTime) {
                shortest = p;
            } else if (p.remainingTime == shortest.remainingTime) {
                if (p == preferred && shortest != preferred) {
                    shortest = p;
                } else if (shortest != preferred && p != preferred && p.arrivalTime < shortest.arrivalTime) {
                    shortest = p;
                }
            }
        }
        return shortest;
    }

    static Result runPreemptiveSJF(List<Process> input, int contextSwitch) {
        List<Process> processes = Process.copyProcesses(input);
        List<String> timeline = new ArrayList<>();
        int time = 0;
        int finishedProcess = 0;
        Process lastProcess = null;

        while (finishedProcess < processes.size()) {

            Process selected = findShortestProcess(processes, time, lastProcess);

            if (selected == null) {
                timeline.add("t=" + time + " CPU: IDLE");
                time++;
                lastProcess = null;
                continue;
            }

            if (lastProcess != null && selected != lastProcess && contextSwitch > 0) {
                for (int i = 0; i < contextSwitch; i++) {
                    timeline.add("t=" + time + " CONTEXT SWITCH (" + lastProcess.id + " -> " + selected.id + ")");
                    time++;
                }
                lastProcess = selected;
                continue;
            }

            if (selected.firstStartTime == -1) {
                selected.firstStartTime = time;
            }

            timeline.add("t=" + time + " CPU: " + selected.id + " (remaining: " + selected.remainingTime + ")");
            selected.remainingTime--;
            time++;
            lastProcess = selected;

            if (selected.remainingTime == 0) {
                selected.finished = true;
                selected.completionTime = time;
                finishedProcess++;
            }
        }

        return new Result("Preemptive SJF (CS=" + contextSwitch + ")", processes, timeline);

    }
}
