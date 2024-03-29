package com.adaptionsoft.games.trivia.domain;

import com.adaptionsoft.games.trivia.domain.event.*;
import com.adaptionsoft.games.trivia.microarchitecture.Entity;
import com.adaptionsoft.games.trivia.microarchitecture.EventRaiser;
import lombok.*;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@AllArgsConstructor
public class Player extends Entity {
    @EqualsAndHashCode.Include
    @Getter(PUBLIC)
    private final String name;

    public Player(String name) {
        this.name = name;
    }

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @With // for testing purposes only
    @Getter(PUBLIC)
    private int coinCount;
    @Getter(PUBLIC)
    @Setter
    private int location;
    @Getter(PUBLIC)
    private int turn = 1;
    @Getter(PACKAGE)
    private boolean isInPenaltyBox;
    @Setter // for testing purposes only
    private int consecutiveCorrectAnswersCount;
    private int consecutiveIncorrectAnswersCount;

    void incrementTurn() {
        turn++;
    }

    boolean isWinning() {
        return (coinCount >= 12);
    }

    boolean canContinueAfterIncorrectAnswer() {
        int maxAllowed = 2;
        return consecutiveIncorrectAnswersCount < maxAllowed;
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
        consecutiveIncorrectAnswersCount++;
        if (consecutiveIncorrectAnswersCount >= 2) {
            goToPenaltyBox();
        }
        consecutiveCorrectAnswersCount = 0;
    }

    private void goToPenaltyBox() {
        isInPenaltyBox = true;
        raise(new PlayerSentToPenaltyBoxEvent(this));
    }

    void updateLocation(int newLocation) {
        setLocation(newLocation);
        Questions.Category category = Questions.Category.getQuestionCategory(getLocation());
        raise(new PlayerChangedLocationEvent(this,category));
    }
}
