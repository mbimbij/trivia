package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Players {
    public static final int MIN_PLAYER_COUNT = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    List<Player> players = new ArrayList<>();
    int currentPlayerIndex = 0;

    public Players(String... playersNames) {
        if (playersNames.length < MIN_PLAYER_COUNT || playersNames.length > MAX_PLAYER_COUNT){
            throw new InvalidNumberOfPlayersException(playersNames.length);
        }
        if(findDuplicates(playersNames)){
            throw new DuplicatePlayerNameException(playersNames);
        }

        Arrays.stream(playersNames).forEach(this::addPlayer);
    }

    private boolean findDuplicates(String[] playersNames) {
        for (int i = 0; i < playersNames.length; i++) {
            for (int j = i+1; j < playersNames.length; j++) {
                if (Objects.equals(playersNames[i], playersNames[j])){
                    return true;
                }
            }
        }
        return false;
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

    public static class DuplicatePlayerNameException extends RuntimeException {
        public DuplicatePlayerNameException(String... playersNames) {
        }
    }

    public static class InvalidNumberOfPlayersException extends RuntimeException {
        public InvalidNumberOfPlayersException(int playersCount) {
            super("number of players must be between 2 and 6, but was: %d".formatted(playersCount));
        }
    }
}