package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import lombok.*;

import java.util.Random;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
@Getter
public class Player extends Entity {
    @EqualsAndHashCode.Include
    private final String name;
    private final AnsweringStrategy answeringStrategy;
    private boolean isInPenaltyBox;
    @With // for testing purposes only
    private int coinCount;
    private int location;
    private int turn = 1;
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

    void advanceLocation(int roll) {
        this.location = (this.location + roll) % board.getSquaresCount();
    }

    void addCoin() {
        coinCount++;
    }

    void goToPenaltyBox() {
        isInPenaltyBox = true;
    }

    void answerCorrectly() {
        if (isOnACorrectAnswersStreak()) {
            addCoin();
        }

        addCoin();
        raise(new PlayerAnsweredCorrectlyEvent(this),
                new CoinAddedToPlayerEvent(this)
        );
        consecutiveCorrectAnswersCount++;
    }

    void answerIncorrectly() {
        raise(new PlayerAnsweredIncorrectlyEvent(this));
        if (!isOnACorrectAnswersStreak()) {
            goToPenaltyBox();
            raise(new PlayerSentToPenaltyBoxEvent(this));
        }
        consecutiveCorrectAnswersCount = 0;
    }

    void incrementTurn() {
        turn++;
    }

    boolean isOnACorrectAnswersStreak() {
        return consecutiveCorrectAnswersCount >= 3;
    }

    void drawQuestion() {
        String question = board.drawQuestion(location);
        raise(new QuestionAskedToPlayerEvent(this, question));
    }

    boolean isWinning() {
        return (coinCount >= 12);
    }

    boolean isAnsweringCorrectly() {
        return answeringStrategy.isAnsweringCorrectly();
    }

    void answerQuestion() {
        if (isAnsweringCorrectly()) {
            answerCorrectly();
        } else {
            answerIncorrectly();
        }
    }

    int rollDice() {
        int roll = rand.nextInt(5) + 1;
        raise(new PlayerRolledDiceEvent(this, roll));
        return roll;
    }

    void advancePlayerLocation(int roll) {
        advanceLocation(roll);
        QuestionCategory questionCategory = board.getQuestionCategory(getLocation());
        raise(new PlayerChangedLocationEvent(this,
                questionCategory));
    }

    void playRegularTurn(int roll) {
        advancePlayerLocation(roll);
        drawQuestion();
        answerQuestion();
    }

    void playTurnFromPenaltyBox(int roll) {
        if (isPair(roll)) {
            raise(new PlayerGotOutOfPenaltyBoxEvent(this));
            playRegularTurn(roll);
        } else {
            raise(new PlayerStayedInPenaltyBoxEvent(this));
        }
    }


    private boolean isPair(int roll) {
        return roll % 2 != 0;
    }

    void playTurn() {
        raise(new PlayerTurnStartedEvent(this));
        int roll = rollDice();
        if (isInPenaltyBox()) {
            playTurnFromPenaltyBox(roll);
        } else {
            playRegularTurn(roll);
        }
    }
}
