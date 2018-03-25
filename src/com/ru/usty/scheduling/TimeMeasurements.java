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
	
	public long responseTime () {
		return this.onCPU - this.arrivalTime;
	}
	public long turnaroundTime () {
		return this.executionTime - this.arrivalTime;
	}
	
}
