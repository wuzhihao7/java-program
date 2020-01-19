package com.technologysia.pc;

import java.util.Vector;

public class PC1 {
    public static void main(String[] args) {
        Client c = new Client();
        Server s = new Server(c);
        c.start();
        s.start();
    }
}

class Client extends Thread {
    Vector queue;
    public Client() {
        this.queue = new Vector();
    }
    @Override
    public void run() {
        while (true) {
            if (! (queue.size() == 0)) {
                processNextElement();
            }
        }
    }
    private void processNextElement() {
        Object next = queue.elementAt(0);
        queue.removeElementAt(0);
        System.out.println(next);
    }
}
class Server extends Thread {
    Client client;
    int counter;

    public Server(Client _client) {
        this.client = _client;
        this.counter = 0;
    }

    @Override
    public void run() {
        while (counter < 10) {
            this.client.queue.addElement(counter);
            counter++;
        }
        throw new RuntimeException("counter >= 10");
    }
}