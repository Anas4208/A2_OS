import java.util.*;

public class Main2 {

    static void printResult(Result result) {

        System.out.println("\n====================================");
        System.out.println(result.algorithm);
        System.out.println("====================================");

        System.out.println("\nTimeline:");

        for (String line : result.timeline) {
            System.out.println(line);
        }

        System.out.println("\nProcess Statistics:");

        System.out.printf("%-10s %-10s %-10s %-10s %-12s %-10s%n",
                "Process", "Arrival", "Finish", "Waiting", "Turnaround", "Response");

        for (Process p : result.processes) {
            System.out.printf("%-10s %-10d %-10d %-10d %-12d %-10d%n",
                    p.id,
                    p.arrivalTime,
                    p.completionTime,
                    p.waitingTime,
                    p.turnaroundTime,
                    p.responseTime);
        }

        System.out.println("\nAverage Statistics:");

        System.out.printf(Locale.US, "Average Waiting Time: %.2f%n", result.averageWaiting);
        System.out.printf(Locale.US, "Average Turnaround Time: %.2f%n", result.averageTurnaround);
        System.out.printf(Locale.US, "Average Response Time: %.2f%n", result.averageResponse);

        System.out.println("====================================\n");
    }

    static List<Process> testCase1()
    {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 4, 2));
        processes.add(new Process("P2", 1, 7, 3));
        processes.add(new Process("P3", 3, 2, 1));
        return processes;
    }

    static List<Process> testCase2()
    {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", 0, 5, 4));
        processes.add(new Process("P2", 1, 6, 2));
        processes.add(new Process("P3", 4, 4, 3));
        return processes;
    }

    static List<Process> readCustomProcesses(Scanner sc)
    {
        List<Process> processes = new ArrayList<>();
        int count;

        while (true) {
            System.out.print("Enter number of processes: ");

            if (sc.hasNextInt()) {
                count = sc.nextInt();

                if (count > 0) {
                    break;
                }
            } else {
                sc.next();
            }

            System.out.println("Invalid input. Enter a positive number.");
        }

        for (int i = 1; i <= count; i++) {

            int arrival;
            int burst;
            int priority;

            System.out.println("\nProcess P" + i);

            while (true) {
                System.out.print("Arrival time: ");

                if (sc.hasNextInt()) {
                    arrival = sc.nextInt();

                    if (arrival >= 0) {
                        break;
                    }
                } else {
                    sc.next();
                }

                System.out.println("Invalid arrival time.");
            }

            while (true) {
                System.out.print("Burst time: ");

                if (sc.hasNextInt()) {
                    burst = sc.nextInt();

                    if (burst > 0) {
                        break;
                    }
                } else {
                    sc.next();
                }

                System.out.println("Invalid burst time.");
            }

            while (true) {
                System.out.print("Priority: ");

                if (sc.hasNextInt()) {
                    priority = sc.nextInt();

                    if (priority >= 0) {
                        break;
                    }
                } else {
                    sc.next();
                }

                System.out.println("Invalid priority.");
            }

            processes.add(new Process("P" + i, arrival, burst, priority));
        }

        return processes;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("===== CPU Scheduling Simulator =====");
        System.out.println("1. Test Case 1");
        System.out.println("2. Test Case 2");
        System.out.println("3. Custom Processes");

        int choice;

        while (true) {
            System.out.print("Choose an option: ");

            if (sc.hasNextInt()) {
                choice = sc.nextInt();

                if (choice >= 1 && choice <= 3) {
                    break;
                }
            } else {
                sc.next();
            }

            System.out.println("Invalid choice. Please choose 1, 2 or 3.");
        }

        List<Process> processes;
        int quantum;
        int contextSwitch;

        if (choice == 1) {
            processes = testCase1();
            quantum = 3;
            contextSwitch = 0;

        } else if (choice == 2) {
            processes = testCase2();
            quantum = 3;
            contextSwitch = 1;

        } else {
            processes = readCustomProcesses(sc);

            while (true) {
                System.out.print("Enter Time Quantum (Q): ");

                if (sc.hasNextInt()) {
                    quantum = sc.nextInt();

                    if (quantum > 0) {
                        break;
                    }
                } else {
                    sc.next();
                }

                System.out.println("Quantum must be greater than zero.");
            }

            while (true) {
                System.out.print("Enter Context Switch Time (CS): ");

                if (sc.hasNextInt()) {
                    contextSwitch = sc.nextInt();

                    if (contextSwitch >= 0) {
                        break;
                    }
                } else {
                    sc.next();
                }

                System.out.println("Context switch cannot be negative.");
            }
        }

        System.out.println("\nUsing Q = " + quantum
                + ", CS = " + contextSwitch);

        Result sjfResult = SJF.runPreemptiveSJF(processes, contextSwitch);
        Result acsResult = ACS.runACS(processes, quantum, contextSwitch);

        printResult(sjfResult);
        printResult(acsResult);

        sc.close();
    }
}
