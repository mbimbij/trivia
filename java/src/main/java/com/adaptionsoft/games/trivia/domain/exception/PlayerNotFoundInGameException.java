package com.adaptionsoft.games.trivia.domain.exception;

public class PlayerNotFoundInGameException extends PlayerException {
    public PlayerNotFoundInGameException(Integer gameId, Integer playerId) {
        super(gameId, playerId, "user %d was not found in the game id=%d".formatted(playerId, gameId));
    }
}
