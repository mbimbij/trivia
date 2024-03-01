package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;

public class Game extends Entity {
    private final Players players;
    private final EventPublisher eventPublisher;
    private boolean isGameInProgress = true;
    private Player currentPlayer;
    int turn = 1;


    // do not call directly, unless in a testing context
    public Game(Players players, EventPublisher eventPublisher) {
        this.players = players;
        this.eventPublisher = eventPublisher;
        currentPlayer = players.getCurrent();
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
        eventPublisher.raise(getAndClearUncommittedEvents());
        eventPublisher.raise(currentPlayer.getAndClearUncommittedEvents());
    }

}
