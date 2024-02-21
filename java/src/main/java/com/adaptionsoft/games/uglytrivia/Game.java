package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    final Players players;
    private final EventPublisher eventPublisher;
    private final Board board;
    private boolean isGameInProgress = true;
    public Player currentPlayer;
    private final List<Event> uncommittedEvents = new ArrayList<>();
    int turn = 1;

    // do not call directly, unless in a testing context
    public Game(Board board, Players players, EventPublisher eventPublisher) {
        this.players = players;
        this.eventPublisher = eventPublisher;
        currentPlayer = this.players.getCurrent();
        this.board = board;
    }

    private void raise(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        raise(new PlayerTurnStartedEvent(currentPlayer));
        int roll = currentPlayer.rollDice();
        if (currentPlayer.isInPenaltyBox()) {
            playTurnFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        endTurn();
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            raise(new PlayerGotOutOfPenaltyBoxEvent(currentPlayer));
            playRegularTurn(roll);
        } else {
            raise(new PlayerStayedInPenaltyBoxEvent(currentPlayer));
        }
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private void playRegularTurn(int roll) {
        advancePlayerLocation(roll);
        currentPlayer.drawQuestion();
        currentPlayer.answerQuestion();
    }
    private void advancePlayerLocation(int roll) {
        currentPlayer.advanceLocation(roll);
        QuestionCategory questionCategory = board.getQuestionCategory(currentPlayer.getLocation());
        raise(new PlayerChangedLocationEvent(currentPlayer,
                questionCategory));
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !currentPlayer.isWinning();
    }

    private List<Event> getUncommittedEventsAndClear() {
        List<Event> eventsCopy = new ArrayList<>(uncommittedEvents);
        uncommittedEvents.clear();
        return eventsCopy;
    }

    private void endTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    private void publishDomainEvents() {
        eventPublisher.raise(getUncommittedEventsAndClear());
        eventPublisher.raise(currentPlayer.getUncommittedEventsAndClear());
    }
}
