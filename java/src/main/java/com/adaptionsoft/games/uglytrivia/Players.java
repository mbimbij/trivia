package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.Event;
import com.adaptionsoft.games.uglytrivia.event.EventPublisher;
import com.adaptionsoft.games.uglytrivia.event.PlayerAddedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Players {
    public static final int MIN_PLAYER_COUNT = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    List<Player> players = new ArrayList<>();
    int currentPlayerIndex = 0;
    private final EventPublisher eventPublisher;

    public Players(EventPublisher eventPublisher, Player... players) {
        this.eventPublisher = eventPublisher;
        if (players.length < MIN_PLAYER_COUNT || players.length > MAX_PLAYER_COUNT) {
            throw new InvalidNumberOfPlayersException(players.length);
        }
        if (findDuplicates(players)) {
            throw new DuplicatePlayerNameException(players);
        }

        Arrays.stream(players).forEach(this::addPlayer);
    }

    private void addPlayer(Player player) {
        players.add(player);
        PlayerAddedEvent event = new PlayerAddedEvent(player, players.size());
        publish(event);
    }

    private void publish(Event event) {
        eventPublisher.publish(event);
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

    public Player getByName(String playerName) {
        return players.stream().filter(p -> p.getName().equals(playerName)).findFirst().orElseThrow();
    }

    public static class DuplicatePlayerNameException extends RuntimeException {
        public DuplicatePlayerNameException(String... playersNames) {
            super("duplicate player names in %s".formatted(Arrays.toString(playersNames)));
        }

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