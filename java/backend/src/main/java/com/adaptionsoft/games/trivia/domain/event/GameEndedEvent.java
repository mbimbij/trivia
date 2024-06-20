package com.adaptionsoft.games.trivia.domain.event;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.Player;
import com.adaptionsoft.games.trivia.domain.UserId;

public class GameEndedEvent extends GameEvent {
    public GameEndedEvent(GameId gameId, String playerName) {
        super(gameId,"game %s ended with winner %s".formatted(gameId, playerName));
    }
}
