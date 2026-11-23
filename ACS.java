import java.util.*;

/*
 * MEMBER 4 - Adaptive Container Scheduler (the three tiers).
 *   Tier 1: every new process joins a Round Robin queue (static quantum Q).
 *   Tier 2: a process that uses its WHOLE quantum without finishing is demoted to a NON-preemptive
 *           priority queue (smaller number = higher priority). Once it starts running it is never preempted.
 *   Tier 3: aging - while a process waits in Tier 2 its priority improves:
 *           effectivePriority = initialPriority - (timeWaitedInTier2 / AGING_INTERVAL)
 *   Tier 1 always runs before Tier 2. A process that finishes exactly at the end of its quantum is
 *   finished, not demoted.
 *
 * Expected results:
 *   Test case 1 (Q=3, CS=0): finish P1=9 P2=13 P3=8   waiting 5,5,3 (avg 4.33)  turnaround 9,12,5 (avg 8.67)  response 0,2,3 (avg 1.67)
 *   Test case 2 (Q=3, CS=1): finish P1=18 P2=15 P3=20 waiting 13,8,12 (avg 11.00) turnaround 18,14,16 (avg 16.00) response 0,3,4 (avg 2.33)
 *                            context switches start at t=3, 7, 11, 15 and 18
 *                            at t=11 the aged priorities are P1=2, P2=1, P3=3 (so P2 runs next)
 */
public class ACS {
    private static final int AGING_INTERVAL = 4;

    // TODO (member 4): one loop iteration = one time unit. Keep: tier1 (queue), tier2 (list), current process,
    // currentTier, quantumUsed, lastExecuted. Each iteration:
    //  1. move processes with arrivalTime <= time into tier1 (they are sorted by arrival).
    //  2. CPU free: take the head of tier1 (quantumUsed = 0); if tier1 is empty take
    //     chooseHighestPriority(tier2, time) and remove it from tier2.
    //  3. nothing to run -> IDLE line, time++, forget lastExecuted (no context switch after idle).
    //  4. lastExecuted != current and contextSwitch > 0 -> add one CONTEXT SWITCH line per time unit; arrivals
    //     during the switch also join tier1.
    //  5. IMPORTANT: right after step 4 set lastExecuted = current, otherwise the switch is charged again on
    //     every following tick of the same process.
    //  6. set firstStartTime if unset, add a timeline line (use queuesState), remainingTime--, time++,
    //     quantumUsed++ if in tier 1.
    //  7. remaining == 0 -> finished, completionTime = time, CPU free.
    //     else if in tier 1 and quantumUsed == quantum -> tier2EntryTime = time, add to tier2, CPU free.
    //     (a process in tier 2 just keeps running: it is non-preemptive)
    //  8. return new Result("Adaptive Container Scheduler (Q=" + quantum + ", CS=" + contextSwitch + ")", ...)
    // Throw IllegalArgumentException if quantum <= 0.
    static Result runACS(List<Process> input, int quantum, int contextSwitch) {
        throw new UnsupportedOperationException("TODO member 4: runACS()");
    }

    // TODO (member 4): for every process in tier2 set
    //      effectivePriority = initialPriority - (max(0, time - tier2EntryTime) / AGING_INTERVAL)
    static void updateAging(List<Process> tier2, int time) {
        
        //Null/empty safety
        if (tier2 == null) return;

        for(Process p : tier2) {
            int waited = Math.max(0, time - p.tier2EntryTime);

            p.effectivePriority = p.initialPriority - (waited / AGING_INTERVAL);
        }
    }

    // TODO (member 4): call updateAging first, then return the process with the smallest effectivePriority.
    // Tie: the one that entered tier 2 earlier.
    static Process chooseHighestPriority(List<Process> tier2, int time) {
        throw new UnsupportedOperationException("TODO member 4: chooseHighestPriority()");
    }

    // TODO (member 4 or 5): return the queues as text for the timeline lines, e.g.
    //      Tier1=[P2 rem=7, P3 rem=2] Tier2=[P1 rem=1 priority=2]
    // (update aging first; list tier 2 ordered by effectivePriority, then tier2EntryTime; empty -> [])
    static String queuesState(Queue<Process> tier1, List<Process> tier2, int time) {
        throw new UnsupportedOperationException("TODO member 4/5: queuesState()");
    }
}
