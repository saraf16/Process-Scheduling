package com.ru.usty.scheduling;

import java.util.Comparator;

public class ShortestProcessNext  implements Comparator<Integer>{
	
	Scheduler SPNSchedule;
	
	public ShortestProcessNext(Scheduler SPNSchedule){
		this.SPNSchedule = SPNSchedule;
	}

    @Override
    public int compare(Integer x, Integer y)
    {

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
