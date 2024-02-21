package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;

import java.util.Collection;

public interface EventPublisher {

    void raise(Event... events);

    void raise(Collection<Event> events);

    void register(EventListener eventListener);
}
