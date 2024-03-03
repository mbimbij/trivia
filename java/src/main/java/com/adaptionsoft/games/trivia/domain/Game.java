package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.Event;
import com.adaptionsoft.games.trivia.microarchitecture.EventPublisher;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Game extends EventRaiser {
    private final EventPublisher eventPublisher;

    private final Players players;

    private final Questions questions;
    private final Random rand;
    private final Board board;

    private boolean isGameInProgress = true;
    int turn = 1;
    private Player currentPlayer;
    private PlayerTurnOrchestrator playerTurnOrchestrator;


    // do not call directly, unless in a testing context
    public Game(EventPublisher eventPublisher, Players players, Questions questions, Random rand, Board board) {
        this.eventPublisher = eventPublisher;
        this.players = players;
        this.questions = questions;
        this.rand = rand;
        this.board = board;
        currentPlayer = players.getCurrent();
        playerTurnOrchestrator = new PlayerTurnOrchestrator(questions, rand, board);
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        playerTurnOrchestrator.performTurn(currentPlayer);
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        endCurrentPlayerTurn();
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !currentPlayer.isWinning();
    }

    private void endCurrentPlayerTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    private void publishDomainEvents() {
        List<Event> aggregatedEvents = getAndClearUncommittedEvents();
        aggregatedEvents.addAll(currentPlayer.getAndClearUncommittedEvents());
        aggregatedEvents.addAll(playerTurnOrchestrator.getAndClearUncommittedEvents());
        aggregatedEvents.sort(Comparator.comparingInt(Event::getOrderNumber));
        eventPublisher.publish(aggregatedEvents);
    }
}
