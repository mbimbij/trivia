package com.adaptionsoft.games.uglytrivia.event;

public interface EventListener {
    boolean accept(Event event);

    void handle(Event event);
}
