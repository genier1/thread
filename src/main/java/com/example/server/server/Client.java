package com.example.server.server;

public class Client  {
    public static void main(String[] args) {
        Server server = new Server();
        Thread[] threads = new Thread[10];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(server);
            threads[i].start();
        }
    }
}
