package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.GameId;
import com.adaptionsoft.games.trivia.domain.UserId;

public class CannotAnswerQuestionBeforeDrawingOneException extends PlayerException {
    public CannotAnswerQuestionBeforeDrawingOneException(GameId gameId, UserId playerId) {
        super(gameId,
                playerId,
                "game id=%s, player id=%s tried to answer a question before drawing one"
                        .formatted(gameId, playerId)
        );
    }
}
