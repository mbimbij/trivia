package com.adaptionsoft.games.uglytrivia;

public class InvalidNumberOfPlayersException extends RuntimeException {
    public InvalidNumberOfPlayersException(int playersCount) {
        super("number of players must be between 2 and 6, but was: %d".formatted(playersCount));
    }
}
