package com.adaptionsoft.games.uglytrivia.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class Event {
    protected final String stringValue;

    public Event(String stringValue) {
        this.stringValue = stringValue;
    }
}
