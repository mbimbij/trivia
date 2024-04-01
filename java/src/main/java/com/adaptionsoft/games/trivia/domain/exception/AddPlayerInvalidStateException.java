package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.Game;

public class AddPlayerInvalidStateException extends GameException {
    public AddPlayerInvalidStateException(Integer gameId, Game.State gameState) {
        super(gameId, "Tried to add player for game=%d with state='%s'".formatted(gameId, gameState));
    }
}
