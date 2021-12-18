package com.example.server.server;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class Server implements Runnable {
    private int counter = 0;
    private ServerStatus serverStatus = ServerStatus.STOPPED;
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock writeLock = rwLock.writeLock();

    public void boot() {
        if (serverStatus != ServerStatus.STOPPED) {
            log.error("서버가 동작할 수 없습니다. ServerStatus - {}", this.serverStatus);
            throw new IllegalStateException("서버가 동작할 수 없습니다.");
        }
        this.serverStatus = ServerStatus.INITIALIZED;

        System.out.println("System Starting...");
        counter = 0;
    }

    public void process() {
        if (serverStatus != ServerStatus.INITIALIZED) {
            log.error("서버가 동작할 수 없습니다. ServerStatus - {}", this.serverStatus);
        }
        this.serverStatus = ServerStatus.RUNNING;


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

    private void terminating() {
        if (serverStatus != ServerStatus.RUNNING) {
            log.error("서버가 동작할 수 없습니다. ServerStatus - {}", this.serverStatus);
        }
        this.serverStatus = ServerStatus.TERMINATED;

        System.out.println("System terminating...");
    }

    public void shutdown() {
        if (serverStatus != ServerStatus.TERMINATED) {
            log.error("서버가 동작할 수 없습니다. ServerStatus - {}", this.serverStatus);
        }
        this.serverStatus = ServerStatus.EXITED;
        terminating();
        System.out.println("System Shutdown");
    }

    @Override
    public void run() {
        boot();
        process();
    }
}