package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayDeque;
import java.util.Queue;

enum Questions {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock"),
    GEOGRAPHY("Geography"),
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

    public void clear() {
        cards.clear();
    }
}
