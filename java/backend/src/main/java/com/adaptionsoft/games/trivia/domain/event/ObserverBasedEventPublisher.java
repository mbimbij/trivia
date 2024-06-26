package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.microarchitecture.EventListener;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ObserverBasedEventPublisher implements EventPublisher {
    private final List<EventListener> listeners = new ArrayList<>();
    protected final List<Event> uncommittedEvents = new ArrayList<>();

    @Override
    public void raise(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    public void flush(Collection<Event> events) {
        events.forEach(event ->
                listeners.stream()
                        .filter(eventListener -> eventListener.accept(event))
                        .forEach(eventListener -> eventListener.handle(event))
        );
    }

    public void register(EventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void flushEvents() {
        flush(uncommittedEvents);
        uncommittedEvents.clear();
    }
}
