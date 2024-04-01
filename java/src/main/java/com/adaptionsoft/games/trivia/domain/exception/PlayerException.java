package com.adaptionsoft.games.trivia.domain.exception;

import lombok.Getter;

@Getter
public class PlayerException extends BusinessException {
    private final Integer gameId;
    private final Integer playerId;

    public PlayerException(Integer gameId, Integer playerId, String message) {
        super(message);
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
