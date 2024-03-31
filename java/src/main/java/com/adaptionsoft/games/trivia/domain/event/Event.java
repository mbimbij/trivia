package com.adaptionsoft.games.trivia.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString
public abstract class Event {
    // TODO remplacer l'order number par un timestamp et/ou un generator pour pouvoir mocker tout ça
    private static final AtomicInteger eventCounter = new AtomicInteger(0);

    protected final String stringValue;
    @EqualsAndHashCode.Exclude
    protected final int orderNumber;

    protected Event(String stringValue) {
        orderNumber = eventCounter.getAndIncrement();
        this.stringValue = stringValue;
    }
}
