package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RollDiceException extends PlayerException {
    public RollDiceException(GameId gameId, UserId playerId) {
        super(gameId, playerId, "game id=%s, player id=%s tried to roll the dice but just gave an incorrect answer and can only answer the question asked".formatted(gameId, playerId));
    }
}
