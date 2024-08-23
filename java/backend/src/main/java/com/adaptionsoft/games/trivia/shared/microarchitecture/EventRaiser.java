package com.adaptionsoft.games.trivia.shared.microarchitecture;

import lombok.Getter;

@Getter
public class EventRaiser {
    protected final EventPublisher eventPublisher;

    public EventRaiser(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected void raise(Event... events) {
        eventPublisher.raise(events);
    }
}
