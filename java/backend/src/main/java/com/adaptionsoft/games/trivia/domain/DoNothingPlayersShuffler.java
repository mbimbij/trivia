package com.adaptionsoft.games.trivia.domain;

import java.util.List;

public class DoNothingPlayersShuffler implements PlayersShuffler {
    @Override
    public List<Player> shuffle(List<Player> players) {
        return players;
    }
}
