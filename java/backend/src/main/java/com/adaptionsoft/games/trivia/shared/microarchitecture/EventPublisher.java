package com.adaptionsoft.games.trivia.shared.microarchitecture;

public interface EventPublisher {

    void raise(Event... events);

    void register(EventListener eventListener);

    void flush();
}
