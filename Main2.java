import java.util.*;

/*
 * MEMBER 5 - program entry, input and printing.
 * Runs both schedulers on the same input and prints each result.
 */
public class Main2 {

    // TODO (member 5): print the algorithm name, every line of result.timeline, then a table with one
    // row per process (Process, Arrival, Finish, Waiting, Turnaround, Response) and finally the
    // three averages with 2 decimals (use Locale.US so the decimal separator is a dot).
    static void printResult(Result result) {
        throw new UnsupportedOperationException("TODO member 5: printResult()");
    }

    // TODO (member 5): the two test cases of the assignment.
    //   Test case 1:  P1 arrival 0 burst 4 priority 2 | P2 1 7 3 | P3 3 2 1      (use Q=3, CS=0)
    //   Test case 2:  P1 arrival 0 burst 5 priority 4 | P2 1 6 2 | P3 4 4 3      (use Q=3, CS=1)
    static List<Process> testCase1() {
        throw new UnsupportedOperationException("TODO member 5: testCase1()");
    }

    static List<Process> testCase2() {
        throw new UnsupportedOperationException("TODO member 5: testCase2()");
    }

    // TODO (member 5): read the number of processes, then arrival (>= 0), burst (> 0) and priority (>= 0)
    // of each one; ids are P1, P2, ... Re-ask on invalid input (not a number / out of range).
    static List<Process> readCustomProcesses(Scanner sc) {
        throw new UnsupportedOperationException("TODO member 5: readCustomProcesses()");
    }

    // TODO (member 5): menu: 1 = test case 1, 2 = test case 2, 3 = custom processes; then read Q (> 0) and
    // CS (>= 0); run SJF.runPreemptiveSJF(processes, cs) and ACS.runACS(processes, q, cs); print both.
    public static void main(String[] args) {
        System.out.println("TODO member 5: main()");
    }
}
