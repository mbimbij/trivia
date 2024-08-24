package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventListener;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ObserverBasedEventPublisher implements EventPublisher {
    private final List<EventListener> listeners = new ArrayList<>();
    protected final List<Event> uncommittedEvents = new ArrayList<>();

    public ObserverBasedEventPublisher() {
        System.out.println();
    }

    @Override
    public void raise(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    private void notifyListeners(Collection<Event> events) {
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
    public void flush() {
        notifyListeners(uncommittedEvents);
        uncommittedEvents.clear();
    }
}
