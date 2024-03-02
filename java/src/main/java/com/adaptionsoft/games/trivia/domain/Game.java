package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
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

    private final int squaresCount;

    private Player currentPlayer;
    private boolean isGameInProgress = true;
    int turn = 1;


    // do not call directly, unless in a testing context
    public Game(EventPublisher eventPublisher, Players players, Questions questions, int squaresCount, Random rand) {
        this.eventPublisher = eventPublisher;
        this.players = players;
        currentPlayer = players.getCurrent();
        this.questions = questions;
        this.squaresCount = squaresCount;
        this.rand = rand;
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        performCurrentPlayerTurn();
        endGameIfCurrentPlayerWon();
        publishDomainEvents();
        endCurrentPlayerTurn();
    }

    private void performCurrentPlayerTurn() {
        raise(new PlayerTurnStartedEvent(currentPlayer));
        int roll = rollDice();
        if (currentPlayer.isInPenaltyBox()) {
            playFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
    }

    private int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(currentPlayer, roll));
        return roll;
    }

    private void playFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            raise(new PlayerGotOutOfPenaltyBoxEvent(currentPlayer));
            playRegularTurn(roll);
        } else {
            raise(new PlayerStayedInPenaltyBoxEvent(currentPlayer));
        }
    }


    void playRegularTurn(int roll) {
        currentPlayer.updateLocation(computeNewPlayerLocation(roll));
        askQuestion();
    }

    private void askQuestion() {
        boolean isAnswerCorrect;
        do {
            isAnswerCorrect = doAskQuestion();
        } while (!isAnswerCorrect && currentPlayer.canContinueAfterIncorrectAnswer());
    }

    private boolean doAskQuestion() {
        drawQuestion();
        boolean isAnswerCorrect = false;
        if (isAnsweringCorrectly()) {
            currentPlayer.answerCorrectly();
            isAnswerCorrect = true;
        } else {
            currentPlayer.answerIncorrectly();
        }
        return isAnswerCorrect;
    }

    private void drawQuestion() {
        String question = questions.drawQuestion(currentPlayer.getLocation());
        raise(new QuestionAskedToPlayerEvent(currentPlayer, question));
    }

    private int computeNewPlayerLocation(int roll) {
        return (currentPlayer.getLocation() + roll) % squaresCount;
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
        aggregatedEvents.sort(Comparator.comparingInt(Event::getOrderNumber));
        eventPublisher.publish(aggregatedEvents);
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    boolean isAnsweringCorrectly() {
        return rand.nextInt(9) != 7;
    }
}
