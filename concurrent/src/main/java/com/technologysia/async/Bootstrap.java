package com.technologysia.async;

public class Bootstrap {
    private Wrapper doWork(Wrapper wrapper){
        new Thread(() -> {
            Worker worker = wrapper.getWorker();
            String result = worker.action(wrapper.getParam());
            wrapper.getListener().result(result);
        }).start();
        return wrapper;
    }

    private Worker newWorker(){
        return object -> object + "world!";
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        Worker worker = bootstrap.newWorker();

        Wrapper wrapper = new Wrapper();
        wrapper.setWorker(worker);
        wrapper.setParam("hello");

        bootstrap.doWork(wrapper).setListener(result -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(result);
        });

        System.out.println(Thread.currentThread().getName());
    }
}
