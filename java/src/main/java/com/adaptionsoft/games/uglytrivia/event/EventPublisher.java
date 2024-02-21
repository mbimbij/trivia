package com.adaptionsoft.games.uglytrivia.event;

import java.util.Collection;

public interface EventPublisher {

    void raise(Event... events);

    void raise(Collection<Event> events);

    void register(EventListener eventListener);
}
