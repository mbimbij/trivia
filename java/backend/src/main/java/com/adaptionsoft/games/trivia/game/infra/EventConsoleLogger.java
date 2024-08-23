package com.adaptionsoft.games.trivia.game.infra;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;

public class EventConsoleLogger implements EventListener {
    @Override
    public boolean accept(Event event) {
        return true;
    }

    @Override
    public void handle(Event event) {
        System.out.println(event.getStringValue());
    }
}
