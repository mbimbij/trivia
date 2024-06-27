package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class MockEventPublisher extends ObserverBasedEventPublisher implements EventPublisher {
    private final List<Event> publishedEvents = new ArrayList<>();

    public void clearEvents() {
        publishedEvents.clear();
        uncommittedEvents.clear();
    }

    @Override
    public void flush(Collection<Event> events) {
        super.flush(events);
        this.publishedEvents.addAll(events);
    }

}
