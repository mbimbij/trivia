package com.adaptionsoft.games.trivia.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PlayTurnException extends PlayerException {
    private PlayTurnException(Integer gameId, Integer playerId, String message) {
        super(gameId, playerId, message);
    }

    public static PlayTurnException notCurrentPlayerException(Integer gameId, Integer playerId, Integer currentPlayerId) {
        String message = "game id=%d, player id=%d tried to play but it is not its turn. Current player is id=%d".formatted(gameId, playerId, currentPlayerId);
        return new PlayTurnException(gameId, playerId, message);
    }
}
