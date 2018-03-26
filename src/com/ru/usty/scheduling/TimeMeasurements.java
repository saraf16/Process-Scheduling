package com.ru.usty.scheduling;

import java.util.ArrayList;

public class TimeMeasurements {
	public long arrivalTime;
	public long onCPU;
	public long waitingTime;
	public long executionTime;	
	public boolean RRcheck;
	public boolean fCheck;
	
	public TimeMeasurements(long arrivalTime){
		this.arrivalTime = arrivalTime;
		this.onCPU = 0;
		this.executionTime = 0;
		this.RRcheck = false;
		this.fCheck = false;
	}
	
	public long responseTime () {
		return this.onCPU - this.arrivalTime;
	}
	public long turnaroundTime () {
		return this.executionTime - this.arrivalTime;
	}
	
}
