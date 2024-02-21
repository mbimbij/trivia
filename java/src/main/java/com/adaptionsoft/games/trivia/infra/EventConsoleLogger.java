package com.adaptionsoft.games.trivia.infra;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.domain.event.PlayerEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventListener;

public class EventConsoleLogger implements EventListener {
    @Override
    public boolean accept(Event event) {
        return event instanceof PlayerEvent;
    }

    @Override
    public void handle(Event event) {
        System.out.println(event.getStringValue());
    }
}
