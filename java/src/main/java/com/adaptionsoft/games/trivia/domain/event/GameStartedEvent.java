package com.adaptionsoft.games.trivia.domain.event;

public class GameStartedEvent extends GameEvent {
    public GameStartedEvent(Integer gameId) {
        super(gameId, "Game id=%d started".formatted(gameId));
    }
}
