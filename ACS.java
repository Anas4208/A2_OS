import java.util.*;

public class ACS {
    private static final int AGING_INTERVAL = 4;

    static Result runACS(List<Process> input, int quantum, int contextSwitch) {
        if(quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be greater than 0");
        }

        List<Process> processes = (input == null) ? new ArrayList<>() : Process.copyProcesses(input);
        Queue<Process> tier1 = new ArrayDeque<>();
        List<Process> tier2 = new ArrayList<>();
        List<String> timeline = new ArrayList<>();

        int time = 0;
        int nextArrivalIdx = 0;
        int completedCount = 0;

        Process current = null;
        int currentTier = 0;
        int quantumUsed = 0;
        Process lastExecuted = null;

        while(completedCount < processes.size()){
            while(nextArrivalIdx < processes.size() && processes.get(nextArrivalIdx).arrivalTime <= time){
                tier1.add(processes.get(nextArrivalIdx));
                nextArrivalIdx++;
            }

            if(current == null){
                if(!tier1.isEmpty()){
                    current = tier1.poll();
                    currentTier = 1;
                    quantumUsed = 0;
                }
                else if(!tier2.isEmpty()){
                    current = chooseHighestPriority(tier2, time);
                    tier2.remove(current);
                    currentTier = 2;
                    quantumUsed = 0;
                }
            }

            if(current == null){
                timeline.add("t=" + time + ": IDLE");
                lastExecuted = null;
                time++;
                continue;
            }

            if(lastExecuted != null && lastExecuted != current && contextSwitch > 0){
                for(int cs = 0; cs < contextSwitch; cs++){
                    timeline.add("t=" + time + ": CONTEXT SWITCH (" + lastExecuted.id + "->" + current.id + ")");
                    time++;
                    while(nextArrivalIdx < processes.size() && processes.get(nextArrivalIdx).arrivalTime <= time){
                        tier1.add(processes.get(nextArrivalIdx));
                        nextArrivalIdx++;
                    }
                }
            }

            lastExecuted = current;
            
            if(current.firstStartTime == -1){
                current.firstStartTime = time;
            }

            String stateSnapshot = queuesState(tier1, tier2, time);
            timeline.add("t=" + time + ": RUNNING " + current.id + "(Tier " + currentTier + ") | " + stateSnapshot);

            current.remainingTime--;
            time++;
            if(currentTier == 1){
                quantumUsed++;
            }

            if(current.remainingTime == 0){
                current.finished = true;
                current.completionTime = time;
                completedCount++;
                current = null;
                currentTier = 0;
                quantumUsed = 0;
            }
            else if(currentTier == 1 && quantumUsed == quantum){
                current.tier2EntryTime = time;
                tier2.add(current);
                current = null;
                currentTier = 0;
                quantumUsed = 0;
            }
        }

        return new Result(
            "Adaptive Container Scheduler (Q=" + quantum + ", CS=" + contextSwitch + ")",
            processes,
            timeline
        );
    }


    static void updateAging(List<Process> tier2, int time) {

        // Null/empty safety
        if (tier2 == null)
            return;

        for (Process p : tier2) {
            int waited = Math.max(0, time - p.tier2EntryTime);

            p.effectivePriority = p.initialPriority - (waited / AGING_INTERVAL);
        }
    }

    static Process chooseHighestPriority(List<Process> tier2, int time) {
        updateAging(tier2, time);

        if (tier2.isEmpty())
            return null;

        Process best = tier2.get(0);

        for (Process p : tier2) {
            if (p.effectivePriority != best.effectivePriority) {
                if (p.effectivePriority < best.effectivePriority) {
                    best = p;
                }
            } else if (p.tier2EntryTime != best.tier2EntryTime) {
                if (p.tier2EntryTime < best.tier2EntryTime) {
                    best = p;
                }
            } else if (p.arrivalTime != best.arrivalTime) {
                if (p.arrivalTime < best.arrivalTime) {
                    best = p;
                }
            } else if (p.id.compareTo(best.id) < 0) {
                best = p;
            }
        }

        return best;
    }

    static String queuesState(Queue<Process> tier1, List<Process> tier2, int time) {
        updateAging(tier2, time);

        StringBuilder sb1 = new StringBuilder("Tier1=[");
        if (tier1 != null && !tier1.isEmpty()) {
            boolean first = true;
            for (Process p : tier1) {
                if (!first) {
                    sb1.append(", ");
                }
                sb1.append(p.id).append("(Remaining: ").append(p.remainingTime).append(", Priority: ")
                        .append(p.initialPriority).append(")");
                first = false;
            }
        }
        sb1.append("]");

        StringBuilder sb2 = new StringBuilder("Tier2=[");
        if (tier2 != null && !tier2.isEmpty()) {
            List<Process> sortedTier2 = new ArrayList<>(tier2);
            sortedTier2.sort((a, b) -> {
                if (a.effectivePriority != b.effectivePriority) {
                    return Integer.compare(a.effectivePriority, b.effectivePriority);
                }
                if (a.tier2EntryTime != b.tier2EntryTime) {
                    return Integer.compare(a.tier2EntryTime, b.tier2EntryTime);
                }
                if (a.arrivalTime != b.arrivalTime) {
                    return Integer.compare(a.arrivalTime, b.arrivalTime);
                }
                return a.id.compareTo(b.id);
            });

            boolean first = true;
            for (Process p : sortedTier2) {
                if (!first) {
                    sb2.append(", ");
                }
                sb2.append(p.id).append("(Remaining: ").append(p.remainingTime).append(", Priority: ")
                        .append(p.effectivePriority).append(")");
                first = false;
            }
        }
        sb2.append("]");

        return sb1 + " " + sb2;
    }

}
