package com.adaptionsoft.games.trivia.domain.event;

public class GameCreatedEvent extends GameEvent {
    public GameCreatedEvent(Integer gameId) {
        super(gameId, "Game created");
    }
}
