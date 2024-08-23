package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ExecuteActionInPenaltyBoxException extends PlayerException {
    public ExecuteActionInPenaltyBoxException(GameId gameId, Player player, String actionName) {
        super(gameId, player.getId(), "game id=%s, player id=%s cannot execute action '%s' while in penalty box".formatted(gameId, player.getName(), actionName));
    }
}
