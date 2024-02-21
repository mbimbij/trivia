package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.EventPublisher;

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
