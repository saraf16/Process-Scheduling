package com.ru.usty.scheduling;

public class ProcessInfoFeedback {
    int queueCounter;
    int processID;

    public ProcessInfoFeedback(int ProcessID) {
        queueCounter = 0;
        this.processID = ProcessID;
    }

    public void incrementQueueCounter() {
        queueCounter++;
    }

    public int getQueueForProcess() {
        return queueCounter;
    }
}