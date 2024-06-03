package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;

public class GameCreatedEvent extends GameEvent {
    public GameCreatedEvent(GameId gameId) {
        super(gameId, "Game created");
    }
}
