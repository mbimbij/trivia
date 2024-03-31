package com.adaptionsoft.games.trivia.domain;

import java.util.Arrays;

public class PlayersFactory {
    public static Players create(Player creator, Player... otherPlayers) {
        Players players = new Players();
        players.addCreator(creator);
        Arrays.stream(otherPlayers).forEach(players::add);
        players.validateOnCreation();
        return players;
    }
}
