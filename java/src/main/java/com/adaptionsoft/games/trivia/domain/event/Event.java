package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@EqualsAndHashCode
@ToString
public abstract class Event {
    /**
     * TODO repenser la logique d'ordre des events: remplacer l'order number par un timestamp et/ou un generator
     * pour pouvoir mocker tout ça, plus gestion de la concurrence
     */
    private static final AtomicInteger eventCounter = new AtomicInteger(0);

    protected final String stringValue;
    @EqualsAndHashCode.Exclude
    protected final int orderNumber;
    // TODO remove the setter after the refacto R-1
    @Setter
    protected GameId gameId;

    protected Event(GameId gameId, String stringValue) {
        orderNumber = eventCounter.getAndIncrement();
        this.gameId = gameId;
        this.stringValue = stringValue;
    }
}
