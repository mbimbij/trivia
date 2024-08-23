package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;

public abstract class GameException extends BusinessException {

    protected final GameId gameId;

    protected GameException(GameId gameId, String message) {
        super(message);
        this.gameId = gameId;
    }
}
