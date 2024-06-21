package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.PlayerAddedEvent;
import com.adaptionsoft.games.trivia.domain.exception.PlayerAlreadyJoinedException;
import com.adaptionsoft.games.trivia.domain.exception.DuplicatePlayerNameException;
import com.adaptionsoft.games.trivia.domain.exception.InvalidNumberOfPlayersException;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Players extends EventRaiser {
    public static final int MIN_PLAYER_COUNT_AT_START_TIME = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    @Getter
    @Setter
    private Player creator;
    @Getter
    private List<Player> individualPlayers = new ArrayList<>();
    private int currentPlayerIndex = 0;

    public Players(EventPublisher eventPublisher, Player creator, Player... individualPlayers) {
        super(eventPublisher);
        addCreator(creator);
        Arrays.stream(individualPlayers).forEach(this::add);
    }

    private void addCreator(Player creator) {
        setCreator(creator);
        this.individualPlayers.add(creator);
        raise(new PlayerAddedEvent(creator, this.individualPlayers.size(), creator.getTurn()));
    }

    public void validateOnCreation() {
        if (individualPlayers.isEmpty() || individualPlayers.size() > MAX_PLAYER_COUNT) {
            throw InvalidNumberOfPlayersException.onCreation(individualPlayers.size());
        }
        if (findDuplicatesAtCreationTime(individualPlayers)) {
            throw DuplicatePlayerNameException.onCreation(individualPlayers);
        }
    }

    public void add(Player newPlayer) {
        if (individualPlayers.contains(newPlayer)) {
            throw new PlayerAlreadyJoinedException(newPlayer);
        }
        if (isNameDuplicate(newPlayer)) {
            throw DuplicatePlayerNameException.onAdd(newPlayer, individualPlayers);
        }
        if (individualPlayers.size() + 1 > MAX_PLAYER_COUNT) {
            throw InvalidNumberOfPlayersException.onAdd();
        }
        individualPlayers.add(newPlayer);
        raise(new PlayerAddedEvent(newPlayer, this.individualPlayers.size(), newPlayer.getTurn()));
    }

    private boolean findDuplicatesAtCreationTime(List<Player> players) {
        String[] playersNames = players.stream().map(Player::getName).toArray(String[]::new);
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

    public int count() {
        return individualPlayers.size();
    }

}