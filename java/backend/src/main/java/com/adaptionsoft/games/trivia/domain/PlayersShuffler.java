package com.adaptionsoft.games.trivia.domain;

import java.util.List;

@FunctionalInterface
public interface PlayersShuffler {
    List<Player> shuffle(List<Player> players);
}
