package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.GameState;

public class InvalidGameStateException extends GameException {
    public InvalidGameStateException(GameId gameId, GameState gameState, String action){
        super(gameId, "Game=%s: Action '%s' is illegal for state='%s'".formatted(gameId, action, gameState));
    }
}
