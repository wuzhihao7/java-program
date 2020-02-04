package com.technologysia.future.futuretask;

public interface Computable<A, V> {
    V compute(A arg) throws InterruptedException;
}
