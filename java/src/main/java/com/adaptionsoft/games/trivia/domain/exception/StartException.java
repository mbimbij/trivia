package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.Game;
import com.adaptionsoft.games.trivia.domain.Players;

public class StartException extends GameException {
    private StartException(Integer gameId, String message) {
        super(gameId, message);
    }

    public static StartException onlyCreatorCanStartGame(Integer gameId, Integer playerId) {
        return new StartException(gameId, "player id=%d tried to start game id=%d but is not the creator".formatted(playerId, gameId));
    }

    public static StartException invalidNumberOfPlayers(Integer gameId, int numberOfPlayers) {
        return new StartException(gameId, "game id=%d must have between %d and %d players to start, but was %d".formatted(gameId,
                Players.MIN_PLAYER_COUNT_AT_START_TIME,
                Players.MAX_PLAYER_COUNT,
                numberOfPlayers));
    }
}
