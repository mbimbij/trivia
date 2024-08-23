package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.GameId;

public class GameCreatedEvent extends GameEvent {
    public GameCreatedEvent(GameId gameId) {
        super(gameId, "Game created");
    }
}
