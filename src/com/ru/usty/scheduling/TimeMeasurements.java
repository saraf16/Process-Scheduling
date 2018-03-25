package com.ru.usty.scheduling;

import java.util.ArrayList;

public class TimeMeasurements {
	public long arrivalTime;
	public long onCPU;
	public long waitingTime;
	public long executionTime;	
	
	public TimeMeasurements(long arrivalTime){
		this.arrivalTime = arrivalTime;
		this.onCPU = 0;
	}
	
	public TimeMeasurements(long waitingTime, long executionTime){
		this.waitingTime = waitingTime;
		this.executionTime = executionTime;
	}
	
	public long responseTime () {
		return this.onCPU - this.arrivalTime;
	}
	public long turnaroundTime () {
		return this.onCPU - this.arrivalTime;
	}
	
}
