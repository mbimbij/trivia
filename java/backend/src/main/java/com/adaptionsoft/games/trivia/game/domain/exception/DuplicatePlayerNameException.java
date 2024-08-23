package com.adaptionsoft.games.trivia.game.domain.exception;

import com.adaptionsoft.games.trivia.game.domain.Player;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DuplicatePlayerNameException extends BusinessException {
    private DuplicatePlayerNameException(String message) {
        super(message);
    }

    public static DuplicatePlayerNameException onCreation(List<Player> players) {
        String playerNames = players.stream()
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
