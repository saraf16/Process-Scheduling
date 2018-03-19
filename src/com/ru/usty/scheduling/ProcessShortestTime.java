package com.ru.usty.scheduling;

import java.util.Comparator;

public class ProcessShortestTime  implements Comparator<Integer>{
	
	Scheduler SPNSchedule;
	
	public ProcessShortestTime(Scheduler SPNSchedule){
		this.SPNSchedule = SPNSchedule;
	}

    @Override
    public int compare(Integer x, Integer y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
    		long process1 = SPNSchedule.processExecution.getProcessInfo(x).totalServiceTime;
    		long process2 = SPNSchedule.processExecution.getProcessInfo(y).totalServiceTime;
        if (process1 < process2) {
            return -1;
        }
        if (process1 > process2) {
            return 1;
        }
        return 0;
    }

}
