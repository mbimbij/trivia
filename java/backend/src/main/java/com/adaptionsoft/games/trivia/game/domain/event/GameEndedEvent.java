package com.adaptionsoft.games.trivia.game.domain.event;

import com.adaptionsoft.games.trivia.game.domain.GameId;

public class GameEndedEvent extends GameEvent {
    public GameEndedEvent(GameId gameId, String playerName) {
        super(gameId,"game %s ended with winner %s".formatted(gameId, playerName));
    }
}
