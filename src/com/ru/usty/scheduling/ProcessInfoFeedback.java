package com.ru.usty.scheduling;

public class ProcessInfoFeedback {
    int queueCounter;
    int processID;

    public ProcessInfoFeedback(int ProcessID) {
        queueCounter = 1;
        this.processID = ProcessID;
    }

    public void incrementQueueCounter() {
        if(queueCounter < 7) {
            queueCounter++;
        }
    }

    public int getQueueForProcess() {
        return queueCounter;
    }
}