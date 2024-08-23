package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.GameId;

public class GameStartedEvent extends GameEvent {
    public GameStartedEvent(GameId gameId) {
        super(gameId, "Game %s started".formatted(gameId));
    }
}
