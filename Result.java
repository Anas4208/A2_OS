import java.util.*;

public class Result{
    String algorithm;
    List<Process> processes;
    List<String> timeline;
    double averageWaiting, averageTurnaround, averageResponse;

    Result(String algorithm, List<Process> processes, List<String> timeline){
        this.algorithm = algorithm;
        this.processes = processes;
        this.timeline = timeline;
        calculateStatistics();
    }

    void calculateStatistics(){
        if(processes ==null || processes.size() == 0){
            averageWaiting = 0;
            averageTurnaround = 0;
            averageResponse = 0;
            return ;
        }
        double totalWaiting = 0;
        double totalTurnaround = 0;
        double totalResponse = 0;

        for(int i = 0; i < processes.size(); i++){
            Process p =processes.get(i);
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            p.responseTime = p.firstStartTime - p.arrivalTime;

            totalWaiting +=p.waitingTime;
            totalTurnaround += p.turnaroundTime;
            totalResponse += p.responseTime;

        }
        int n = processes.size();
        averageWaiting = totalWaiting /n;
        averageTurnaround = totalTurnaround/n;
        averageResponse = totalResponse/n;

    }

}