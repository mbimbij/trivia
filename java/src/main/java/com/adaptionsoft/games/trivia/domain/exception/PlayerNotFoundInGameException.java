package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.UserId;

public class PlayerNotFoundInGameException extends PlayerException {
    public PlayerNotFoundInGameException(GameId gameId, UserId playerId) {
        super(gameId, playerId, "user %s was not found in the game id=%s".formatted(playerId, gameId));
    }
}
