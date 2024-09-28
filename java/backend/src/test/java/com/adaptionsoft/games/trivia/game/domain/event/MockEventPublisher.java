package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.shared.microarchitecture.Event;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MockEventPublisher extends ObserverBasedEventPublisher implements EventPublisher {
    private final List<Event> publishedEvents = new ArrayList<>();

    public void clearEvents() {
        publishedEvents.clear();
        uncommittedEvents.clear();
    }

    @Override
    public void flush() {
        publishedEvents.addAll(uncommittedEvents);
        super.flush();
    }
}
