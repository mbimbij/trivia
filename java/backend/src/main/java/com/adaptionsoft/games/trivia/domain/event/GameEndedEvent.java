package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.UserId;

public class GameEndedEvent extends GameEvent {
    public GameEndedEvent(GameId gameId, UserId winnerId) {
        super(gameId,"game %s ended with winner %s".formatted(gameId, winnerId));
    }
}
