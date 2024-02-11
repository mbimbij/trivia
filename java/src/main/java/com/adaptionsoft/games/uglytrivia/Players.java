package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Players {
    List<Player> players = new ArrayList<>();
    int currentPlayerIndex = 0;

    public Players(String... playersNames) {
        if (playersNames.length < 2 || playersNames.length > 6){
            throw new InvalidNumberOfPlayersException(playersNames.length);
        }
        Arrays.stream(playersNames).forEach(this::addPlayer);
    }

    void addPlayer(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    void goToNext() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    Player getCurrent() {
        return players.get(currentPlayerIndex);
    }

    public int size() {
        return players.size();
    }

}