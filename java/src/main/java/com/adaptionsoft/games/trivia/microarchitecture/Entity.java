package com.adaptionsoft.games.trivia.microarchitecture;

import com.adaptionsoft.games.trivia.domain.event.Event;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Entity {
    @Getter
    protected final List<Event> uncommittedEvents = new ArrayList<>();

    protected void raise(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    public List<Event> getUncommittedEventsAndClear() {
        List<Event> eventsCopy = new ArrayList<>(uncommittedEvents);
        uncommittedEvents.clear();
        return eventsCopy;
    }
}
