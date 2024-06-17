package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;

public interface EventPublisher {

    void raise(Event... events);

    void publishImmediately(Event... events);

    void register(EventListener eventListener);

    void publishAndClearUncommittedEvents();
}
