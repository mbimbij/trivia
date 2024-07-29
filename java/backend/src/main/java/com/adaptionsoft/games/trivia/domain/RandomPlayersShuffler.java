package com.adaptionsoft.games.trivia.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPlayersShuffler implements PlayersShuffler {
    @Override
    public List<Player> shuffle(List<Player> players) {
        ArrayList<Player> shuffledList = new ArrayList<>(players);
        Collections.shuffle(shuffledList);
        return shuffledList;
    }
}
