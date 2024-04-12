package com.adaptionsoft.games.trivia.domain.exception;

import com.adaptionsoft.games.trivia.domain.Players;

public class InvalidNumberOfPlayersException extends BusinessException {
    private InvalidNumberOfPlayersException(String message) {
        super(message);
    }

    public static InvalidNumberOfPlayersException onCreation(int playersCount) {
        return new InvalidNumberOfPlayersException("number of players at creation time must be between 1 and 6, but was: %d".formatted(playersCount));
    }

    public static InvalidNumberOfPlayersException onAdd() {
        return new InvalidNumberOfPlayersException("Tried to add another player but cannot have more than 6 in a game.");
    }
}
