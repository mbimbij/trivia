package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    final Players players;
    private final EventPublisher eventPublisher;

    private final Random rand;
    private final Board board;
    private boolean isGameInProgress = true;
    public Player currentPlayer;
    private final List<Event> uncommittedEvents = new ArrayList<>();
    int turn = 1;

    // do not call directly, unless in a testing context
    public Game(Random rand, Board board, Players players, EventPublisher eventPublisher) {
        this.rand = rand;
        this.players = players;
        this.eventPublisher = eventPublisher;
        currentPlayer = this.players.getCurrent();
        this.board = board;
    }

    private void publish(Event... events) {
        uncommittedEvents.addAll(Arrays.asList(events));
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        publish(new PlayerTurnStartedEvent(currentPlayer));
        int roll = rollDice();
        if (currentPlayer.isInPenaltyBox()) {
            playTurnFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
        endGameIfCurrentPlayerWon();
        endTurn();
    }

    private int rollDice() {
        int roll = this.rand.nextInt(5) + 1;
        publish(new PlayerRolledDiceEvent(currentPlayer, roll));
        return roll;
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            publish(new PlayerGotOutOfPenaltyBoxEvent(currentPlayer));
            playRegularTurn(roll);
        } else {
            publish(new PlayerStayedInPenaltyBoxEvent(currentPlayer));
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
        publish(new PlayerChangedLocationEvent(currentPlayer,
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
        eventPublisher.publish(getUncommittedEventsAndClear());
        eventPublisher.publish(currentPlayer.getUncommittedEventsAndClear());

        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }
}
