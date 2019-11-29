package com.technologysia.strategy;

/**
 * 实现策略接口-减法
 */
public class OperationSubstract implements Strategy {
    @Override
    public int doOperation(int num1, int num2) {
        return num1 - num2;
    }
}
