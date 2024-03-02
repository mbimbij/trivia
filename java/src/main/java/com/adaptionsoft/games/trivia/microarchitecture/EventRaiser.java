package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class EventRaiser {

    protected final List<Event> uncommittedEvents = new ArrayList<>();

    protected void raise(Event... events) {
        raise(Arrays.asList(events));
    }

    protected void raise(Collection<Event> events) {
        uncommittedEvents.addAll(events);
    }

    public List<Event> getAndClearUncommittedEvents() {
        List<Event> eventsCopy = new ArrayList<>(uncommittedEvents);
        uncommittedEvents.clear();
        return eventsCopy;
    }
}
