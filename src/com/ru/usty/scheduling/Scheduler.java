package com.ru.usty.scheduling;
import java.util.*;

import com.ru.usty.scheduling.process.ProcessExecution;

public class Scheduler {

	ProcessExecution processExecution;
	Policy policy;
	int quantum;
	boolean isOnCPU;

	Queue<Integer> queue;


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
		queue = new LinkedList<Integer>();


		switch(policy) {
		case FCFS:	//First-come-first-served
			System.out.println("Starting new scheduling task: First-come-first-served");

			break;
		case RR:	//Round robin
			System.out.println("Starting new scheduling task: Round robin, quantum = " + quantum);
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SPN:	//Shortest process next
			System.out.println("Starting new scheduling task: Shortest process next");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
			break;
		case SRT:	//Shortest remaining time
			System.out.println("Starting new scheduling task: Shortest remaining time");
			/**
			 * Add your policy specific initialization code here (if needed)
			 */
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
				System.out.println("er tomt " + processID);
				processExecution.switchToProcess(processID);
				isOnCPU = true;

				}
				else {
				System.out.println("adda í queue" + processID);
				queue.add(processID);
				}
				break;
			case RR:	//Round robin

				break;
			case SPN:	//Shortest process next
				System.out.println("Starting new scheduling task: Shortest process next");
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
			case SRT:	//Shortest remaining time
				System.out.println("Starting new scheduling task: Shortest remaining time");
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
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
				
				break;
			case SPN:	//Shortest process next
				System.out.println("Starting new scheduling task: Shortest process next");
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
				break;
			case SRT:	//Shortest remaining time
				System.out.println("Starting new scheduling task: Shortest remaining time");
				/**
				 * Add your policy specific initialization code here (if needed)
				 */
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



	}
}
