package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;

public class CannotDrawQuestionBeforeRollingDiceException extends PlayerException {
    public CannotDrawQuestionBeforeRollingDiceException(GameId gameId, UserId playerId) {
        super(gameId,
                playerId,
                "game id=%s, player id=%s tried to draw a question before rolling the dice"
                        .formatted(gameId, playerId)
        );
    }
}
