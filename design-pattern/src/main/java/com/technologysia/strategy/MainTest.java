package com.technologysia.strategy;

public class MainTest {
    public static void main(String[] args) {
        Context add = new Context(new OperationAdd());
        System.out.println(add.executeStrategy(1, 2));
        Context substract = new Context(new OperationSubstract());
        System.out.println(substract.executeStrategy(1,2));
    }
}
