package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

public class PlayersFactory {

    private final EventPublisher eventPublisher;

    public PlayersFactory(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public Players create(Player... playersArray) {
        Players players = new Players(playersArray);
        eventPublisher.raise(players.getUncommittedEventsAndClear());
        return players;
    }
}
