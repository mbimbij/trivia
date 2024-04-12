package com.adaptionsoft.games.trivia.domain.exception;

public abstract class GameException extends BusinessException {

    protected final Integer gameId;

    protected GameException(Integer gameId, String message) {
        super(message);
        this.gameId = gameId;
    }
}
