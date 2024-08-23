package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;

public class CannotUpdateLocationFromPenaltyBoxException extends PlayerException {
    public CannotUpdateLocationFromPenaltyBoxException(GameId gameId, UserId playerId) {
        super(gameId,
                playerId,
                "game id=%s, player id=%s tried to update location but is in penalty box"
                        .formatted(gameId, playerId)
        );
    }
}
