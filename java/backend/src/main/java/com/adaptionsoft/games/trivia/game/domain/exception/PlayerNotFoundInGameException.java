package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;

public class PlayerNotFoundInGameException extends PlayerException {
    public PlayerNotFoundInGameException(GameId gameId, UserId playerId) {
        super(gameId, playerId, "user %s was not found in the game id=%s".formatted(playerId, gameId));
    }
}
