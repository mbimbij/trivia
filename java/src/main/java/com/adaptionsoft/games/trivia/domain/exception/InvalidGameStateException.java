package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.Game;

public class InvalidGameStateException extends GameException {
    public InvalidGameStateException(Integer gameId, Game.State gameState, String action){
        super(gameId, "Game=%d: Action '%s' is illegal for state='%s'".formatted(gameId, action, gameState));
    }
}
