import java.util.*;

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
        boolean justSwitched = false;

        while (finishedProcess < processes.size()) {

            Process selected = findShortestProcess(processes, time, lastProcess);

            if (selected == null) {
                timeline.add("t=" + time + " CPU: IDLE");
                time++;
                lastProcess = null;
                continue;
            }

            if (lastProcess != null && selected != lastProcess && contextSwitch > 0 && !justSwitched) {
                for (int i = 0; i < contextSwitch; i++) {
                    timeline.add("t=" + time + " CONTEXT SWITCH (" + lastProcess.id + " -> " + selected.id + ")");
                    time++;
                }
                justSwitched = true;
                continue;
            }

            if (selected.firstStartTime == -1) {
                selected.firstStartTime = time;
            }

            timeline.add("t=" + time + " CPU: " + selected.id + " (remaining: " + selected.remainingTime + ")");
            justSwitched = false;
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
