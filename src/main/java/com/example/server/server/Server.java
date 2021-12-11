package com.example.server.server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server implements Runnable {
    private int counter = 0;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock writeLock = rwLock.writeLock();

    public void boot() {
        System.out.println("System Starting...");
        counter = 0;
    }

    public void execute() {
        System.out.println("Executing...");
        System.out.println("starting counter is: " + counter);
        for (int i=0; i < 4; i++) {
            writeLock.lock();

            try {
                incrementCounter();
                Thread.sleep(1000);
                decreaseCounter();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }
        System.out.println("ending counter: " + counter + " & thread safe:" + (counter == 0));
    }

    private void incrementCounter() {
        counter++;
    }

    private void decreaseCounter() {
        counter--;
    }

    public void terminate() {
        System.out.println("System terminated");
    }

    @Override
    public void run() {
        boot();
        execute();
        terminate();
    }
}
