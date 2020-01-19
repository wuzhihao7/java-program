package com.technologysia.pc;

import java.util.Vector;

public class PC2 {
    public static void main(String[] args) {
        Client2 c = new Client2();
        Server2 s = new Server2(c);
        c.start();
        s.start();
    }
}

class Server2 extends Thread {
    Client2 client;
    int counter;

    public Server2(Client2 _client) {
        this.client = _client;
        this.counter = 0;
    }

    @Override
    public void run() {
        try {
            while (counter < 10) {
                this.client.queue.addElement(new Integer(counter));
                counter++;
            }
            throw new RuntimeException("counter >= 10");
        } catch (Exception e) {
            this.client.interruptFlag = true;
            throw new RuntimeException(e.toString());
        }
    }
}

class Client2 extends Thread {
    Vector queue;
    boolean interruptFlag;

    public Client2() {
        this.queue = new Vector();
        this.interruptFlag = false;
    }

    @Override
    public void run() {
        while (!interruptFlag) {
            if (!(queue.size() == 0)) {
                processNextElement();
            }
        }
// Processes whatever elements remain on the queue before exiting.
        while (!(queue.size() == 0)) {
            processNextElement();
        }
        System.out.flush();
    }

    private void processNextElement() {
        Object next = queue.elementAt(0);
        queue.removeElementAt(0);
        System.out.println(next);
    }
}