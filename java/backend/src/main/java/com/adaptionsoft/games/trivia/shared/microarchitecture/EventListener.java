package com.adaptionsoft.games.trivia.shared.microarchitecture;

public interface EventListener {
    boolean accept(Event event);

    void handle(Event event);
}
