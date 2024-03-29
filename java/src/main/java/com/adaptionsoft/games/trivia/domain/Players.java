package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.PlayerAddedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Players extends EventRaiser {
    public static final int MIN_PLAYER_COUNT = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    private static final int MIN_PLAYER_COUNT_CREATION_TIME = 1;
    @Getter
    private final Player creator;
    @Getter
    private List<Player> individualPlayers = new ArrayList<>();
    private int currentPlayerIndex = 0;


    public Players(Player... individualPlayers) {
        validatePlayerCountAtCreationTime(individualPlayers);
        if (findDuplicates(individualPlayers)) {
            throw new DuplicatePlayerNameException(individualPlayers);
        }

        creator = individualPlayers[0];

        Arrays.stream(individualPlayers).forEach(player -> {
            this.individualPlayers.add(player);
            PlayerAddedEvent event = new PlayerAddedEvent(player, this.individualPlayers.size());
            raise(event);
        });
    }

    private void validatePlayerCountAtCreationTime(Player[] individualPlayers) {
        if (individualPlayers.length < MIN_PLAYER_COUNT_CREATION_TIME || individualPlayers.length > MAX_PLAYER_COUNT) {
            throw new InvalidNumberOfPlayersAtCreationTimeException(individualPlayers.length);
        }
    }

    private void validatePlayerCount(Player[] individualPlayers) {
        if (individualPlayers.length < MIN_PLAYER_COUNT || individualPlayers.length > MAX_PLAYER_COUNT) {
            throw new InvalidNumberOfPlayersException(individualPlayers.length);
        }
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
        currentPlayerIndex = (currentPlayerIndex + 1) % individualPlayers.size();
        individualPlayers.forEach(Player::incrementTurn);
    }

    public Player getCurrent() {
        return individualPlayers.get(currentPlayerIndex);
    }

    public int size() {
        return individualPlayers.size();
    }

    public static class DuplicatePlayerNameException extends RuntimeException {
        public DuplicatePlayerNameException(Player[] players) {
            super("duplicate player names in %s".formatted(Arrays.stream(players)
                    .map(Player::getName)
                    .collect(Collectors.joining(","))));
        }
    }

    public static class InvalidNumberOfPlayersAtCreationTimeException extends RuntimeException {
        public InvalidNumberOfPlayersAtCreationTimeException(int playersCount) {
            super("number of players at creation time must be between 1 and 6, but was: %d".formatted(playersCount));
        }
    }

    public static class InvalidNumberOfPlayersException extends RuntimeException {
        public InvalidNumberOfPlayersException(int playersCount) {
            super("number of players must be between 2 and 6, but was: %d".formatted(playersCount));
        }
    }
}