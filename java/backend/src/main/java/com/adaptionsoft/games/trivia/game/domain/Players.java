package com.adaptionsoft.games.trivia.game.domain;

import com.adaptionsoft.games.trivia.game.domain.exception.DuplicatePlayerNameException;
import com.adaptionsoft.games.trivia.game.domain.exception.InvalidNumberOfPlayersException;
import com.adaptionsoft.games.trivia.game.domain.exception.PlayerAlreadyJoinedException;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.shared.microarchitecture.EventRaiser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Players extends EventRaiser {
    public static final int MIN_PLAYER_COUNT_AT_START_TIME = 2;
    public static final int MAX_PLAYER_COUNT = 6;
    @Getter
    @Setter
    private Player creator;
    @Getter
    private List<Player> individualPlayers = new ArrayList<>();
    private int currentPlayerIndex = 0;
    @Setter // for testing purposes only
    private PlayersShuffler shuffler = new RandomPlayersShuffler();

    public Players(EventPublisher eventPublisher, Player creator, Player... otherPlayers) {
        super(eventPublisher);
        addCreator(creator);
        Arrays.stream(otherPlayers).forEach(this::add);
    }

    private void addCreator(Player creator) {
        setCreator(creator);
        this.individualPlayers.add(creator);
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

    public void setCurrent(Player player) {
        Player newCurrentPlayer = this.individualPlayers.stream()
                .filter(p -> Objects.equals(p.getId(), player.getId()))
                .findAny()
                .orElseThrow();
        currentPlayerIndex = individualPlayers.indexOf(newCurrentPlayer);
    }

    public void shuffle() {
        this.individualPlayers = this.shuffler.shuffle(this.individualPlayers);
    }


    public List<Player> getIndividualPlayersOtherThanCreator() {
        return this.individualPlayers.stream()
                .filter(player -> !Objects.equals(creator, player))
                .toList();
    }
}
