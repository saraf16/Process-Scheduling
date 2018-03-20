package com.ru.usty.scheduling;

import java.util.Comparator;

public class ShortestRemainingTime  implements Comparator<Integer>{
	
	Scheduler SRTSchedule;
	
	public ShortestRemainingTime(Scheduler SRTSchedule){
		this.SRTSchedule = SRTSchedule;
	}

    @Override
    public int compare(Integer x, Integer y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
    		// process.getTotalServiceTime()) - process.getElapsedExecutionTime()
    		long process1 = SRTSchedule.processExecution.getProcessInfo(x).totalServiceTime - SRTSchedule.processExecution.getProcessInfo(x).elapsedExecutionTime;
    		long process2 = SRTSchedule.processExecution.getProcessInfo(y).totalServiceTime - SRTSchedule.processExecution.getProcessInfo(y).elapsedExecutionTime;
        if (process1 < process2) {
            return -1;
        }
        if (process1 > process2) {
            return 1;
        }
        return 0;
    }
}

