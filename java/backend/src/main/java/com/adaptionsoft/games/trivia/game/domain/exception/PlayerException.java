package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;
import lombok.Getter;

@Getter
public abstract class PlayerException extends BusinessException {
    private final GameId gameId;
    private final UserId playerId;

    public PlayerException(GameId gameId, UserId playerId, String message) {
        super(message);
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
