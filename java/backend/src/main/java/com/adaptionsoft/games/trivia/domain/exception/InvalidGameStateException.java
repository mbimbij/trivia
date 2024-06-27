package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.State;

public class InvalidGameStateException extends GameException {
    public InvalidGameStateException(GameId gameId, State gameState, String action){
        super(gameId, "Game=%s: Action '%s' is illegal for state='%s'".formatted(gameId, action, gameState));
    }
}
