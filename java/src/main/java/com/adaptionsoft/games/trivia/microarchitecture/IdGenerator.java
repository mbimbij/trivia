package com.adaptionsoft.games.trivia.microarchitecture;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private AtomicInteger atomicInteger = new AtomicInteger();
    public Integer nextId() {
        return atomicInteger.getAndIncrement();
    }
}
