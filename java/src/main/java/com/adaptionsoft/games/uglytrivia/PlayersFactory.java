package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.EventPublisher;

public class PlayersFactory {

    public static Players create(EventPublisher eventPublisher, Player... playersArray) {
        Players players = new Players(eventPublisher, playersArray);
        players.getUncommittedEvents().forEach(eventPublisher::publish);
        return players;
    }
}
