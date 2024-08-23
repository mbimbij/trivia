package com.adaptionsoft.games.trivia.shared.microarchitecture;

import java.util.concurrent.atomic.AtomicInteger;

// TODO distinguer id métier et technique, ici il s'agit de l'id métier. L'id technique est géré dans la couche data
public class IdGenerator {
    private AtomicInteger atomicInteger = new AtomicInteger();
    public Integer nextId() {
        return atomicInteger.getAndIncrement();
    }
}
