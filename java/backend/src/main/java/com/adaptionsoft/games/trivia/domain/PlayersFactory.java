package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class PlayersFactory {

    public static Players create(EventPublisher eventPublisher, Player creator, Player... otherPlayers) {
        Players players = new Players(eventPublisher);
        players.addCreator(creator);
        Arrays.stream(otherPlayers).forEach(players::add);
        players.validateOnCreation();
        return players;
    }
}
