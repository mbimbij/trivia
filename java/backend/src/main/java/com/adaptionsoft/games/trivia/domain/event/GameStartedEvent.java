package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;

public class GameStartedEvent extends GameEvent {
    public GameStartedEvent(GameId gameId) {
        super(gameId, "Game %s started".formatted(gameId));
    }
}
