package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class MockEventPublisher extends ObserverBasedEventPublisher implements EventPublisher {
    private final List<Event> events = new ArrayList<>();

    public void clearEvents() {
        events.clear();
    }

    @Override
    public void publish(Collection<Event> events) {
        super.publish(events);
        this.events.addAll(events);
    }

    @Override
    public void publish(Event... events) {
        publish(Arrays.asList(events));
    }
}
