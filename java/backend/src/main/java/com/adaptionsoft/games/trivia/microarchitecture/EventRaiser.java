package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
