package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.event.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    final Players players;
    private final EventPublisher eventPublisher;

    private final Random rand;
    private final Board board;
    private boolean isGameInProgress = true;
    @Getter
    private Player currentPlayer;
    @Getter
    private final List<Event> uncommittedEvents = new ArrayList<>();
    int turn = 1;

    // do not call directly, unless in a testing context
    Game(Random rand, Board board, Players players, EventPublisher eventPublisher) {
        this.rand = rand;
        this.players = players;
        this.eventPublisher = eventPublisher;
        currentPlayer = this.players.getCurrent();
        this.board = board;
        publish(new GameCreatedEvent());
    }

    private void publish(Event... events) {
        eventPublisher.publish(events);
    }

    public void play() {
        do {
            performGameTurn();
        } while (isGameInProgress);
    }

    public void performGameTurn() {
        publish(new PlayerTurnStartedEvent(currentPlayer, turn));
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
        publish(new PlayerRolledDiceEvent(currentPlayer, turn, roll));
        return roll;
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            publish(new PlayerGotOutOfPenaltyBoxEvent(currentPlayer, turn));
            playRegularTurn(roll);
        } else {
            publish(new PlayerStayedInPenaltyBoxEvent(currentPlayer, turn));
        }
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private void playRegularTurn(int roll) {
        advancePlayerLocation(roll);
        askQuestionToCurrentPlayer();
    }

    private void advancePlayerLocation(int roll) {
        currentPlayer.advanceLocation(roll);
        Questions questionCategory = board.getQuestionCategory(currentPlayer.getLocation());
        publish(new PlayerChangedLocationEvent(currentPlayer,
                turn,
                questionCategory));
    }

    private void endGameIfCurrentPlayerWon() {
        this.isGameInProgress = !hasCurrentPlayerWon();
    }

    private void endTurn() {
        players.goToNextPlayerTurn();
        currentPlayer = players.getCurrent();
        turn++;
    }

    public void askQuestionToCurrentPlayer() {
        askQuestion();
        if (currentPlayer.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
            askSecondQuestionAfterFirstIncorrectAnswer();
        }
    }

    private void askSecondQuestionAfterFirstIncorrectAnswer() {
        askQuestion();
        if (currentPlayer.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
            goToPenaltyBox();
        }
    }

    private void askQuestion() {
        String question = board.drawQuestion(currentPlayer.getLocation());
        publish(new QuestionAskedToPlayerEvent(currentPlayer, turn, question));
    }

    private void answerCorrectly() {
        currentPlayer.addCoin();
        publish(new PlayerAnsweredCorrectlyEvent(currentPlayer, turn),
                new CoinAddedToPlayerEvent(currentPlayer, turn)
        );
    }

    private void answerIncorrectly() {
        publish(new PlayerAnsweredIncorrectlyEvent(currentPlayer, turn));
    }

    private void goToPenaltyBox() {
        currentPlayer.goToPenaltyBox();
        publish(new PlayerSentToPenaltyBoxEvent(currentPlayer, turn));
    }


    private boolean hasCurrentPlayerWon() {
        return (currentPlayer.getCoinCount() == 6);
    }

}
