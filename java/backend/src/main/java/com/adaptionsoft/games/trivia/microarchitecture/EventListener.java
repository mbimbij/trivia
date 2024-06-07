package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;

public interface EventListener {
    boolean accept(Event event);

    void handle(Event event);
}
