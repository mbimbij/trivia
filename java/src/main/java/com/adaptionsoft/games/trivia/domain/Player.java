package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import lombok.*;

import java.util.Random;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
public class Player extends Entity {
    @EqualsAndHashCode.Include
    @Getter(PUBLIC)
    private final String name;
    @With // for testing purposes only
    @Getter(PUBLIC)
    private int coinCount;
    @Getter(PUBLIC)
    private int location;
    @Getter(PUBLIC)
    private int turn = 1;
    private final AnsweringStrategy answeringStrategy;
    @Getter(PACKAGE)
    private boolean isInPenaltyBox;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    private final Board board;
    private final Random rand;

    public Player(String name, AnsweringStrategy answeringStrategy, Board board, Random rand) {
        this.name = name;
        this.answeringStrategy = answeringStrategy;
        this.board = board;
        this.rand = rand;
    }

    void incrementTurn() {
        turn++;
    }

    boolean isWinning() {
        return (coinCount >= 12);
    }

    void playTurn() {
        raise(new PlayerTurnStartedEvent(this));
        int roll = rollDice();
        if (isInPenaltyBox) {
            playTurnFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
    }

    private void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            raise(new PlayerGotOutOfPenaltyBoxEvent(this));
            playRegularTurn(roll);
        } else {
            raise(new PlayerStayedInPenaltyBoxEvent(this));
        }
    }

    private int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(this, roll));
        return roll;
    }

    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    private void playRegularTurn(int roll) {
        advancePlayerLocation(roll);
        drawQuestion();
        if (answeringStrategy.isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
        }
    }

    private void advancePlayerLocation(int roll) {
        updateLocation(roll);
        PlayerChangedLocationEvent event = new PlayerChangedLocationEvent(this,
                board.getQuestionCategory(getLocation()));
        raise(event);
    }

    private void updateLocation(int roll) {
        this.location = (this.location + roll) % board.getSquaresCount();
    }

    private void drawQuestion() {
        String question = board.drawQuestion(location);
        raise(new QuestionAskedToPlayerEvent(this, question));
    }

    /**
     * Used externally by tests ONLY
     */
    void answerCorrectly() {
        if (isOnAStreak()) {
            addCoin();
        }

        addCoin();
        consecutiveCorrectAnswersCount++;
        raise(new PlayerAnsweredCorrectlyEvent(this),
                new CoinAddedToPlayerEvent(this)
        );
    }

    /**
     * Used externally by tests ONLY
     */
    boolean isOnAStreak() {
        return consecutiveCorrectAnswersCount >= 3;
    }

    private void addCoin() {
        coinCount++;
    }

    /**
     * Used externally by tests ONLY
     */
    void answerIncorrectly() {
        raise(new PlayerAnsweredIncorrectlyEvent(this));
        if (!isOnAStreak()) {
            goToPenaltyBox();
        }
        consecutiveCorrectAnswersCount = 0;
    }

    private void goToPenaltyBox() {
        isInPenaltyBox = true;
        raise(new PlayerSentToPenaltyBoxEvent(this));
    }
}
