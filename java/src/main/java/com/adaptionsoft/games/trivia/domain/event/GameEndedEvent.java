package com.adaptionsoft.games.trivia.domain.event;

public class GameEndedEvent extends GameEvent {
    public GameEndedEvent(Integer gameId, Integer winnerId) {
        super(gameId,"game gameId=%d ended with winner playerId=%d".formatted(gameId, winnerId));
    }
}
