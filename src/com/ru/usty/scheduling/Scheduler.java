package com.ru.usty.scheduling;
import java.util.*;
import java.util.Comparator;
import java.util.concurrent.Semaphore;


import com.ru.usty.scheduling.process.ProcessExecution;
import com.ru.usty.scheduling.ShortestProcessNext;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	int processRunning;
	boolean isOnCPU;

	Queue<Integer> queue;
	Comparator<Integer> comparator;
	PriorityQueue<Integer> queueP;
	Thread rrThread;
    Semaphore queueMutex;


    //þráður frumstilur her settur hér


	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public Scheduler(ProcessExecution processExecution) {
		this.processExecution = processExecution;

		/**
		 * Add general initialization code here (if needed)
		 */
	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void startScheduling(Policy policy, int quantum) {

		this.policy = policy;
		this.quantum = quantum;
		this.isOnCPU = false;


		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");
			queue = new LinkedList<Integer>();
			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);

            queue = new LinkedList<Integer>();
            queueMutex = new Semaphore(1);
            this.rrThread = new Thread(newRunnable());
            rrThread.start();

            break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			comparator = new ShortestProcessNext(this);
			queueP = new PriorityQueue<Integer>(10, comparator);
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			comparator = new ShortestRemainingTime(this);
			queueP = new PriorityQueue<Integer>(10, comparator);
			break;
		case HRRN:	//Highest response ratio next
			System.out.println("Starting new scheduling task: Highest response ratio next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case FB:	//Feedback
			System.out.println("Starting new scheduling task: Feedback, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		}

		/**
		 * Add general scheduling or initialization code here (if needed)
		 */

	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processAdded(int processID) {

		switch(policy) {
			case FCFS:	//First-come-first-served
				if(queue.isEmpty() && isOnCPU == false){
					processExecution.switchToProcess(processID);
					isOnCPU = true;
				}
				else {
					queue.add(processID);
				}
				break;
			case RR:	//Round robin
                try {
                    queueMutex.acquire();
                        queue.add(processID);
                    queueMutex.release();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }


				break;
			case SPN:	//Shortest process next
				if(queueP.isEmpty() && isOnCPU == false){
					processExecution.switchToProcess(processID);
					isOnCPU = true;
				}
				else {
					queueP.add(processID);	
				}
				break;
			case SRT:	//Shortest remaining time
				/**
				 * process.getTotalServiceTime()) - process.getElapsedExecutionTime(),
				 */
				if(queueP.isEmpty() && isOnCPU == false){
					processExecution.switchToProcess(processID);
					processRunning = processID;
					isOnCPU = true;
				}
				else {
					System.out.println("ProcessID -> " + processID);
					System.out.println("processRunning -> " + processRunning);
					if (comparator.compare(processID, processRunning) < 0) {
						processExecution.switchToProcess(processID);
						queueP.add(processRunning);
						processRunning = processID;
					}
					else {
						queueP.add(processID);
					}	
				}
				break;
			case HRRN:	//Highest response ratio next
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
			case FB:	//Feedback
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
		}


	}

	/**
	 * DO NOT CHANGE DEFINITION OF OPERATION
	 */
	public void processFinished(int processID) {
		switch(policy) {
			case FCFS:	//First-come-first-served
				System.out.println("Finish " + processID);
				if(!queue.isEmpty() && isOnCPU == true ){
					int newprocess = queue.remove();
					processExecution.switchToProcess(newprocess);
				}
				else {
					isOnCPU = false;
				}
				break;
			case RR:	//Round robin

                try{
                    if(queue.isEmpty() && isOnCPU == false){
                        rrThread.join();

                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }


				break;
			case SPN:	//Shortest process next
				System.out.println("Finish " + processID);
				if(!queueP.isEmpty() && isOnCPU == true){
					int newprocess = queueP.remove();
					processExecution.switchToProcess(newprocess);
				}
				else {
					isOnCPU = false;
				}
				break;
			case SRT:	//Shortest remaining time
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				System.out.println("Finish " + processID);
				if(!queueP.isEmpty() && isOnCPU == true){
					int newprocess = queueP.remove();
					processExecution.switchToProcess(newprocess);
					processRunning = newprocess;
				}
				else {
					isOnCPU = false;
				}
				break;
			case HRRN:	//Highest response ratio next
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
			case FB:	//Feedback
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
		}
	}

    private  Runnable newRunnable (){
        return new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!queue.isEmpty()){
                        processRunning = queue.remove();
                        processExecution.switchToProcess(processRunning);
                        System.out.println("current systemtime -> " + System.currentTimeMillis());
                        isOnCPU = true;

                        try {
                            Thread.sleep(quantum);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            queueMutex.acquire();
                            queue.add(processRunning);
                            queueMutex.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    //if(queue.isEmpty() && isOnCPU == false){
                    //    break;
                    //}
                }
            }
        };
    }
}
