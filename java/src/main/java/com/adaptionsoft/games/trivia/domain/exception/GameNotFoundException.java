package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.GameId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends GameException {
    public GameNotFoundException(GameId gameId) {
        super(gameId, "game id=%s was not found".formatted(gameId));
    }
}
