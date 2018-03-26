package com.ru.usty.scheduling;

import java.util.*;
import java.util.concurrent.Semaphore;

import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.ShortestProcessNext;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	int processRunning;
	int counter;
	boolean isOnCPU;
	boolean runThread;
	boolean kill;

	Queue<Integer> queue;
	Queue<ProcessInfoFeedback> queue1, queue2, queue3, queue4, queue5, queue6, queue7;
	Comparator<Integer> comparator;
	PriorityQueue<Integer> queueP;
	ArrayList<Integer> finishArray;
	TimeMeasurements timeMeasurements;
	ArrayList<Long> responseTimes;
	ArrayList<Long> turnaroundTimes;
	Thread rrThread;
	Thread fThread;
	Semaphore queueMutex;
	Semaphore arrayMutex;
	long lastStartTime;
	ProcessInfoFeedback processInfoFB;

	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

	}

	public void startScheduling(Policy policy, int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		this.isOnCPU = false;
		
		switch (policy) {
		case FCFS: // First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			responseTimes = new ArrayList<Long>();
			turnaroundTimes  = new ArrayList<Long>();
			queue = new LinkedList<Integer>();
			break;
		case RR: // Round robin
			if (rrThread != null && rrThread.isAlive()) {
				System.out.println("Thread is dying !!!");
				try {
					kill = true;
					rrThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			kill = false;
			
			responseTimes = new ArrayList<Long>();
			turnaroundTimes  = new ArrayList<Long>();
			
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			queue = new LinkedList<Integer>();
			finishArray = new ArrayList<Integer>();
			queueMutex = new Semaphore(1);
			arrayMutex = new Semaphore(1);
			this.rrThread = new Thread(roundRobinRunnable());
			runThread = false;
			counter = 0;
			break;
		case SPN: // Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			responseTimes = new ArrayList<Long>();
			turnaroundTimes  = new ArrayList<Long>();
			comparator = new ShortestProcessNext(this);
			queueP = new PriorityQueue<Integer>(10, comparator);
			break;
		case SRT: // Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			responseTimes = new ArrayList<Long>();
			turnaroundTimes  = new ArrayList<Long>();
			comparator = new ShortestRemainingTime(this);
			queueP = new PriorityQueue<Integer>(10, comparator);
			break;
		case HRRN: // Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			responseTimes = new ArrayList<Long>();
			turnaroundTimes  = new ArrayList<Long>();
			comparator = new HighestResponseRatio(this);
			queueP = new PriorityQueue<Integer>(10, comparator);
			break;
		case FB: // Feedback
			if (fThread != null && fThread.isAlive()) {
				System.out.println("Thread is dying !!!");
				try {
					kill = true;
					fThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			kill = false;

			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			queue1= new LinkedList<ProcessInfoFeedback>();
			queue2= new LinkedList<ProcessInfoFeedback>();
			queue3= new LinkedList<ProcessInfoFeedback>();
			queue4= new LinkedList<ProcessInfoFeedback>();
			queue5= new LinkedList<ProcessInfoFeedback>();
			queue6= new LinkedList<ProcessInfoFeedback>();
			queue7= new LinkedList<ProcessInfoFeedback>();
			finishArray = new ArrayList<Integer>();
			queueMutex = new Semaphore(1);
			arrayMutex = new Semaphore(1);
			this.fThread = new Thread(feedbackRunnable());
			runThread = false;
			break;
		}


	}

	public void processAdded(int processID) {

		switch (policy) {
		case FCFS: // First-come-first-served
			timeMeasurements = new TimeMeasurements(System.currentTimeMillis());
			if (queue.isEmpty() && isOnCPU == false) {
				processExecution.switchToProcess(processID);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
				isOnCPU = true;
			} else {
				queue.add(processID);
			}
			break;
		case RR: // Round robin
			if (runThread == false) {
				rrThread.start();
				runThread = true;
			}
			timeMeasurements = new TimeMeasurements(System.currentTimeMillis());
			try {
				queueMutex.acquire();
				queue.add(processID);
				queueMutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case SPN: // Shortest process next
			timeMeasurements = new TimeMeasurements(System.currentTimeMillis());
			if (queueP.isEmpty() && isOnCPU == false) {
				processExecution.switchToProcess(processID);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
				isOnCPU = true;
			} else {
				queueP.add(processID);
			}
			break;
		case SRT: // Shortest remaining time
			timeMeasurements = new TimeMeasurements(System.currentTimeMillis());
			if (queueP.isEmpty() && isOnCPU == false) {
				processExecution.switchToProcess(processID);
				processRunning = processID;
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
				isOnCPU = true;
			} else {
				System.out.println("ProcessID -> " + processID);
				System.out.println("processRunning -> " + processRunning);
				if (comparator.compare(processID, processRunning) < 0) {
					processExecution.switchToProcess(processID);
					queueP.add(processRunning);
					timeMeasurements.onCPU = System.currentTimeMillis();
					responseTimes.add(timeMeasurements.responseTime());
					processRunning = processID;
				} else {
					queueP.add(processID);
				}
			}
			break;
		case HRRN: // Highest response ratio next
			timeMeasurements = new TimeMeasurements(System.currentTimeMillis());
			if (queueP.isEmpty() && isOnCPU == false) {
				processExecution.switchToProcess(processID);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
				isOnCPU = true;
			} else {
				queueP.add(processID);
			}
			break;
		case FB: // Feedback
			processInfoFB = new ProcessInfoFeedback(processID);
			if (runThread == false) {
				fThread.start();
				runThread = true;
			}
			try {
				queueMutex.acquire();
				queue1.add(processInfoFB);
				queueMutex.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		

	}

	public void processFinished(int processID) {
		switch (policy) {
		case FCFS: // First-come-first-served
			System.out.println("Finish " + processID);
			timeMeasurements.executionTime = System.currentTimeMillis();
			turnaroundTimes.add(timeMeasurements.turnaroundTime());
			if (!queue.isEmpty() && isOnCPU == true) {
				int newprocess = queue.remove();
				processExecution.switchToProcess(newprocess);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
			} else {
				isOnCPU = false;
			}
			
			if (queue.isEmpty() && isOnCPU == false){
				calculateTimes();
			}
			
			break;
		case RR: // Round robin
			System.out.println("Finish " + processID);
			finishArray.add(processID);
			timeMeasurements.executionTime = System.currentTimeMillis();
			turnaroundTimes.add(timeMeasurements.turnaroundTime());
			counter++;
			
			if(counter == 15) {
				calculateTimes();
			}
			
			break;
		case SPN: // Shortest process next
			System.out.println("Finish " + processID);
			timeMeasurements.executionTime = System.currentTimeMillis();
			turnaroundTimes.add(timeMeasurements.turnaroundTime());
			if (!queueP.isEmpty() && isOnCPU == true) {
				int newprocess = queueP.remove();
				processExecution.switchToProcess(newprocess);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
			} else {
				isOnCPU = false;
			}
			
			if (queue.isEmpty() && isOnCPU == false){
				calculateTimes();
			}
			break;
		case SRT: // Shortest remaining time
			System.out.println("Finish " + processID);
			timeMeasurements.executionTime = System.currentTimeMillis();
			turnaroundTimes.add(timeMeasurements.turnaroundTime());
			if (!queueP.isEmpty() && isOnCPU == true) {
				int newprocess = queueP.remove();
				processExecution.switchToProcess(newprocess);
				processRunning = newprocess;
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
			} else {
				isOnCPU = false;
			}
			if (queueP.isEmpty() && isOnCPU == false){
				calculateTimes();
			}
			break;
		case HRRN: // Highest response ratio next
			System.out.println("Finish " + processID);
			timeMeasurements.executionTime = System.currentTimeMillis();
			turnaroundTimes.add(timeMeasurements.turnaroundTime());
			if (!queueP.isEmpty() && isOnCPU == true) {
				int newprocess = queueP.remove();
				processExecution.switchToProcess(newprocess);
				timeMeasurements.onCPU = System.currentTimeMillis();
				responseTimes.add(timeMeasurements.responseTime());
			} else {
				isOnCPU = false;
			}
			if (queueP.isEmpty() && isOnCPU == false){
				calculateTimes();
			}
			break;
		case FB: // Feedback
			finishArray.add(processID);
			break;
		}
	}
	
	void calculateTimes() {
		long sum1 = 0;
		long sum2 = 0;
		for(long t: responseTimes) {
			sum1 += t;
		}
		for(long t: turnaroundTimes) {
			sum2 += t;
		}
		
		System.out.println("Average response time for " + this.policy + ": " + sum1/responseTimes.size());
		System.out.println("Average turnaround time for " + this.policy + ": " + sum2/turnaroundTimes.size());
	}

	
	private Runnable feedbackRunnable() {
		// TODO Auto-generated method stub
		return new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						queueMutex.acquire();
						if(!queue1.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue1.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}

						}
						else if(!queue2.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue2.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}

						}
						else if(!queue3.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue3.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}


						}
						else if(!queue4.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue4.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}


						}
						else if(!queue5.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue5.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}


						}
						else if(!queue6.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue6.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}

						}
						else if(!queue7.isEmpty()) {
							try {
								isOnCPU = true;
								processInfoFB = queue7.remove();
							}
							catch (NullPointerException e) {
								System.out.println("villa" + e);
							}

						}
						queueMutex.release();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					processExecution.switchToProcess(processInfoFB.processID);
					lastStartTime = System.currentTimeMillis();


					long quantumCheck = lastStartTime + quantum;
					while((!finishArray.contains(processInfoFB.processID))){
						if(System.currentTimeMillis() >= quantumCheck){
							break;
						}
					}
					processInfoFB.incrementQueueCounter();

					try {
						queueMutex.acquire();
						int count =  processInfoFB.getQueueForProcess();
						if (!finishArray.contains(processInfoFB.processID)) {
							switch (count) {
								case 1:
									queue1.add(processInfoFB);
									break;
								case 2:
									queue2.add(processInfoFB);
									break;
								case 3:
									queue3.add(processInfoFB);
									break;
								case 4:
									queue4.add(processInfoFB);
									break;
								case 5:
									queue5.add(processInfoFB);
									break;
								case 6:
									queue6.add(processInfoFB);
									break;
								case 7:
									queue7.add(processInfoFB);
									break;
							}
						}
						queueMutex.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (kill) {
						System.out.println("Thread is dying !#%$&&!");
						break;
					}
				}
			}
		};
	}
	
	
	
	
	
	private Runnable roundRobinRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						queueMutex.acquire();
						if (!queue.isEmpty()) {
							isOnCPU = true;
							try {
								processRunning = queue.remove();
								if(!timeMeasurements.RRcheck) {
									timeMeasurements.onCPU = System.currentTimeMillis();
									responseTimes.add(timeMeasurements.responseTime());
									timeMeasurements.RRcheck = true;
								}
								//timeMeasurements.onCPU = System.currentTimeMillis();
								//responseTimes.add(timeMeasurements.responseTime());
							} catch (NullPointerException e) {
								System.out.println("villa" + e);
							}
							processExecution.switchToProcess(processRunning);
							lastStartTime = System.currentTimeMillis();
						}
						queueMutex.release();

					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					long quantumCheck = lastStartTime + quantum;
					while((!finishArray.contains(processRunning))){
						if(System.currentTimeMillis() >= quantumCheck){
							break;
						}
					}
					
					try {
						queueMutex.acquire();
						if (!finishArray.contains(processRunning)) {
							queue.add(processRunning);
						}
						queueMutex.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if (kill) {
						System.out.println("Thread is dying !#%$&&!");
						//calculateTimes();
						break;
					}
				}
			}
		};
	}
}
