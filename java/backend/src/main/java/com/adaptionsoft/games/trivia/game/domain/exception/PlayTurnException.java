package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PlayTurnException extends PlayerException {
    private PlayTurnException(GameId gameId, UserId playerId, String message) {
        super(gameId, playerId, message);
    }

    public static PlayTurnException notCurrentPlayerException(GameId gameId, UserId playerId, UserId currentPlayerId) {
        String message = "game id=%s, player id=%s tried to play but it is not its turn. Current player is id=%s".formatted(gameId, playerId, currentPlayerId);
        return new PlayTurnException(gameId, playerId, message);
    }
}
