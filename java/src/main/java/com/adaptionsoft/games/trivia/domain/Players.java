package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.PlayerAddedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Players extends Entity {
    public static final int MIN_PLAYER_COUNT = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    List<Player> players = new ArrayList<>();
    int currentPlayerIndex = 0;


    public Players(Player... players) {
        if (players.length < MIN_PLAYER_COUNT || players.length > MAX_PLAYER_COUNT) {
            throw new InvalidNumberOfPlayersException(players.length);
        }
        if (findDuplicates(players)) {
            throw new DuplicatePlayerNameException(players);
        }

        Arrays.stream(players).forEach(player -> {
            this.players.add(player);
            PlayerAddedEvent event = new PlayerAddedEvent(player, this.players.size());
            raise(event);
        });
    }

    private boolean findDuplicates(Player[] players) {
        String[] playersNames = Arrays.stream(players).map(Player::getName).toArray(String[]::new);
        return findDuplicates(playersNames);
    }

    private boolean findDuplicates(String[] playersNames) {
        for (int i = 0; i < playersNames.length; i++) {
            for (int j = i + 1; j < playersNames.length; j++) {
                if (Objects.equals(playersNames[i], playersNames[j])) {
                    return true;
                }
            }
        }
        return false;
    }

    public void goToNextPlayerTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        players.forEach(Player::incrementTurn);
    }

    public Player getCurrent() {
        return players.get(currentPlayerIndex);
    }

    public int size() {
        return players.size();
    }

    public static class DuplicatePlayerNameException extends RuntimeException {
        public DuplicatePlayerNameException(Player[] players) {
            super("duplicate player names in %s".formatted(Arrays.stream(players)
                    .map(Player::getName)
                    .collect(Collectors.joining(","))));
        }
    }

    public static class InvalidNumberOfPlayersException extends RuntimeException {
        public InvalidNumberOfPlayersException(int playersCount) {
            super("number of players must be between 2 and 6, but was: %d".formatted(playersCount));
        }
    }
}