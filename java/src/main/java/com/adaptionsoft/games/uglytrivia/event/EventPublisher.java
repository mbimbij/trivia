package com.adaptionsoft.games.uglytrivia.event;

import java.util.Collection;
import java.util.List;

public interface EventPublisher {

    void publish(Event... events);

    void publish(Collection<Event> events);

    void register(EventListener eventListener);
}
