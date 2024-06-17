package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlayersFactory {

    public static Players create(EventPublisher eventPublisher, Player creator, Player... otherPlayers) {
        Players players = new Players(eventPublisher, creator, otherPlayers);
        players.validateOnCreation();
        return players;
    }
}
