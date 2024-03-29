package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.PlayerAddedEvent;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public class Players extends EventRaiser {
    private static final int MIN_PLAYER_COUNT_AT_CREATION_TIME = 1;
    public static final int MIN_PLAYER_COUNT_AT_START_TIME = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    @Getter
    private final Player creator;
    @Getter
    private List<Player> individualPlayers = new ArrayList<>();
    private int currentPlayerIndex = 0;


    public Players(Player... individualPlayers) {
        if (individualPlayers.length < MIN_PLAYER_COUNT_AT_CREATION_TIME || individualPlayers.length > MAX_PLAYER_COUNT) {
            throw InvalidNumberOfPlayersException.onCreation(individualPlayers.length);
        }
        if (findDuplicatesAtCreationTime(individualPlayers)) {
            throw DuplicatePlayerNameException.onCreation(individualPlayers);
        }

        creator = individualPlayers[0];

        Arrays.stream(individualPlayers).forEach(this::add);
    }

    private void add(Player newPlayer) {
        individualPlayers.add(newPlayer);
        raise(new PlayerAddedEvent(newPlayer, individualPlayers.size()));
    }

    public void addAfterCreationTime(Player newPlayer) {
        if (isNameDuplicate(newPlayer)) {
            throw DuplicatePlayerNameException.onAdd(newPlayer, individualPlayers);
        }
        if (individualPlayers.size() + 1 > MAX_PLAYER_COUNT) {
            throw InvalidNumberOfPlayersException.onAdd();
        }
        add(newPlayer);
    }

    private boolean findDuplicatesAtCreationTime(Player[] players) {
        String[] playersNames = Arrays.stream(players).map(Player::getName).toArray(String[]::new);
        return findDuplicates(playersNames);
    }

    private boolean isNameDuplicate(Player newPlayer) {
        List<Player> mergedPlayers = new ArrayList<>(individualPlayers);
        mergedPlayers.add(newPlayer);
        String[] mergedPlayersNames = mergedPlayers.stream()
                .map(Player::getName)
                .toArray(String[]::new);
        return findDuplicates(mergedPlayersNames);
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
        private DuplicatePlayerNameException(String message) {
            super(message);
        }

        public static DuplicatePlayerNameException onCreation(Player[] players) {
            String playerNames = Arrays.stream(players)
                    .map(Player::getName)
                    .collect(Collectors.joining(","));
            String message = "duplicate player names on game creation: %s".formatted(playerNames);
            return new DuplicatePlayerNameException(message);
        }

        public static DuplicatePlayerNameException onAdd(Player newPlayer, Collection<Player> existingPlayers) {
            String existingPlayerNames = existingPlayers.stream()
                    .map(Player::getName)
                    .collect(Collectors.joining(","));
            String message = "duplicate player name on player join: %s. Existing player names:%s".formatted(newPlayer.getName(), existingPlayerNames);
            return new DuplicatePlayerNameException(message);
        }
    }

    public static class InvalidNumberOfPlayersException extends RuntimeException {
        private InvalidNumberOfPlayersException(String message) {
            super(message);
        }

        public static InvalidNumberOfPlayersException onCreation(int playersCount){
            return new InvalidNumberOfPlayersException("number of players at creation time must be between 1 and 6, but was: %d".formatted(playersCount));
        }

        public static InvalidNumberOfPlayersException onAdd(){
            return new InvalidNumberOfPlayersException("Tried to add another player but cannot have more than 6 in a game.");
        }
    }
}