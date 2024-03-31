package com.adaptionsoft.games.trivia.domain.event;

import lombok.Getter;

@Getter
public class GameEvent extends Event {
    private final Integer gameId;

    protected GameEvent(Integer gameId, String stringValue) {
        super(stringValue);
        this.gameId = gameId;
    }
}
