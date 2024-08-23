package com.adaptionsoft.games.trivia.game.domain.exception;

public class InvalidNumberOfPlayersException extends BusinessException {
    private InvalidNumberOfPlayersException(String message) {
        super(message);
    }

    public static InvalidNumberOfPlayersException onAdd() {
        return new InvalidNumberOfPlayersException("Tried to add another player but cannot have more than 6 in a game.");
    }
}
