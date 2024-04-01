package com.adaptionsoft.games.trivia.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends GameException {
    public GameNotFoundException(Integer gameId) {
        super(gameId, "game id=%d was not found".formatted(gameId));
    }
}
