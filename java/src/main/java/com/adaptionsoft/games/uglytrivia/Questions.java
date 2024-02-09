package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayDeque;
import java.util.Queue;

public enum Questions {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock"),
    ;
    private final Queue<String> cards = new ArrayDeque<>();
    private final String value;

    Questions(String value) {
        this.value = value;
    }

    public void stackCard(String content) {
        cards.add(content);
    }

    public String drawCard() {
        return cards.remove();
    }

    @Override
    public String toString() {
        return value;
    }
}
