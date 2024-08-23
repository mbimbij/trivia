package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.GameId;
import com.adaptionsoft.games.trivia.game.domain.UserId;
import com.adaptionsoft.games.trivia.game.domain.Players;

public class StartException extends GameException {
    private StartException(GameId gameId, String message) {
        super(gameId, message);
    }

    public static StartException onlyCreatorCanStartGame(GameId gameId, UserId playerId) {
        return new StartException(gameId, "player id=%s tried to start game id=%s but is not the creator".formatted(playerId, gameId));
    }

    public static StartException invalidNumberOfPlayers(GameId gameId, int numberOfPlayers) {
        return new StartException(gameId, "game id=%s must have between %d and %d players to start, but was %d".formatted(gameId,
                Players.MIN_PLAYER_COUNT_AT_START_TIME,
                Players.MAX_PLAYER_COUNT,
                numberOfPlayers));
    }
}
