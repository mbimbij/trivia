package com.adaptionsoft.games.trivia.domain.event;

public class GameCreatedEvent extends Event {
    public GameCreatedEvent() {
        super("Game created");
    }
}
