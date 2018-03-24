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

