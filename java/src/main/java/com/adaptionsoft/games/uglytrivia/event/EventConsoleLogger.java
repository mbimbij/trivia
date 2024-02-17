package com.adaptionsoft.games.uglytrivia.event;

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
