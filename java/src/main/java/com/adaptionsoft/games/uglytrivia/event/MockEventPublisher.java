package com.adaptionsoft.games.uglytrivia.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MockEventPublisher implements EventPublisher {
    @Getter
    private final List<Event> events = new ArrayList<>();
    private final List<EventListener> listeners = new ArrayList<>();

    @Override
    public void raise(Event... events) {
        raise(Arrays.asList(events));
    }

    @Override
    public void raise(Collection<Event> events) {
        this.events.addAll(events);
        events.forEach(event ->
                listeners.stream()
                        .filter(eventListener -> eventListener.accept(event))
                        .forEach(eventListener -> eventListener.handle(event))
        );
    }

    @Override
    public void register(EventListener listener) {
        this.listeners.add(listener);
    }

    public void clearEvents() {
        events.clear();
    }
}
