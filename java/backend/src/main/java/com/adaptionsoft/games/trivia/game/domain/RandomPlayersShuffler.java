package com.adaptionsoft.games.trivia.game.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RandomPlayersShuffler implements PlayersShuffler {
    @Override
    public List<Player> shuffle(List<Player> players) {
        ArrayList<Player> originalList = new ArrayList<>(players);
        ArrayList<Player> shuffledList = new ArrayList<>(players);

        do {
            Collections.shuffle(shuffledList);
        }while (Objects.equals(originalList, shuffledList));

        return shuffledList;
    }
}
