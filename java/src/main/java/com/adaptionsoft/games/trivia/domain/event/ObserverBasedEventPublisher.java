package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.microarchitecture.EventListener;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ObserverBasedEventPublisher implements EventPublisher {
    private final List<EventListener> listeners = new ArrayList<>();

    public void publish(Event... events) {
        publish(Arrays.asList(events));
    }

    public void publish(Collection<Event> events) {
        events.forEach(event ->
                listeners.stream()
                        .filter(eventListener -> eventListener.accept(event))
                        .forEach(eventListener -> eventListener.handle(event))
        );
    }

    public void register(EventListener listener) {
        this.listeners.add(listener);
    }
}
