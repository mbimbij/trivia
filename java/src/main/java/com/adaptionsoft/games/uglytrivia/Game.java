package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends Entity {
    final Players players;
    private final EventPublisher eventPublisher;
    private boolean isGameInProgress = true;
    public Player currentPlayer;
    int turn = 1;

    // do not call directly, unless in a testing context
    public Game(Players players, EventPublisher eventPublisher) {
        this.players = players;
        this.eventPublisher = eventPublisher;
        currentPlayer = this.players.getCurrent();
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        currentPlayer.playTurn();
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        goToNextPlayer();
    }


    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !currentPlayer.isWinning();
    }

    private void goToNextPlayer() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    private void publishDomainEvents() {
        eventPublisher.raise(getUncommittedEventsAndClear());
        eventPublisher.raise(currentPlayer.getUncommittedEventsAndClear());
    }
}
