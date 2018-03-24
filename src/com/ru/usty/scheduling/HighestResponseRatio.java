package com.ru.usty.scheduling;

import java.util.Comparator;


public class HighestResponseRatio implements Comparator<Integer>{

    Scheduler HRRNSchedule;

    public HighestResponseRatio(Scheduler HRRNSchedule){
        this.HRRNSchedule = HRRNSchedule;
    }

    @Override
    public int compare(Integer x, Integer y) {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        long process1 = (HRRNSchedule.processExecution.getProcessInfo(x).totalServiceTime + HRRNSchedule.processExecution.getProcessInfo(x).elapsedWaitingTime) / HRRNSchedule.processExecution.getProcessInfo(x).totalServiceTime;
        long process2 = (HRRNSchedule.processExecution.getProcessInfo(y).totalServiceTime + HRRNSchedule.processExecution.getProcessInfo(y).elapsedWaitingTime) / HRRNSchedule.processExecution.getProcessInfo(y).totalServiceTime;
        if (process1 < process2) {
            return -1;
        }
        if (process1 > process2) {
            return 1;
        }
        return 0;
    }
}
